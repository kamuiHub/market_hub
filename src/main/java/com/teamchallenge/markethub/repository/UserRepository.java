package com.teamchallenge.markethub.repository;

import com.teamchallenge.markethub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);
    User findByPhone(String phone);
    Boolean existsUserByEmailOrPhone(String email, String phone);
}
