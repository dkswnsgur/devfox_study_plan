package shop.mtcoding.bank.handler.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import shop.mtcoding.bank.handler.ex.CustomValidationExeption;

import java.util.HashMap;
import java.util.Map;

@Component //@Component: 이 클래스가 Spring의 관리 대상 Bean
@Aspect //@Aspect: 이 클래스가 AOP(Aspect-Oriented Programming) 기능을 수행하는 Aspect임을 명시
public class CustomValidationAdvice { //Spring AOP를 사용하여 PostMapping과 PutMapping 요청에서 발생하는 유효성 검사 오류를 처리하는 CustomValidationAdvice 클래스

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)") //@Pointcut: 특정 조건에 따라 AOP가 적용될 지점
    public void postMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping() {
    }

    @Around("postMapping() || putMapping()") // @Around: 지정된 Pointcut 전후로 코드를 실행
    public Object validationAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable { //proceedingJoinPoint: 실제 메서드 호출에 접근할 수 있는 객체
        Object[] args = proceedingJoinPoint.getArgs(); //proceedingJoinPoint.getArgs()를 통해 메서드의 매개변수를 확인
        for (Object arg : args) {
            if (arg instanceof BindingResult) { //BindingResult 타입의 객체가 있으면 이를 검사
                BindingResult bindingResult = (BindingResult) arg;

                if (bindingResult.hasErrors()) { //bindingResult.hasErrors() 가 참이면 유효성 검사 오류
                    Map<String, String> errorMap = new HashMap<>();

                    for (FieldError error : bindingResult.getFieldErrors()) { //FieldError 객체를 통해 각 필드의 오류 메시지를 추출하고 errorMap에 담음
                        errorMap.put(error.getField(), error.getDefaultMessage()); //HTTP 400 상태와 함께 오류 메시지를 클라이언트로 반환
                    }
                    /*throw new CustomValidationExeption("유효성 검사", errorMap);*/
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap); //HTTP 400 상태와 함께 오류 메시지를 클라이언트로 반환
                }
            }
        }
        return proceedingJoinPoint.proceed(); //정상적으로 해당 메세지를 실행해라
    }
}

/*
  get, delete, post(body), put(body)
 */
