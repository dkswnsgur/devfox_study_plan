package shop.mtcoding.bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.transaction.TransactionRepository;
import shop.mtcoding.bank.domain.transaction.Transction;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.account.AccountReqDto;
import shop.mtcoding.bank.dto.account.AccountRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends DummyObject {

    @InjectMocks //모든 Mock들이 InjectMocks로 주입됨
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Spy //진짜 객체를 InjectMocks 에 주입한다
    private ObjectMapper om;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void 계좌등록_test() throws Exception {
        //given
        Long userId = 1L;

        AccountReqDto.AccountSaveReqDto accountSaveReqDto = new AccountReqDto.AccountSaveReqDto();
        accountSaveReqDto.setNumber(1111L);
        accountSaveReqDto.setPassword(1234L);

        //stub 1
        User ssar = newMockUser(userId, "ssar", "쌀");
        when(userRepository.findById(any())).thenReturn(Optional.of(ssar));

        //stub 2
        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());

        //stub 3
        Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
        when(accountRepository.save(any())).thenReturn(ssarAccount);

        //when
        AccountRespDto.AccountTransferRespDto.AccountWithdrawRespDto.AccountDepositRespDto.AccountSaveRespDto accountSaveRespDto = accountService.계좌등록(accountSaveReqDto, userId);
        String responseBody = om.writeValueAsString(accountSaveRespDto);
        System.out.println("테스트 :"+ responseBody);

        //then
        assertThat(accountSaveRespDto.getNumber()).isEqualTo(1111L);
    }

    @Test
    public void 계좌삭제_test() throws Exception {
        //given
        Long number = 1111L;
        Long userId = 2L;

        //stub
        User ssar = newMockUser(1L, "ssar", "쌀");
        Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount));

        //when
        try {
            accountService.계좌삭제(number, userId);
        }catch (Exception e) {
            return;
        }

        //then
        assertThrows(CustomApiException.class, () -> accountService.계좌삭제(number, userId));
    }

    // Account -> balance 변경됐는지
    // Trasction -> balance 잘 기록됐는지
    @Test
    public void 계좌입금_test() throws Exception { //이게 정석임 given 하고 stub하고 마지막으로 하는거 stub은 가짜로 만드는것
        // given
        AccountReqDto.AccountDepositReqDto accountDepositReqDto = new AccountReqDto.AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("01088887777");

        // stub 1L
        User ssar = newMockUser(1L, "ssar", "쌀"); // 실행됨
        Account ssarAccount1 = newMockAccount(1L, 1111L, 1000L, ssar); // 실행됨 - ssarAccount1 -> 1000원
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount1)); // 실행안됨 -> service호출후 실행됨 ->
        // 1100원

        // stub 2 (스텁이 진행될 때 마다 연관된 객체는 새로 만들어서 주입하기 - 타이밍 때문에 꼬인다)
        Account ssarAccount2 = newMockAccount(1L, 1111L, 1000L, ssar); // 실행됨 - ssarAccount1 -> 1000원
        Transction transaction = newMockDepositTransaction(1L, ssarAccount2);
        when(transactionRepository.save(any())).thenReturn(transaction); // 실행안됨

        // when
        AccountRespDto.AccountTransferRespDto.AccountWithdrawRespDto.AccountDepositRespDto accountDepositRespDto = accountService.계좌입금(accountDepositReqDto);
        System.out.println("테스트 : 트랜잭션 입금계좌 잔액 : " + accountDepositRespDto.getTransaction().getDepositAccountBalance());
        System.out.println("테스트 : 계좌쪽 잔액 : " + ssarAccount1.getBalance());
        System.out.println("테스트 : 계좌쪽 잔액 : " + ssarAccount2.getBalance());

        // then
        assertThat(ssarAccount1.getBalance()).isEqualTo(1100L);
        assertThat(accountDepositRespDto.getTransaction().getDepositAccountBalance()).isEqualTo(1100L);
    }

    @Test
    public void 계좌입금_test2() throws Exception {
        //given
        AccountReqDto.AccountDepositReqDto accountDepositReqDto = new AccountReqDto.AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("01088887777");

        //stub1
        User ssar = newMockUser(1L, "ssar", "쌀"); // 실행됨
        Account ssarAccount1 = newMockAccount(1L, 1111L, 1000L, ssar); // 실행됨 - ssarAccount1 -> 1000원
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount1)); // 실행안됨 -> service호출후 실행됨 ->
        // 1100원

        //stub2
        User ssar2 = newMockUser(1L, "ssar", "쌀"); // 실행됨
        Account ssarAccount2 = newMockAccount(1L, 1111L, 1000L, ssar2); // 실행됨 - ssarAccount1 -> 1000원
        Transction transaction = newMockDepositTransaction(1L, ssarAccount2);
        when(transactionRepository.save(any())).thenReturn(transaction); // 실행안됨

        //when
        AccountRespDto.AccountTransferRespDto.AccountWithdrawRespDto.AccountDepositRespDto accountDepositRespDto = accountService.계좌입금(accountDepositReqDto);
        String responseBody = om.writeValueAsString(accountDepositRespDto);
        System.out.println("테스트 : " + responseBody);

        //then
        assertThat(ssarAccount1.getBalance()).isEqualTo(1100L);
    }

    //이게 가장간단
    @Test
    public void 계좌입금_test3() throws Exception {
        // given
        Account account = newMockAccount(1L,1111L,1000L,null);
        Long amount = 100L;

        // when
        if(amount <= 0L) {
           throw new CustomApiException("0원 이하의 금액을 입금할수 없습니다");
        }
        account.deposit(100L);

        //then
        assertThat(account.getBalance()).isEqualTo(1100L);
    }

    //계좌 출금_테스트(서비스)
    @Test
    public void 계좌출금_test() throws Exception {
        //given
        Long amount = 100L;
        Long password = 1234L;
        Long userId = 1L;

        User ssar = newMockUser(1L,"ssar","쌀");
        Account ssarAccount = newMockAccount(1L,1111L,1000L, ssar);

        //when and there
        if(amount <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할수 없습니다");
        }

        ssarAccount.checkOwner(1L);
        ssarAccount.checkSamePassword(1234L);
        ssarAccount.checkBalance(amount);
        ssarAccount.withdraw(amount);

        //then
        assertThat(ssarAccount.getBalance()).isEqualTo(900L);
    }

    // 계좌 이체_테스트 (서비스)
    @Test
    public void 계좌이체_test() throws Exception {
        //given
        Long userId = 1L;
        AccountReqDto.AccountTransferReqDto accountTransferReqDto = new AccountReqDto.AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(2222L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setGubun("TRANSFER");

        User ssar = newMockUser(1L, "ssar", "쌀");
        User cos = newMockUser(2L, "cos", "코스");
        Account withdrawAccount = newMockAccount(1L, 1111L, 1000L, ssar);
        Account depositAccount = newMockAccount(2L, 2222L, 1000L, cos);

        //when
        //출금 계좌와 입금 계좌가 동일하면 안됨
        if(accountTransferReqDto.getWithdrawNumber().longValue() == accountTransferReqDto.getDepositNumber().longValue()) {
            throw new CustomApiException("입출금 계좌가 동일할수는 없습니다");
        }

        // 0원 체크
        if(accountTransferReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할수 없습니다");
        }

        //출금 소유자 확인 (로그인한 사람과 동일한지)
        withdrawAccount.checkOwner(userId);

        //출금 계좌 비밀번호 확인
        withdrawAccount.checkSamePassword(accountTransferReqDto.getWithdrawPassword());

        //출금 계좌 잔액확인
        withdrawAccount.checkBalance(accountTransferReqDto.getAmount());

        //이체하기
        withdrawAccount.withdraw(accountTransferReqDto.getAmount());
        depositAccount.deposit(accountTransferReqDto.getAmount());

        //when

    }





}
