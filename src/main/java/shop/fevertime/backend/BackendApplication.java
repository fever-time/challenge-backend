package shop.fevertime.backend;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BackendApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:aws.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(BackendApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
//        SpringApplication.run(BackendApplication.class, args);
    }

//    @Bean
//    public AuditorAware<String> auditorProvider() {
//        return () -> Optional.of(UUID.randomUUID().toString());
//    }

}
