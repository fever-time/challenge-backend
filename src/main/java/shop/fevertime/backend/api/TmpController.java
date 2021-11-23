package shop.fevertime.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.fevertime.backend.domain.*;
import shop.fevertime.backend.repository.CategoryRepository;
import shop.fevertime.backend.repository.ChallengeRepository;
import shop.fevertime.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TmpController {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ChallengeRepository challengeRepository;

    @GetMapping("/login")
    public String login() {
        return "index";
    }

    @GetMapping("/test")
    public String test() {

        User user = new User("test", "test@email.com", UserRole.USER, 123456L);
        userRepository.save(user);

        Category category1 = new Category("공부");
        Category category2 = new Category("운동");
        Category category3 = new Category("취미");
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);

        Challenge challenge = new Challenge("test", "test", "test", LocalDateTime.now(), LocalDateTime.now(), 0, true, user, categories);
        challengeRepository.save(challenge);

        return "ok";
    }

    @GetMapping("/get")
    public String getTest() {
        Challenge challenge = challengeRepository.findByTitle("test").orElseGet(null);

        List<ChallengeCategory> challengeCategories = challenge.getChallengeCategories();
        for (ChallengeCategory challengeCategory : challengeCategories) {
            Category category = challengeCategory.getCategory();
            System.out.println(category.getName());
        }

        return "ok";
    }

}
