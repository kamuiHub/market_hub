package com.teamchallenge.markethub.dto.user;

import com.teamchallenge.markethub.dto.authorization.AuthorizationRequest;
import com.teamchallenge.markethub.model.User;
import com.teamchallenge.markethub.model.role.Role.java.Role;

import java.time.LocalDateTime;

public record UserResponse(String firstname, String lastname, String email, String phone, LocalDateTime registrationDate, String role) {

    public static UserResponse convertToUserResponse(User user) {
        return new UserResponse(user.getFirstname(),
                user.getLastname(), user.getEmail(),user.getPhone(), user.getRegistrationDate(),user.getRole());
    }

    public static User convertToNewSeller(AuthorizationRequest authorizationRequest) {
        User seller = new User();
        seller.setFirstname(authorizationRequest.firstname());
        seller.setLastname(authorizationRequest.lastname());
        seller.setEmail(authorizationRequest.email());
        seller.setPhone(authorizationRequest.phone());
        seller.setPassword(authorizationRequest.password());
        seller.setRole(Role.SELLER.name());
        return seller;
    }
}
