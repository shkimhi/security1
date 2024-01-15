package com.cos.security1.config;


import com.cos.security1.oauth.PrincipalOauth2UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize(postAuthorize도) 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserServer principalOauth2UserServer;

    //해당 메서드의 리턴되는 오브젝트르 IoC로 등록 해줌 (Bean 어노테이션)
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행 함.
                .defaultSuccessUrl("/") // 로그인 성공 시 / 이동
                .and()
                .oauth2Login()
                .loginPage("/loginForm") // 구글 로그인 완료 된 후 처리가 필요함.
                                        // (1. 코드받기(인증완료) 2. 엑세스토큰(권한 생성) 3. 사용자프로필 정보 가져옴 4-1. 사용자프로필 토대로 회원가입 자동 진행
                                        //                                                                   4-2. 추가 정보가 필요할 경우 추가 회원가입
                                        // Tip. 코드 X (엑세스토큰 + 사용자프로필 정보O)
                .userInfoEndpoint()
                .userService(principalOauth2UserServer);
    }
}
