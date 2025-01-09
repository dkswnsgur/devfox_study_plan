package shop.mtcoding.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.swing.*;

@EnableJpaAuditing
@SpringBootApplication //@CreatedDate, @LastModifiedDate와 같은 JPA 감사 어노테이션이 자동으로 작동하여 엔티티의 생성일자나 수정일자를 자동으로 관리할 수 있습니다.
public class BankAapplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BankAapplication.class, args);
       /* String[] iocNames = context.getBeanDefinitionNames();
        for(String name : iocNames) {
            System.out.println(name);
        }*/
        /*SpringApplication.run(BankAapplication.class, args);*/
    }
}