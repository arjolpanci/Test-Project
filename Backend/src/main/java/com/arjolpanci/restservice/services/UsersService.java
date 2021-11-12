package com.arjolpanci.restservice.services;

import com.arjolpanci.restservice.dbmodels.ResetTokens;
import com.arjolpanci.restservice.dbmodels.User;
import com.arjolpanci.restservice.httpmodels.IncomingPageRequest;
import com.arjolpanci.restservice.repositories.ResetTokenRepository;
import com.arjolpanci.restservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    public User findUserById(Long userId) throws Exception {
        Optional<User> optUser = userRepository.findById(userId);
        if(optUser.isPresent()) {
            return optUser.get();
        }
        throw new Exception("There is no user with the requested id!");
    }

    public Page<User> getUsers(IncomingPageRequest request) {
        Pageable pageData = PageRequest.of(request.getPageNumber(), request.getPageSize());
        return userRepository.getUsers(pageData);
    }

    public User findUserByUsername(String username) throws Exception {
        Optional<User> optUser = userRepository.findByUsername(username);
        if(optUser.isPresent()) {
            return optUser.get();
        }
        throw new Exception("User with username " + username + " not found!");
    }

    public User findUserByEmail(String email) throws Exception {
        Optional<User> optUser = userRepository.findByEmail(email);
        if(optUser.isPresent()) {
            return optUser.get();
        }
        throw new Exception("Can't find user with the supplied email!");
    }

    public void saveUser(User user) throws Exception {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if(optionalUser.isPresent()) {
            throw new Exception("Sorry that username already exists");
        }
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteTokenForUser(User user) {
        Optional<ResetTokens> resetTokens = resetTokenRepository.findByUserId(user.getId());
        if(resetTokens.isPresent()){
            resetTokenRepository.delete(resetTokens.get());
        }
    }

    public ResetTokens getTokenForUser(User user) throws Exception {
        Optional<ResetTokens> resetTokens = resetTokenRepository.findByUserId(user.getId());
        if(resetTokens.isPresent()) {
            return resetTokens.get();
        }
        throw new Exception("There is no reset token for the supplied user!");
    }

    public String setTokenForUser(User user) {
        ResetTokens resetTokens = new ResetTokens();
        String token = randomString(10);
        resetTokens.setUser(user);
        resetTokens.setToken(token);
        resetTokenRepository.save(resetTokens);
        return token;
    }

    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        this.updateUser(user);
        this.deleteTokenForUser(user);
    }

    private static String randomString(int length) {
        char[] characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();;
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }

}
