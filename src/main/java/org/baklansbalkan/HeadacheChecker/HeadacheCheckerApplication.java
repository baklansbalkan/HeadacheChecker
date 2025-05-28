package org.baklansbalkan.HeadacheChecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class HeadacheCheckerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeadacheCheckerApplication.class, args);
    }
}
