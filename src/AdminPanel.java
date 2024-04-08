import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AdminPanelUserDto;
import service.AuthenticationService;
import service.EmailSendingService;
import service.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AdminPanel extends HttpServlet {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AdminPanel() {
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
        this.userService = new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request) && authenticationService.isAdmin(request)) {
            PrintWriter writer = response.getWriter();
            List<AdminPanelUserDto> usersData = userService.getUsersForAdminPanel();
            StringBuilder users = new StringBuilder();
            for (AdminPanelUserDto user : usersData) {
                users.append("<tr>")
                        .append("<th scope=\"row\">").append(user.getId()).append("</th>")
                        .append("<td>").append(user.getEmail()).append("</td>")
                        .append("<td>").append(user.getUsername()).append("</td>")
                        .append("<td>").append(user.getName()).append("</td>")
                        .append("<td>").append(user.getSurname()).append("</td>")
                        .append("<td>").append(user.getActive() ? "Yes" : "No").append("</td>")
                        .append("<td>").append(user.getMessageCount()).append("</td>")
//                        .append("<td>")
//                        .append("<a href=\"/tasks_1-Servlets/details?taskId=").append(task.getId()).append("\">Details</a>\n")
//                        .append("</td>")
                        .append("</tr>\n");
            }
            writer.print("<html lang=\"en\">" +
                    "<head>" +
                    "    <title></title>" +
                    "    <script src=\"bootstrap.bundle.min.js\"></script>" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">" +
                    "</head>" +
                    "<body>" +
                    UiUtils.navbarHtml() +
                    (usersData.isEmpty() ? "<span>No results</span>" : (
                            "    <table class=\"table\">" +
                                    "      <thead>" +
                                    "        <tr>" +
                                    "          <th scope=\"col\">#</th>" +
                                    "          <th scope=\"col\">Email</th>" +
                                    "          <th scope=\"col\">Login</th>" +
                                    "          <th scope=\"col\">Name</th>" +
                                    "          <th scope=\"col\">Surname</th>" +
                                    "          <th scope=\"col\">Is active</th>" +
                                    "          <th scope=\"col\">Messages count</th>" +
                                    "        </tr>" +
                                    "      </thead>" +
                                    "      <tbody>" +
                                    users +
                                    "      </tbody>" +
                                    "    </table>"
                    )) +
                    "</body>" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
