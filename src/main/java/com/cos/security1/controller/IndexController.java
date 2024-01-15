package com.cos.security1.controller;

import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("/test/login")
    @ResponseBody
    public String loginTest(Authentication authentication,
                            @AuthenticationPrincipal UserDetails userDetails){ // PrincipalDetails가 UserDetails를 상속 받았기 때문에 PrincipalDetails로 대체 가능
                                                                                //oauth는 CastClassException 발생 !
        System.out.println("/test/login =======================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUser());

        System.out.println("authentication : " + userDetails.getUsername());
        return "세션 정보 확인";
    }
    @GetMapping("/test/oauth/login")
    @ResponseBody
    public String oauthloginTest(Authentication authentication,
                                 @AuthenticationPrincipal OAuth2User oAuth ){
        System.out.println("/test/oauth/login =======================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oAuth2User.getAttributes());
        System.out.println("oAuthUser : " + oAuth.getAttributes());

        return "OAuth 세션 정보 확인";
    }


    @GetMapping({"","/"})
    public String index(){
        //머스테치 사용. (스프링 권장사항) (기본경로 : src/main/resource/)
        //뷰 리졸버 설정 : templates(prefix), .mustache (suffix)
        return "index"; // src/main/resources/template/index.mustache
    }
    @GetMapping("/user")
    @ResponseBody
    public String user(){
        return "user";
    }
    @GetMapping("/admin")
    @ResponseBody
    public String admin(){
        return "admin";
    }
    @GetMapping("/manager")
    @ResponseBody
    public String manager(){
        return "manager";
    }
    //Security Config 설정 후 로그인 페이지 안뜸.
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }
    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rwaPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rwaPassword);
        user.setPassword(encPassword);
        userRepository.save(user); //회원가입 잘되지만 패스워드 암호화 X
        return "redirect:/loginForm";
    }
    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    @ResponseBody
    public String info(){
        return "개인정보";
    }
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    @ResponseBody
    public String data(){
        return "데이터";
    }

}
