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

    /**
     * 에러 발생시 알람 테스트
     */
    @GetMapping("/error-test")
    public String getArticles(){
        int i = 1/0;
        return "response";
    }

}
