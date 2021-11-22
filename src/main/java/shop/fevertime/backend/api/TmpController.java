package shop.fevertime.backend.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TmpController {

    @GetMapping("/login")
    public String login() {
        return "index";
    }

}
