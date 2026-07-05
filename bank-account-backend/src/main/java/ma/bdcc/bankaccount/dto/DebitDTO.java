package ma.bdcc.bankaccount.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class DebitDTO {
    private String accountId;
    private double amount;
    private String description;
}
