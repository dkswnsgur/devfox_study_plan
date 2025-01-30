package shop.mtcoding.bank.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.bank.domain.user.User;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {

    //jpa query method
    //select * from account where number = :number
    //checkpoint : 리펙토링 해야함!! (계좌가 소유자 확인시 쿼리가 두번나가기 떄문에 join fetch) - accopunt.getUser().getId()
    //join fetch를 하면 조인해서 객체에 값을 미리 가져 올수 있다
    //@Query("SELECT ac FROM Account ac JOIN FETCH ac.user u WHERE ac.number = :number")
    Optional<Account> findByNumber(@Param("number") Long number);

    //jpa query method
    //select * from account where user_id = :id
    List<Account> findByUser_id(Long id);

}
