package io.horrorshow.soulswap;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SOULSwapController {

    @RequestMapping("/")
    public String index() {
        return "This is SOULSwap!";
    }
}
