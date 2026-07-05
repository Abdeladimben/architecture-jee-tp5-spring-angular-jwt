package ma.bdcc.bankaccount.dto;

import lombok.*;
import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private String type;
    private String description;
    private String userId;
}
