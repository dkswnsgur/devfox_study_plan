package shop.mtcoding.bank.handler.ex;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomValidationExeption extends RuntimeException  {
    private Map<String, String> errorMap;

    public CustomValidationExeption(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }
}
