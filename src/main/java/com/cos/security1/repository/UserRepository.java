package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


//CRUD 함수를 JpaRepository가 들고 있음
//@Repository라는 어노테이션이 없어도 IOC 가능 ( JpaRepository를 상속 했기 때문)
public interface UserRepository extends JpaRepository<User,Integer>{

    // JPA / findBy(규칙) -> Username (문법)
    // select * from user where username = 1?
    User findByUsername(String username); // Jpa Query methods
}
