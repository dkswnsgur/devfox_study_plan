package shop.mtcoding.bank.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.dto.account.AccountReqDto;
import shop.mtcoding.bank.dto.account.AccountRespDto;
import shop.mtcoding.bank.handler.ex.CustomForbiddenException;
import shop.mtcoding.bank.service.AccountService;

import javax.validation.Valid;

@RequiredArgsConstructor//Lombok 애노테이션으로, final 또는 @NonNull로 선언된 모든 필드를 매개변수로 하는 생성자를 자동으로 생성
@RequestMapping("/api")
@RestController //자동으로 json형식으로 바꿔줌
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/s/account")
    public ResponseEntity<?> saveAccount (@RequestBody @Valid AccountReqDto.AccountSaveReqDto accountSaveReqDto,
                                          BindingResult bindingResult, @AuthenticationPrincipal LoginUser loginUser){
       AccountRespDto.AccountSaveRespDto accountSaveRespDto = accountService.계좌등록(accountSaveReqDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 등록 성공", accountSaveRespDto), HttpStatus.CREATED);
    }

    //인증이 필요하고, account 테이블 1번 row 해주세요
    //cos 로 로그인을 했는데 cos의 id가 2번이에요
    //권한처리 떄문에 선호하지 않는다

    //인증이 필요하고, account 테이블 데이터 다 주세요 !!!

    //인증이 필요하고 account 테이블에 login 한 유저의 계좌만 주세요
    @GetMapping("/s/account/login-user")
    public ResponseEntity<?> findUserAccount(@AuthenticationPrincipal LoginUser loginUser) {

        AccountRespDto.AccountListRespDto accountListRespDto = accountService.계좌목록보기_유저별(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1,"계좌목록_유저별 성공",accountListRespDto), HttpStatus.OK);
    }

    @DeleteMapping("/s/account/{number}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long number, @AuthenticationPrincipal LoginUser loginUser) {
        accountService.계좌삭제(number, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1,"계좌 삭제 완료",null), HttpStatus.OK);
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<?> depositAccount(@RequestBody @Valid AccountReqDto.AccountDepositReqDto accountDepositReqDto, BindingResult bindingResult) {
        AccountRespDto.AccountDepositRespDto accountDepositRespDto = accountService.계좌입금(accountDepositReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1,"계좌 입금 완료", accountDepositRespDto), HttpStatus.OK);
    }

    @PostMapping("/s/account/withdraw")
    public ResponseEntity<?> withdrawAccount(@RequestBody @Valid AccountReqDto.AccountWithdrawReqDto accountWithdrawReqDto,
                                             BindingResult bindingResult,
                                             @AuthenticationPrincipal LoginUser loginuser) {
        AccountRespDto.AccountWithdrawRespDto accountWithdrawRespDto = accountService.계좌출금(accountWithdrawReqDto,loginuser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1,"계좌 출금 완료", accountWithdrawRespDto), HttpStatus.OK);
    }

    @PostMapping("/s/account/transfer")
    public ResponseEntity<?> transferAccount(@RequestBody @Valid AccountReqDto.AccountTransferReqDto accountTransferReqDto,
                                             BindingResult bindingResult,
                                             @AuthenticationPrincipal LoginUser loginuser) {
        AccountRespDto.AccountTransferRespDto accountTransferRespDto = accountService.계좌이체(accountTransferReqDto,loginuser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1,"계좌 이체 완료", accountTransferRespDto), HttpStatus.CREATED);
    }

    @GetMapping("/s/account/{number}")
    public ResponseEntity<?> findDetailAccount(@PathVariable Long number,
                                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                                               @AuthenticationPrincipal LoginUser loginUser) {
        AccountRespDto.AccountDetailRespDto accountDetailRespDto = accountService.계좌상세보기(number, loginUser.getUser().getId(),
                page);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌상세보기 성공", accountDetailRespDto), HttpStatus.OK);
    }


}
