package com.mymain838.security1.config.oauth;

import com.mymain838.security1.auth.PrincipalDetails;
import com.mymain838.security1.config.oauth.provider.FacebookUserInfo;
import com.mymain838.security1.config.oauth.provider.GoogleUserInfo;
import com.mymain838.security1.config.oauth.provider.NaverUserInfo;
import com.mymain838.security1.config.oauth.provider.OAuth2UserInfo;
import com.mymain838.security1.model.User;
import com.mymain838.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    //구글로 부터 받은 userRequest 데이터에 대한 후처리를 담당하는 메서드 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println(userRequest.getClientRegistration());
        System.out.println(userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        //구글로그인 버튼 클릭 -> 구글로그인 창-> 로그인을 완료 -> code 를 리턴(Auth-Client라이브러리) -> Access Token 요청
        //user Request 정보 -> loadUser함수 호출 -> 회원프로필 받아준다.
        System.out.println(oAuth2User.getAttributes());
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인");
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        } else {
            System.out.println("다른 외부로그인은 지원하지 않습니다.");
        }


        String provider = oAuth2UserInfo.getProvider(); //google
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId; //google_~~
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            System.out.println("신규 회원");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .Role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("기존 회원");

        }


        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
