package com.StudyPal.Users.User;

import com.StudyPal.Users.Security.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
    private final AuthenticationManager authManager;
    private final UserDao userDao;

    public UserService(UserDao userDao, JWTService jwtService, AuthenticationManager authManager) {
        this.userDao = userDao;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    public User registerUser(RegisterationRequest user) {
        user.setPassword(encoder.encode(user.getPassword()));

        userDao.save();
        return ;
    }

    public String verify(User user) {
        try {
            System.out.println("Attempting to authenticate user: " + user.getUsername());

            User existingUser = userDao.findByUsername(user.getUsername());
            if (existingUser == null) {
                System.out.println("User not found: " + user.getUsername());
                return "User not found";
            }

            System.out.println("Found user: " + existingUser.getUsername());

            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(user.getUsername());
            } else {
                System.out.println("Authentication failed for user: " + user.getUsername());
                return "Authentication failed";
            }
        } catch (AuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            return "An error occurred during login";
        }
    }
}