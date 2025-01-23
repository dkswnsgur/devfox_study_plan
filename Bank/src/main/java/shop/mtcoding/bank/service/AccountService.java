package shop.mtcoding.bank.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.account.AccountReqDto;
import shop.mtcoding.bank.dto.account.AccountRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

  private final UserRepository userRepository;
  private final AccountRepository accountRepository;

    public AccountRespDto.AccountSaveRespDto.AccountListRespDto 계좌목록보기_유저별(Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new CustomApiException("유저를 찾을 수 없습니다"));

        // 유저의 모든 계좌목록
        List<Account> accountListPS = accountRepository.findByUser_id(userId);
        return new AccountRespDto.AccountSaveRespDto.AccountListRespDto(userPS, accountListPS);
    }


    @Transactional
    public AccountRespDto.AccountSaveRespDto 계좌등록(AccountReqDto.AccountSaveReqDto accountSaveReqDto, Long userId) {
     //User DB에 있는지 검증 겸 유저 엔티티 가져오기
        User userPs = userRepository.findById(userId).orElseThrow(
                ()-> new CustomApiException("유저를 찾을수 없습니다")
        );

     //해당 계좌가 DB에 있는 중복여부를 체크
        Optional<Account> accountOP = accountRepository.findByNumber(accountSaveReqDto.getNumber());
        if(accountOP.isPresent()) {
            throw new CustomApiException("해당 계좌가 이미 존재합니다");
        }

     //계좌 등록
        Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPs));

     //DTO를 응답
        return new AccountRespDto.AccountSaveRespDto(accountPS);
  }



}
