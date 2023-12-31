package com.teamchallenge.markethub.service;

import com.teamchallenge.markethub.exception.UserNotFoundException;
import com.teamchallenge.markethub.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);
    User update(User user);
    void remove(Integer id);
    User findByEmail(String email);
    User findByPhone(String phone);
    User findById(Integer id) throws UserNotFoundException;
    List<User> findAll();
    List<User> findAllByRole();

}
