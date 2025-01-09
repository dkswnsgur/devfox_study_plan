package shop.mtcoding.bank.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.bank.domain.user.User;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
