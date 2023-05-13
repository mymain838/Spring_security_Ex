package com.mymain838.security1.auth;

//Security ContextHolder 에 시큐리티 session 을 저장
//오브젝트 => Authentication 타입 객체
//User 오브젝트 타입 = > User Deails 타입 객체

import com.mymain838.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//Security Session => Authentication => UserDeatails(Prin)
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // 콤퍼지션

    private Map<String, Object> attributes;

    public PrincipalDetails(User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    //해당 User의 권한을 리턴하는 곳!!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }
    @Override
    public String getName() {
        return null;
    }
}
