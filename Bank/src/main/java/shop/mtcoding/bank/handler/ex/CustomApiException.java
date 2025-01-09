package shop.mtcoding.bank.handler.ex;

import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException {
    private final int statusCode;

    public CustomApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    // statusCode를 반환하는 getter 메서드 추가
    public int getStatusCode() {
        return statusCode;
    }
}
