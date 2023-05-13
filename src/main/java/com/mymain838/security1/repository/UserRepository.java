package com.mymain838.security1.repository;

import com.mymain838.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 생략가능 JpaRepository를 상속했기때문에 자동 빈등록
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByUsername(String username);
}
