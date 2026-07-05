package ma.bdcc.bankaccount.service;

import jakarta.transaction.Transactional;
import ma.bdcc.bankaccount.dto.*;
import ma.bdcc.bankaccount.entities.*;
import ma.bdcc.bankaccount.enums.AccountStatus;
import ma.bdcc.bankaccount.exception.BalanceNotSufficientException;
import ma.bdcc.bankaccount.exception.BankAccountNotFoundException;
import ma.bdcc.bankaccount.exception.CustomerNotFoundException;
import ma.bdcc.bankaccount.mappers.BankAccountMapper;
import ma.bdcc.bankaccount.repositories.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankServiceImpl implements BankService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private CurrentAccountRepository currentAccountRepository;
    private SavingAccountRepository savingAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapper mapper;

    public BankServiceImpl(CustomerRepository customerRepository,
                           BankAccountRepository bankAccountRepository,
                           CurrentAccountRepository currentAccountRepository,
                           SavingAccountRepository savingAccountRepository,
                           AccountOperationRepository accountOperationRepository,
                           BankAccountMapper mapper) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.currentAccountRepository = currentAccountRepository;
        this.savingAccountRepository = savingAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.mapper = mapper;
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = mapper.fromCustomerDTO(customerDTO);
        Customer saved = customerRepository.save(customer);
        return mapper.fromCustomer(saved);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
            .stream().map(mapper::fromCustomer).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));
        return mapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + id));
        if (customerDTO.getName() != null) customer.setName(customerDTO.getName());
        if (customerDTO.getEmail() != null) customer.setEmail(customerDTO.getEmail());
        Customer saved = customerRepository.save(customer);
        return mapper.fromCustomer(saved);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        return customerRepository.findByNameContains(keyword)
            .stream().map(mapper::fromCustomer).collect(Collectors.toList());
    }

    @Override
    public CurrentAccountDTO saveCurrentAccount(double initialBalance, double overDraft, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        CurrentAccount account = CurrentAccount.builder()
            .id(UUID.randomUUID().toString())
            .balance(initialBalance)
            .createdAt(new Date())
            .status(AccountStatus.CREATED)
            .customer(customer)
            .overDraft(overDraft)
            .build();
        CurrentAccount saved = currentAccountRepository.save(account);
        return (CurrentAccountDTO) mapper.fromBankAccount(saved);
    }

    @Override
    public SavingAccountDTO saveSavingAccount(double initialBalance, double interestRate, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        SavingAccount account = SavingAccount.builder()
            .id(UUID.randomUUID().toString())
            .balance(initialBalance)
            .createdAt(new Date())
            .status(AccountStatus.CREATED)
            .customer(customer)
            .interestRate(interestRate)
            .build();
        SavingAccount saved = savingAccountRepository.save(account);
        return (SavingAccountDTO) mapper.fromBankAccount(saved);
    }

    @Override
    public List<BankAccountDTO> listAccounts() {
        return bankAccountRepository.findAll()
            .stream().map(mapper::fromBankAccount).collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getAccount(String accountId) {
        BankAccount account = bankAccountRepository.findById(accountId)
            .orElseThrow(() -> new BankAccountNotFoundException("Account not found: " + accountId));
        return mapper.fromBankAccount(account);
    }

    @Override
    public void deleteAccount(String accountId) {
        bankAccountRepository.deleteById(accountId);
    }

    @Override
    public void debit(String accountId, double amount, String description, String userId) {
        BankAccount account = bankAccountRepository.findById(accountId)
            .orElseThrow(() -> new BankAccountNotFoundException("Account not found"));
        if (account.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance insufficient");
        }
        AccountOperation operation = AccountOperation.builder()
            .operationDate(new Date())
            .amount(amount)
            .type(OperationType.DEBIT)
            .description(description)
            .userId(userId)
            .bankAccount(account)
            .build();
        accountOperationRepository.save(operation);
        account.setBalance(account.getBalance() - amount);
        bankAccountRepository.save(account);
    }

    @Override
    public void credit(String accountId, double amount, String description, String userId) {
        BankAccount account = bankAccountRepository.findById(accountId)
            .orElseThrow(() -> new BankAccountNotFoundException("Account not found"));
        AccountOperation operation = AccountOperation.builder()
            .operationDate(new Date())
            .amount(amount)
            .type(OperationType.CREDIT)
            .description(description)
            .userId(userId)
            .bankAccount(account)
            .build();
        accountOperationRepository.save(operation);
        account.setBalance(account.getBalance() + amount);
        bankAccountRepository.save(account);
    }

    @Override
    public void transfer(String accountSource, String accountDestination, double amount, String userId) {
        debit(accountSource, amount, "Transfer to " + accountDestination, userId);
        credit(accountDestination, amount, "Transfer from " + accountSource, userId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        return accountOperationRepository.findByBankAccountId(accountId)
            .stream().map(mapper::fromAccountOperation).collect(Collectors.toList());
    }
}
