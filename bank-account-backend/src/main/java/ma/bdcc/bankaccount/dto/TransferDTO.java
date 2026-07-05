package ma.bdcc.bankaccount.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class TransferDTO {
    private String accountSource;
    private String accountDestination;
    private double amount;
}
