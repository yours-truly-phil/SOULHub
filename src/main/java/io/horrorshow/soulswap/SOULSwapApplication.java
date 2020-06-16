package io.horrorshow.soulswap;

import io.horrorshow.soulswap.data.SOULSwapRepository;
import io.horrorshow.soulswap.data.SOULPatch;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
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

//    @Bean
//    ApplicationRunner init(SOULSwapRepository repository) {
//
//        return args -> {
//            SOULPatch patch = new SOULPatch();
//            patch.setName("name1");
//            patch.setDescription("description1");
//            patch.setSoulFileName("soulfile name.soul");
//            patch.setSoulFileContent("soulfile content");
//            patch.setSoulpatchFileName("soulpatchfile name.soulpatch");
//            patch.setSoulpatchFileContent("soulpatch file content");
//            patch.setAuthor("author");
//            patch.setNoServings(10L);
//            repository.save(patch);
//            repository.findAll().forEach(System.out::println);
//        };
//    }

}
