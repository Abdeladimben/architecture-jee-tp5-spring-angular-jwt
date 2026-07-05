package ma.bdcc.bankaccount.mappers;

import ma.bdcc.bankaccount.dto.*;
import ma.bdcc.bankaccount.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapper {

    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public BankAccountDTO fromBankAccount(BankAccount bankAccount) {
        if (bankAccount instanceof CurrentAccount) {
            CurrentAccountDTO dto = new CurrentAccountDTO();
            BeanUtils.copyProperties(bankAccount, dto);
            dto.setOverDraft(((CurrentAccount) bankAccount).getOverDraft());
            dto.setType("CA");
            dto.setCustomerDTO(fromCustomer(bankAccount.getCustomer()));
            return dto;
        } else if (bankAccount instanceof SavingAccount) {
            SavingAccountDTO dto = new SavingAccountDTO();
            BeanUtils.copyProperties(bankAccount, dto);
            dto.setInterestRate(((SavingAccount) bankAccount).getInterestRate());
            dto.setType("SA");
            dto.setCustomerDTO(fromCustomer(bankAccount.getCustomer()));
            return dto;
        }
        return null;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation operation) {
        AccountOperationDTO dto = new AccountOperationDTO();
        BeanUtils.copyProperties(operation, dto);
        dto.setType(operation.getType().toString());
        return dto;
    }
}
