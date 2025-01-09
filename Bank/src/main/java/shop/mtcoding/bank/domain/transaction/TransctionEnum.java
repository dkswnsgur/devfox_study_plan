package shop.mtcoding.bank.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransctionEnum {
   WITHDRAW("출금"), DEPOSIT("입금"), TRANSFER("이체"), ALL("입출금 내역");

   private String value;
}
