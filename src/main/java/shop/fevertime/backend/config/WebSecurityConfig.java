package shop.fevertime.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shop.fevertime.backend.security.AdminAccessDeniedHandler;
import shop.fevertime.backend.security.jwt.JwtAuthenticationEntryPoint;
import shop.fevertime.backend.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // 컨트롤러에서 인가가 필요한 API에 @Secured 사용가능
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtRequestFilter;

    private final AdminAccessDeniedHandler adminAccessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**/*").permitAll()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/error-test").permitAll()
                .antMatchers(HttpMethod.GET, "/docs/**").permitAll()
                .antMatchers(HttpMethod.GET, "/challenges/{challengeId}/user").authenticated()
                .antMatchers(HttpMethod.GET, "/challenges/**").permitAll()
                .antMatchers(HttpMethod.GET, "/feeds/**").permitAll()
                .antMatchers(HttpMethod.GET, "/search/**").permitAll()
                .antMatchers(HttpMethod.GET, "/sub/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/login/kakao").permitAll()
                .antMatchers("/websocket/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().accessDeniedHandler(adminAccessDeniedHandler);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
