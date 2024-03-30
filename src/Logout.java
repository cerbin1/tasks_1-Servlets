import db.UserActivationLinkRepository;
import db.UserRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.EmailSendingService;
import service.UserService;

import java.io.IOException;

public class Logout extends HttpServlet {
    private final UserService userService;

    public Logout() {
        userService = new UserService(new UserRepository(), new UserActivationLinkRepository(), new EmailSendingService());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        String username = (String) request.getSession().getAttribute("username");
        userService.logoutUser(username);
        response.sendRedirect("login.html");
    }
}
