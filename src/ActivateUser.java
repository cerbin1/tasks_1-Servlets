import db.UserActivationLinkRepository;
import db.UserRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.EmailSendingService;
import service.UserService;

import java.io.IOException;

public class ActivateUser extends HttpServlet {
    private final UserService userService;

    public ActivateUser() {
        userService = new UserService(new UserRepository(), new UserActivationLinkRepository(), new EmailSendingService());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String linkId = request.getQueryString().split("=")[1];
        if (userService.activateUserByLink(linkId)) {
            response.sendRedirect("login.html");
        } else {
            response.sendRedirect("error.html");
        }
    }
}
