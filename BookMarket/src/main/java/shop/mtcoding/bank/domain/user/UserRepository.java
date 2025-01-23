package shop.mtcoding.bank.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> { //JpaRepository는 Spring Data JPA에서 제공하는 인터페이스로, User 엔티티와 기본적인 CRUD 작업을 처리하는 메서드들을 자동으로 제공
            /*save(): 엔티티를 저장하거나 업데이트.
            findById(): 기본 키를 사용하여 엔티티 조회.
            findAll(): 모든 엔티티 조회.
            deleteById(): 기본 키를 사용하여 엔티티 삭제.*/

    //select * from user where username = ? //
    Optional<User> findByUsername(String username); //Jpa NameQuery 작동 //SQL로는 SELECT * FROM user WHERE username = ?와 같은 쿼리가 실행됩니다.
    //메서드의 반환 타입인 Optional<User>는 username에 해당하는 User 객체가 있을 수도 있고 없을 수도 있음을 나타냅니다. Optional을 사용하여 null 체크를 더 안전하게 처리할 수 있습니다.
    // save - 이미 만들어져있음
}
