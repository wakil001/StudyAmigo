package com.StudyPal.Users.User;

import com.StudyPal.Users.Security.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


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

        User newUser = new User();

        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(encoder.encode(user.getPassword()));

        return userDao.save(newUser);
    }

    public User updateUser(UserUpdateRequest request, int userId) throws ResourceNotFoundException {
        return  userDao.findById(userId).map(existingUser ->{
            existingUser.setName(request.getName());
            existingUser.setEmail(request.email);
            return userDao.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

    }

    public String verify(User user) {
        try {
            System.out.println("Attempting to authenticate user: " + user.getName());

            User existingUser = userDao.findByUsername(user.getName());
            if (existingUser == null) {
                System.out.println("User not found: " + user.getName());
                return "User not found";
            }

            System.out.println("Found user: " + existingUser.getName());

            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(user.getName());
            } else {
                System.out.println("Authentication failed for user: " + user.getName());
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