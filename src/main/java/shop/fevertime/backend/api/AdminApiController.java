package shop.fevertime.backend.api;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminApiController {

    @GetMapping("/admin")
    @Secured("ROLE_ADMIN")
    public String admin() {
        return "ok";
    }

}
