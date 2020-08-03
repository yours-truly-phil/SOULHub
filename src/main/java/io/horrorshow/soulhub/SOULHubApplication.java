package io.horrorshow.soulhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class SOULHubApplication {

    public static void main(final String[] args) {
        ApplicationContext context = SpringApplication.run(SOULHubApplication.class, args);

        System.out.println(context.getApplicationName() + "SOULHub Application started!");
    }

}
