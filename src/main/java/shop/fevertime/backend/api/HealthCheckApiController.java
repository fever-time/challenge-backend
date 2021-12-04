package shop.fevertime.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckApiController {

    @GetMapping("/")
    public String health() {
        return "ok";
    }
}
