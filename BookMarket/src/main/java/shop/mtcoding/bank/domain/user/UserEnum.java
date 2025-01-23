package shop.mtcoding.bank.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor //모든 필드를 매개변수로 받는 생성자를 자동으로 생성
@Getter
public enum UserEnum { //UserEnum.ADMIN은 관리자라는 값을 가지고, UserEnum.CUSTOMER는 고객이라는 값을 가집
    ADMIN("관리자"), CUSTOMER("고객");

    private String value;
}
