package com.mymain838.security1.controller;

import com.mymain838.security1.auth.PrincipalDetails;
import com.mymain838.security1.model.User;
import com.mymain838.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){
        System.out.println("/test/login ==========");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
       // 두 방법 모두 같음
        System.out.println("authentication: " + principalDetails.getUser().getUsername());


        System.out.println("userD: " + userDetails.getUser().getUsername());
        return "세션 정보 확인하기";
    }
    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth){
        System.out.println("/test/login ==========");
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        // 두 방법 모두 같음
        System.out.println("authentication객체접근: " + oAuth2User.getAttributes());


        System.out.println("oauth객체접근:  " + oAuth2User.getAttributes());




        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping({"/", ""})
    public String index() {

        return "index";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {

        return "admin";

    }
    //OAuth 로그인 해도 PrincipalDetails
    //일반 로그인을 해도 PrincipalDetails
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails: "+ principalDetails.getUser().toString());
        return "user";

    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {

        return "manager";

    }

    @GetMapping("/loginForm")
    public String login() {

        return "loginForm";

    }

    @GetMapping("/joinForm")
    public String joinForm() {

        return "joinForm";

    }

    @PostMapping("/join")
    public String join(User user) {

        String encPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encPassword);
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "redirect:/loginForm";

    }

    @Secured("ROLE_MANAGER")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }
    @PreAuthorize("hasRole('ROLE_USER')or hasRole('ROLE_MAMAGER')")
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이타";
    }
    @GetMapping("/fail")
    public @ResponseBody String fail(){
        return "ㅈ됌";
    }
}
