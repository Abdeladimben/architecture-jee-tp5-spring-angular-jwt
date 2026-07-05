package ma.bdcc.bankaccount.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CurrentAccountDTO extends BankAccountDTO {
    private double overDraft;
}
