package ma.bdcc.bankaccount.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
}
