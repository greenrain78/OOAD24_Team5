package app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
@Slf4j
@RestController
public class MenuController {

    @GetMapping("/")
    public RedirectView helloWorld(){
        log.info("redirect to /html/menu.html");
        return new RedirectView("/html/menu.html");
    }
    @GetMapping("/management")
    public RedirectView admin(){
        log.info("redirect to /html/management.html");
        return new RedirectView("/html/management.html");
    }
    @GetMapping("/management/codes")
    public RedirectView adminCodes(){
        log.info("redirect to /html/codes.html");
        return new RedirectView("/monitor/codes.html");
    }
    @GetMapping("/others")
    public RedirectView adminAll(){
        log.info("redirect to /html/all.html");
        return new RedirectView("/monitor/all.html");
    }
}