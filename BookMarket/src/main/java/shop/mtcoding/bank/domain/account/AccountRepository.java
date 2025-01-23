package shop.mtcoding.bank.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.bank.domain.user.User;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {

    //jpa query method
    //select * from account where number = :number
    //checkpoint : 리펙토링 해야함
    Optional<Account> findByNumber(Long number);

    //jpa query method
    //select * from account where user_id = :id
    List<Account> findByUser_id(Long id);

}
