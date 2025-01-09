package shop.mtcoding.bank.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.mtcoding.bank.handler.ex.CustomApiException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<ErrorResponse> apiException(CustomApiException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getStatusCode());
        return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
    }
}
