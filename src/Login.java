import db.UserActivationLinkRepository;
import db.UserRepository;
import jakarta.servlet.http.*;
import service.EmailSendingService;
import service.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;

import static conf.ApplicationProperties.APP_BASE_PATH;

public class Login extends HttpServlet {
    private final UserService userService;

    public Login() {
        userService = new UserService(new UserRepository(), new UserActivationLinkRepository(), new EmailSendingService());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userService.authenticateUser(username, password)) {
            HttpSession session = request.getSession();
            String sessionId = session.getId();
            if (userService.userIsLoggedIn(username, sessionId)) {
                response.sendRedirect(APP_BASE_PATH + "/loggedIn");
            } else {
                userService.loginUser(username, sessionId);
                session.setAttribute("username", username);
                response.sendRedirect(APP_BASE_PATH + "/loggedIn");
            }
        } else {
            throw new RuntimeException("Login");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String sessionId = tryGetSessionIdFromCookie(request);
        if (userService.userIsLoggedIn(username, sessionId) && userService.userIsActive(username)) {
            PrintWriter writer = response.getWriter();
            writer.print("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <title>Main Page</title>\n" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<nav class=\"navbar navbar-expand-md navbar-light bg-light\">\n" +
                    "    <a class=\"navbar-brand\" href=\"#\">Navbar</a>\n" +
                    "    <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarNav\"\n" +
                    "            aria-controls=\"navbarNav\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n" +
                    "        <span class=\"navbar-toggler-icon\"></span>\n" +
                    "    </button>\n" +
                    "    <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\n" +
                    "        <ul class=\"navbar-nav\">\n" +
                    "            <li class=\"nav-item active\">\n" +
                    "                <a class=\"nav-link\" href=\"\">Main Page</a>\n" +
                    "            </li>\n" +
                    "            <li class=\"nav-item\">\n" +
                    "                <a class=\"nav-link\" href=\"\">List tasks</a>\n" +
                    "            </li>\n" +
                    "            <li class=\"nav-item\">\n" +
                    "                <a class=\"nav-link\" href=\"\">Create task</a>\n" +
                    "            </li>\n" +
                    "            <li class=\"nav-item\">\n" +
                    "                <a class=\"nav-link\" href=\"\">My Tasks</a>\n" +
                    "            </li>\n" +
                    "            <li class=\"nav-item\">\n" +
                    "                <a class=\"nav-link\" href=\"\">Notifications {count}</a>\n" +
                    "            </li>\n" +
                    "            <li class=\"nav-item\">\n" +
                    "                <a class=\"nav-link\" href=\"\">User List</a>\n" +
                    "            </li>\n" +
                    "            <li class=\"nav-item\">\n" +
                    "                <a class=\"nav-link\" href=\"\">Stats</a>\n" +
                    "            </li>\n" +
                    "        </ul>\n" +
                    "    </div>\n" +
                    "</nav>\n" +
                    "</body>\n" +
                    "</html>");
        } else {
            response.sendRedirect("error.html");
        }
    }

    private String tryGetSessionIdFromCookie(HttpServletRequest request) {
        Optional<Cookie> jsessionidCookie = Arrays
                .stream(request.getCookies())
                .filter((name) -> name.getName().equals("JSESSIONID"))
                .findFirst();
        return jsessionidCookie.orElseThrow().getValue();
    }
}
