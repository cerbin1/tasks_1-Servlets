package service;

import db.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(String email, String username, String plainPassword, String name, String surname) {
        String hashedPassword = hashPassword(plainPassword);
        userRepository.createUser(email, username, hashedPassword, name, surname);
    }

    // note - SHA-512 should be changed with PBKDF2, BCrypt, or SCrypt but for simplicity and no additional lib SHA was used
    private String hashPassword(String plainPassword) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update("salt".getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public boolean authenticateUser(String username, String plainPassword) {
        return userRepository.userExists(username, hashPassword(plainPassword));
    }

    public void loginUser(String username, String sessionId) {
        boolean loginExists = userRepository.userLoginExists(username, sessionId);
        if (loginExists) {
            throw new RuntimeException("Login exists");
        } else {
            userRepository.createLogin(username, sessionId);
        }
    }

    public boolean userIsLoggedIn(String username, String sessionId) {
        return userRepository.userLoginExists(username, sessionId);
    }

    public void logoutUser(String username) {
        userRepository.deactivateUserLogin(username);
    }
}
