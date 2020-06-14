package io.horrorshow.soulswap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class SOULSwapApplication {

    @RequestMapping("/")
    public String home() {
        return staticHello();
    }

    private String staticHello() {
        return "Hello from SOULSwap!";
    }

    public static void main(final String[] args) {
        ApplicationContext context = SpringApplication.run(SOULSwapApplication.class, args);

        System.out.println(context.getApplicationName() + "SOULSwap Application started!");
    }

}
