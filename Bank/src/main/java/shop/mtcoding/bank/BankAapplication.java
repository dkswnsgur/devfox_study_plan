package shop.mtcoding.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BankAapplication{

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BankAapplication.class, args);
        // String[] iocNames = context.getBeanDefinitionNames();
        // for (String name : iocNames) {
        // System.out.println(name);
        // }
    }

}