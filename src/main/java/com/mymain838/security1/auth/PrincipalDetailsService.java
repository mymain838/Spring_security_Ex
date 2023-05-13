package com.mymain838.security1.auth;

import com.mymain838.security1.model.User;
import com.mymain838.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 Ioc
//되있는 loadUserByUsername 함수가 실행

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null){
            return new PrincipalDetails(userEntity, null);
        }
        return null;
    }
}
