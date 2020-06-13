package io.horrorshow.soulswap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SOULSwapApplication {

    public static void main(final String[] args) {
        ApplicationContext context = SpringApplication.run(SOULSwapApplication.class, args);

        System.out.println(context.getApplicationName() + "SOULSwap Application started!");
    }

}
