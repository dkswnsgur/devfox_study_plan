package shop.mtcoding.bank.dto.account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.transaction.Transction;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.service.AccountService;
import shop.mtcoding.bank.util.CustomDateUtil;

public class AccountRespDto {

    @Setter
    @Getter
    public static class AccountDepositRespDto {
        private Long id; //계좌 id
        private Long number; //계좌 번호
        private AccountDepositRespDto.TransactionDto transaction; //계좌 남겼다는 증거

        public AccountDepositRespDto(Account account, Transction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.transaction = new AccountDepositRespDto.TransactionDto(transaction);
        }

        @Setter
        @Getter
        public class TransactionDto {
            private Long id;
            private String gubun;
            private String sender;
            private String reciver;
            private Long amount;
            @JsonIgnore
            private Long depositAccountBalance; //클라이언트에게 전달 x -> 서비스단에서 테스트 용도
            private String tel;
            private String createdAt;

            public TransactionDto(Transction transction) {
                this.id = transction.getId();
                this.gubun = transction.getGubun().getValue();
                this.amount = transction.getAmount();
                this.sender = transction.getSender();
                this.reciver = transction.getReceiver();
                this.createdAt = CustomDateUtil.toStringFormat(transction.getCreatedAt());
            }

        }

        @Getter
        @Setter
        public static class AccountSaveRespDto {
            private Long id;
            private Long number;
            private Long balance;

            public AccountSaveRespDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }

            @Setter
            @Getter
            public static class AccountListRespDto {
                private String fullname;
                private List<AccountDto> accounts = new ArrayList<>();

                public AccountListRespDto(User user, List<Account> accounts) {
                    this.fullname = user.getFullname();
                    this.accounts = accounts.stream().map(AccountDto::new).collect(Collectors.toList());
                    // [account, account]
                }

                @Setter
                @Getter
                public class AccountDto {
                    private Long id;
                    private Long number;
                    private Long balance;

                    public AccountDto(Account account) {
                        this.id = account.getId();
                        this.number = account.getNumber();
                        this.balance = account.getBalance();
                    }
                }
            }
        }
    }
}