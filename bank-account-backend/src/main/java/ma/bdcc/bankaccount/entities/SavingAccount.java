package ma.bdcc.bankaccount.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("SA")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SavingAccount extends BankAccount {
    private double interestRate;
}
