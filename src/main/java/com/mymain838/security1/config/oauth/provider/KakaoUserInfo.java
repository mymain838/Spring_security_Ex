package com.mymain838.security1.config.oauth.provider;


import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
    /*
            System.out.println(attributes);
                {id=아이디값,
                connected_at=2022-02-22T15:50:21Z,
                properties={nickname=이름},
                kakao_account={
                    profile_nickname_needs_agreement=false,
                    profile={nickname=이름},
                    has_email=true,
                    email_needs_agreement=false,
                    is_email_valid=true,
                    is_email_verified=true,
                    email=이메일}
                }
            */
    private Map<String, Object> attributes; //oauth2User.getAttributes()
    private Map<String, Object> attributesAccount;
    private Map<String, Object> attributesProfie;

    public KakaoUserInfo(Map<String, Object> attributes){
        this.attributes = attributes;
        this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.attributesProfie = (Map<String, Object>) attributesAccount.get("profile");
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {

        return attributesAccount.get("email").toString();
    }

    @Override
    public String getName() {

        return attributesProfie.get("nickname").toString();
    }
}
