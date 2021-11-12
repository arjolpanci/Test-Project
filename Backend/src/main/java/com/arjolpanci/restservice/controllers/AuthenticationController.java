package com.arjolpanci.restservice.controllers;

import com.arjolpanci.restservice.dbmodels.ResetTokens;
import com.arjolpanci.restservice.dbmodels.User;
import com.arjolpanci.restservice.httpmodels.*;
import com.arjolpanci.restservice.services.MailService;
import com.arjolpanci.restservice.services.UsersService;
import com.arjolpanci.restservice.util.JwtUtils;
import com.arjolpanci.restservice.util.ResponseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger LOG = LogManager.getLogger(AuthenticationController.class);

    @Autowired
    private ResponseManager responseManager;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/users")
    public ResponseManager.ResponseObject<List<User>> getUsers(@RequestHeader("Authorization") String authHeader,
                                                               @RequestBody IncomingPageRequest request) {
        try {
            String token = authHeader.split(" ")[1];
            jwtUtils.verifyUserAuthority(null, token, User.ROLE_SUPERVISOR, null);
            List<User> users = usersService.getUsers(request).getContent();
            return responseManager.getFormattedResponse(HttpStatus.OK, "Users retrieved successfully!", users);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PostMapping("/login")
    public ResponseManager.ResponseObject<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            User user = usersService.findUserByUsername(username);

            if(passwordEncoder.matches(password, user.getPassword())){
                String role = user.getRole();
                String token = jwtUtils.generateToken(user, role);
                LoginResponse response = new LoginResponse(user.getId(), token, role);
                return responseManager.getFormattedResponse(HttpStatus.OK, "User authenticated successfully!", response);
            }else{
                throw new BadCredentialsException("Bad Credentials");
            }
        } catch (BadCredentialsException e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.UNAUTHORIZED, "Incorrect credentials provided!", null);
        } catch (UsernameNotFoundException e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.UNAUTHORIZED, "There is no user with the provided username!", null);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.UNAUTHORIZED, "Could not authenticate user!", null);
        }
    }

    @PostMapping("/register")
    public ResponseManager.ResponseObject<String> register(@RequestBody RegisterRequest requestBody) {
        try {
            if(requestBody.getUsername() == "" || requestBody.getEmail() == "" || requestBody.getPassword() == "") {
                throw new Exception("Incorrect data supplied!");
            }
            User newUser = new User();
            newUser.setUsername(requestBody.getUsername());
            newUser.setPassword(passwordEncoder.encode(requestBody.getPassword()));
            String role = requestBody.getRole();
            if(role == null) {
                role = User.ROLE_USER;
            }
            newUser.setRole(role);
            newUser.setEmail(requestBody.getEmail());
            usersService.saveUser(newUser);
            return responseManager.getFormattedResponse(HttpStatus.OK, "User registered succesfully!", null);
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseManager.ResponseObject<String> updateUser(@RequestHeader("Authorization") String authHeader,
                                                             @PathVariable(value="userId") Long userId,
                                                             @RequestBody RegisterRequest requestBody) {
        try {
            String token = authHeader.split(" ")[1];
            jwtUtils.verifyUserAuthority(null, token, User.ROLE_SUPERVISOR, null);
            User user = usersService.findUserById(userId);
            if(requestBody.getEmail() != null) user.setEmail(requestBody.getEmail());
            if(requestBody.getRole() != null) user.setRole(requestBody.getRole());
            usersService.updateUser(user);
            return responseManager.getFormattedResponse(HttpStatus.OK, "User updated succesfully!", null);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PostMapping("/password/reset")
    public ResponseManager.ResponseObject<String> resetPassword(@RequestBody RegisterRequest requestBody) {
        try {
            User user = usersService.findUserByEmail(requestBody.getEmail());
            String resetToken = usersService.setTokenForUser(user);
            mailService.sendEmail(user.getEmail(), "Reset Password Token", "Your password reset token is: " + resetToken);
            return responseManager.getFormattedResponse(HttpStatus.OK, "An email has been sent with your reset code", null);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PostMapping("/password/update")
    public ResponseManager.ResponseObject<String> updatePassword(@RequestBody PasswordResetRequest request) {
        try {
            User user = usersService.findUserByEmail(request.getEmail());
            ResetTokens resetTokens = usersService.getTokenForUser(user);
            if(request.getToken().equals(resetTokens.getToken()) && user.getId().equals(resetTokens.getUser().getId())) {
                usersService.updatePassword(user, request.getPassword());
                return responseManager.getFormattedResponse(HttpStatus.OK, "The password has been updated!", null);
            }else {
                throw new Exception("Invalid password reset token supplied");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return responseManager.getFormattedResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
}
