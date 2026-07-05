package ma.bdcc.bankaccount.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class SavingAccountDTO extends BankAccountDTO {
    private double interestRate;
}
