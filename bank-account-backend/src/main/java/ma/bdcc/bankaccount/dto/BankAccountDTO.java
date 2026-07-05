package ma.bdcc.bankaccount.dto;

import lombok.*;
import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor
public class BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private String status;
    private CustomerDTO customerDTO;
    private String type;
}
