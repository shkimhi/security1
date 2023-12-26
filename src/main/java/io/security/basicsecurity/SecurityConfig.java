package io.security.basicsecurity;

import io.security.basicsecurity.Model.ERole;
import io.security.basicsecurity.Service.CustomDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private CustomDetailService customDetailService;
    @Autowired
    public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, CustomDetailService customDetailService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customDetailService = customDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customDetailService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/images/**", "lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //인가 정책
                .authorizeRequests()
                .antMatchers("/", "/login", "/registration", "/h2/**").permitAll()
                .antMatchers("/home/admin").hasAuthority(ERole.ADMIN.getValue())
                .antMatchers("/home/user").hasAuthority(ERole.MANAGER.getValue())
                .antMatchers("/home/guest").hasAuthority(ERole.GUEST.getValue())
                .anyRequest().authenticated(); // 어떠한 요청에도 인증을 받도록
        http
                .csrf()
                    .disable()
                .headers()
                    .frameOptions().disable()
                    .and()
                //인증정책
                .formLogin() // form 방식으로 로그인
                    //.loginPage("/loginPage") // 누구나 이 경로로는 이동 할 수 있어야함.
                    .defaultSuccessUrl("/")
                    .failureUrl("/login")
                    .usernameParameter("userId")
                    .passwordParameter("passwd")
                    .loginProcessingUrl("/login_proc")
                    .successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            System.out.println("authentication : " + authentication.getName());
                            response.sendRedirect("/");
                        }
                    })
                    .failureHandler(new AuthenticationFailureHandler() {
                        @Override
                        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                            System.out.println("exception : " + exception.getMessage());
                            response.sendRedirect("/loginPage");
                        }
                    })
                    .permitAll()
        ;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}