import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.AuthenticationService;
import service.EmailSendingService;
import service.UserService;
import utils.UiUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static conf.ApplicationProperties.APP_BASE_PATH;

public class Login extends HttpServlet {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public Login() {
        userService = new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService());
        authenticationService = new AuthenticationService(userService);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userId = userService.getUserIdByUserCredentials(username, password);
        if (userId.isBlank()) {
            throw new RuntimeException("Login");
        } else {
            HttpSession session = request.getSession();
            String sessionId = session.getId();
            if (userService.userIsLoggedIn(username, sessionId)) {
                response.sendRedirect(APP_BASE_PATH + "/loggedIn");
            } else {
                userService.createLogin(username, sessionId);
                session.setAttribute("username", username);
                session.setAttribute("userId", userId);
                response.sendRedirect(APP_BASE_PATH + "/loggedIn");
            }
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            PrintWriter writer = response.getWriter();
            writer.print("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <title>Main Page</title>\n" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    UiUtils.navbarHtml() +
                    "</body>\n" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
