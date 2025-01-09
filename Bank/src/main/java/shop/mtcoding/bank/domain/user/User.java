package shop.mtcoding.bank.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor //스프링이 User 객체를 생성할떄 빈 생성자로 new를 하기 떄문
@Getter
@EntityListeners(AuditingEntityListener.class) //@CreatedDate, @LastModifiedDate 어노테이션을 사용하여 시간을 자동으로 설정하는 기능을 제공
@Table(name = "user_tb") //@Table 어노테이션은 이 엔티티가 매핑될 데이터베이스 테이블의 이름을 지정. 여기서는 user_tb라는 테이블에 매핑됩
@Entity //이 클래스가 JPA 엔티티임을 나타낸다. JPA 엔티티는 데이터베이스의 테이블과 매핑되는 객체이다.
public class User { //extends 시간 설정 (상속)
    @Id //이 필드는 엔티티의 기본 키(primary key)를 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본 키의 값을 데이터베이스에서 자동으로 생성하도록 지정 enerationType.IDENTITY는 데이터베이스가 자동으로 값을 증가시켜 생성하는 방식
    private long id;
    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 60) //패스워드 인코딩
    private String password;
    @Column(nullable = false, length = 20)
    private String email;
    @Column(nullable = false, length = 20)
    private String fullname;
    @Enumerated(EnumType.STRING) //@Enumerated(EnumType.STRING)는 role 필드가 UserEnum 열거형(Enum) 타입임을 나타냄
    @Column(nullable = false)
    private UserEnum role; //ADMIN, CUSTOMER
    @CreatedDate //Insert @CreatedDate와 @LastModifiedDate는 Spring Data JPA의 Auditing 기능을 사용하여 엔티티의 생성일자와 수정일자를 자동으로 관리
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate //Insert, Update //@CreatedDate와 @LastModifiedDate는 Spring Data JPA의 Auditing 기능을 사용하여 엔티티의 생성일자와 수정일자를 자동으로 관리
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder //@Builder는 빌더 패턴을 사용하여 객체를 생성할 수 있게 해줍니다. 이 패턴을 사용하면 객체를 생성할 때 가독성이 높고, 필드가 많을 경우 유용
    public User(long id, String username, String password, String email, String fullname, UserEnum role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
