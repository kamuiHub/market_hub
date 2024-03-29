package com.teamchallenge.markethub.controller;

import com.teamchallenge.markethub.config.jwt.JwtUtils;
import com.teamchallenge.markethub.dto.authorization.AuthorizationRequest;
import com.teamchallenge.markethub.dto.authorization.AuthorizationResponse;
import com.teamchallenge.markethub.dto.user.UserDto;
import com.teamchallenge.markethub.email.CustomTemplates;
import com.teamchallenge.markethub.email.EmailSender;
import com.teamchallenge.markethub.error.exception.UserExistException;
import com.teamchallenge.markethub.model.User;
import com.teamchallenge.markethub.service.impl.UserServiceImpl;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/markethub")
public class AuthorizationController {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationController.class);

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailSender emailSender;

    @PostMapping("/authorization")
    public ResponseEntity<?> createNewUserAndAuthenticationAndSendMail(@RequestBody @Valid AuthorizationRequest authRequest,
                                                                       UriComponentsBuilder uri) {
        checkUserExist(authRequest.email(), authRequest.phone());
        User newUser = userService.create(UserDto.convertToUser(authRequest));
        String username = newUser.getFirstname() + " " + newUser.getLastname();
        sendEmail(authRequest.email(), authRequest.firstname(), authRequest.lastname());
        return ResponseEntity.created(createUserLocation(uri, newUser))
                .body(new AuthorizationResponse(
                        201,
                        username,
                        generateToken(authRequest.email(), authRequest.password()))
                );
    }

    private URI createUserLocation(UriComponentsBuilder uri, User savedSeller) {
        return uri
                .path("/markethub/users/{id}")
                .buildAndExpand(savedSeller.getId())
                .toUri();
    }

    private void checkUserExist(String email, String phone) {
        if (userService.existByEmailAndPhone(email, phone)) {
            throw new UserExistException();
        }
    }

    private void sendEmail(String email, String firstname, String lastname) {
        try {
            String fullName = firstname + " " + lastname;
            Map<String, Object> params = new HashMap<>();
            params.put("name", fullName);
            emailSender.asyncSendMail(email, params, CustomTemplates.WELCOME_AUTHENTICATED_USERS_TEMPLATE);
        } catch (MessagingException | TemplateException | IOException | MailSendException e) {
            log.error(e.getMessage());
        }
    }

    private String generateToken(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(email, password));
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        return jwtUtils.generateTokenFromEmail(user.getUsername());
    }

}
