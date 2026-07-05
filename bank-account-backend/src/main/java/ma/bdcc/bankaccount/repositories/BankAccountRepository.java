package ma.bdcc.bankaccount.repositories;

import ma.bdcc.bankaccount.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
