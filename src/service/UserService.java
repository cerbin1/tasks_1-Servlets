package service;

import db.dao.UserActivationLinkDao;
import db.dao.UserDao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import static conf.ApplicationProperties.APP_BASE_PATH;
import static conf.ApplicationProperties.APP_BASE_URL;

public class UserService {
    private final UserDao userDao;
    private final UserActivationLinkDao userActivationLinkDao;
    private final EmailSendingService emailSendingService;

    public UserService(UserDao userDao, UserActivationLinkDao userActivationLinkDao, EmailSendingService emailSendingService) {
        this.userDao = userDao;
        this.userActivationLinkDao = userActivationLinkDao;
        this.emailSendingService = emailSendingService;
    }

    public void registerUser(String email, String username, String plainPassword, String name, String surname) {
        String hashedPassword = hashPassword(plainPassword);
        userDao.createUser(email, username, hashedPassword, name, surname);

        UUID randomUuid = UUID.randomUUID();
        userActivationLinkDao.createLink(username, randomUuid);
        String mailContent = "Go to this link to activate your account: \n" + APP_BASE_URL + APP_BASE_PATH + "/activate?linkId=" + randomUuid;
        emailSendingService.sendEmail("Task Application - activation link", mailContent, email);
    }

    public List<UserDto> getUsersData() {
        return userDao.findAll();
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

    public String getUserIdByUserCredentials(String username, String plainPassword) {
        return userDao.getUserIdByUsernameAndPassword(username, hashPassword(plainPassword));
    }

    public void createLogin(String username, String sessionId) {
        userDao.createLogin(username, sessionId);
    }

    public boolean userIsLoggedIn(String username, String sessionId) {
        return userDao.userLoginExists(username, sessionId);
    }

    public void logoutUser(String username) {
        userDao.deactivateUserLogin(username);
    }

    public boolean activateUserByLink(String id) {
        String username = userActivationLinkDao.getUsernameForNonExpiredLink(id);
        if (userDao.setUserActive(username)) {
            return userActivationLinkDao.setLinkExpired(id);
        }
        return false;
    }

    public boolean userIsActive(String username) {
        return userDao.getActiveUserWith(username);
    }

    public boolean validateNoUserWithGivenEmailAndUsername(String email, String username) {
        return !userDao.existsByEmail(email) && !userDao.existsByUsername(username);
    }
}
