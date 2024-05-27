package app.controller;

import app.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class MenuController {

    @GetMapping("/")
    public RedirectView helloWorld(){
        return new RedirectView("/html/menu.html");
    }
    @GetMapping("/admin")
    public RedirectView admin(){
        return new RedirectView("/html/management.html");
    }

}