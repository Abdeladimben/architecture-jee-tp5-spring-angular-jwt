package ma.bdcc.bankaccount.repositories;

import ma.bdcc.bankaccount.entities.SavingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingAccountRepository extends JpaRepository<SavingAccount, String> {
}
