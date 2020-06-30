package io.horrorshow.soulhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableJpaAuditing
public class SOULHubApplication {

    @PersistenceContext
    private EntityManager entityManager;


    public static void main(final String[] args) {
        ApplicationContext context = SpringApplication.run(SOULHubApplication.class, args);

        System.out.println(context.getApplicationName() + "SOULHub Application started!");
    }

}
