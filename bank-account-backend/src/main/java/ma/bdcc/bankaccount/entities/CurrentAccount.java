package ma.bdcc.bankaccount.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("CA")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CurrentAccount extends BankAccount {
    private double overDraft;
}
