package hsrm.mi.game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpielerController {
    @GetMapping()
    public String getMethodName(){
        return "helloWorld";
    }
}