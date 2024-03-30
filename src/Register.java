import db.UserActivationLinkRepository;
import db.UserRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.EmailSendingService;
import service.UserService;

import java.io.IOException;
import java.io.PrintWriter;

public class Register extends HttpServlet {
    private final UserService userService;

    public Register() {
        userService = new UserService(new UserRepository(), new UserActivationLinkRepository(), new EmailSendingService());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String username = request.getParameter("username");

        if (userService.validateNoUserWithGivenEmailAndUsername(email, username)) {
            userService.registerUser(email,
                    username,
                    request.getParameter("password"),
                    request.getParameter("name"),
                    request.getParameter("surname"));

            PrintWriter writer = response.getWriter();
            writer.print("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <title>Activate user</title>\n" +
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
                    "                <a class=\"nav-link\" href=\"login.html\">Login</a>\n" +
                    "            </li>\n" +
                    "            <li class=\"nav-item\">\n" +
                    "                <a class=\"nav-link\" href=\"register.html\">Register</a>\n" +
                    "            </li>\n" +
                    "        </ul>\n" +
                    "    </div>\n" +
                    "</nav>" +
                    "<div class=\"container\">\n" +
                    "    <h1>Link has been sent to email. Click it to activate your account.</h1>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>");
        } else {
            response.sendRedirect("error.html");
        }
    }
}
