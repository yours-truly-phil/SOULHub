package io.horrorshow.soulswap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootApplication
@EnableJpaAuditing
public class SOULSwapApplication {

    @PersistenceContext
    private EntityManager entityManager;


    public static void main(final String[] args) {
        ApplicationContext context = SpringApplication.run(SOULSwapApplication.class, args);

        System.out.println(context.getApplicationName() + "SOULSwap Application started!");
    }

}
