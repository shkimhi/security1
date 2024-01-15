package com.cos.security1.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserServer extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("ClientRegistration : " + userRequest.getClientRegistration()); //ClientRegistration
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue()); // 인증 token
        //구글 로그인 클릭 -> 구글로그인 -> code 리턴 ( Oauth-Client라이브러리) -> AccessToken 요청
        //userRequest정보 -> loadUser함수 호출 -> 구글로부터 회원 프로필 획득
        System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes()); //ClientRegistration

        OAuth2User oAuth2User = super.loadUser(userRequest);

        return super.loadUser(userRequest);
    }
}
