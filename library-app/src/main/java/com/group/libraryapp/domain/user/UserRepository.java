package com.group.libraryapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);

    //boolean existsByName(String name);

    //jpa 쿼리만들고 싶을때 검색해봄

}
