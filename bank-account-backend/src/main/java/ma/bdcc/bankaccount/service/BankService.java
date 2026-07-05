package ma.bdcc.bankaccount.service;

import ma.bdcc.bankaccount.dto.*;
import ma.bdcc.bankaccount.exception.BalanceNotSufficientException;
import java.util.List;

public interface BankService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> listCustomers();
    CustomerDTO getCustomer(Long customerId);
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
    List<CustomerDTO> searchCustomers(String keyword);

    CurrentAccountDTO saveCurrentAccount(double initialBalance, double overDraft, Long customerId);
    SavingAccountDTO saveSavingAccount(double initialBalance, double interestRate, Long customerId);
    List<BankAccountDTO> listAccounts();
    BankAccountDTO getAccount(String accountId);
    void deleteAccount(String accountId);

    void debit(String accountId, double amount, String description, String userId);
    void credit(String accountId, double amount, String description, String userId);
    void transfer(String accountSource, String accountDestination, double amount, String userId);
    List<AccountOperationDTO> accountHistory(String accountId);
}
