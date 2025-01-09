package shop.mtcoding.bank.domain.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import shop.mtcoding.bank.domain.account.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor //스프링이 User 객체를 생성할떄 빈 생성자로 new를 하기 떄문
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transaction_tb")
@Entity

public class Transction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Many: Transaction -> Account (외래키)
    @JoinColumn(name = "iwitdraw_account_id", referencedColumnName = "id") // 외래키 컬럼명 지정
    private Account iwitdrawAccount;

    @ManyToOne(fetch = FetchType.LAZY) // Many: Transaction -> Account (외래키)
    @JoinColumn(name = "deposit_account_id", referencedColumnName = "id") // 외래키 컬럼명 지정
    private Account depositAccount;

    private Long amount;

    private Long withdrawAccountBalance; // 11계좌 -> 1000원 --> 500원 --> 200원
    private long depositAccountBalance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransctionEnum gubun; //WITHDRAW, DEPOSIT, TRANSFER, ALL

    //계좌가 사라져도 로그는 남아야 한다
    private String sender;
    private String receiver;
    private String tel;

    @CreatedDate //Insert
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate //Insert, Update
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Transction(Long id, Account iwitdrawAccount, Account depositAccount, Long amount, Long withdrawAccountBalance, long depositAccountBalance, TransctionEnum gubun, String sender, String receiver, String tel, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.iwitdrawAccount = iwitdrawAccount;
        this.depositAccount = depositAccount;
        this.amount = amount;
        this.withdrawAccountBalance = withdrawAccountBalance;
        this.depositAccountBalance = depositAccountBalance;
        this.gubun = gubun;
        this.sender = sender;
        this.receiver = receiver;
        this.tel = tel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
