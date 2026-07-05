package ma.bdcc.bankaccount.repositories;

import ma.bdcc.bankaccount.entities.CurrentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentAccountRepository extends JpaRepository<CurrentAccount, String> {
}
