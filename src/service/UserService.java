package service;

import db.UserActivationLinkRepository;
import db.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static conf.ApplicationProperties.APP_BASE_PATH;
import static conf.ApplicationProperties.APP_BASE_URL;

public class UserService {
    private final UserRepository userRepository;
    private final UserActivationLinkRepository userActivationLinkRepository;
    private final EmailSendingService emailSendingService;

    public UserService(UserRepository userRepository, UserActivationLinkRepository userActivationLinkRepository, EmailSendingService emailSendingService) {
        this.userRepository = userRepository;
        this.userActivationLinkRepository = userActivationLinkRepository;
        this.emailSendingService = emailSendingService;
    }

    public void registerUser(String email, String username, String plainPassword, String name, String surname) {
        String hashedPassword = hashPassword(plainPassword);
        userRepository.createUser(email, username, hashedPassword, name, surname);

        UUID randomUuid = UUID.randomUUID();
        userActivationLinkRepository.createLink(username, randomUuid);
        String mailContent = "Go to this link to activate your account: \n" + APP_BASE_URL + APP_BASE_PATH + "/activate?linkId=" + randomUuid;
        emailSendingService.sendEmail("Task Application - activation link", mailContent, email);
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
        userRepository.createLogin(username, sessionId);
    }

    public boolean userIsLoggedIn(String username, String sessionId) {
        return userRepository.userLoginExists(username, sessionId);
    }

    public void logoutUser(String username) {
        userRepository.deactivateUserLogin(username);
    }

    public boolean activateUserByLink(String id) {
        String username = userActivationLinkRepository.getUsernameForNonExpiredLink(id);
        if (userRepository.setUserActive(username)) {
            return userActivationLinkRepository.setLinkExpired(id);
        }
        return false;
    }

    public boolean userIsActive(String username) {
        return userRepository.getActiveUserWith(username);
    }

    public boolean validateNoUserWithGivenEmailAndUsername(String email, String username) {
        return !userRepository.existsByEmail(email) && !userRepository.existsByUsername(username);
    }
}
