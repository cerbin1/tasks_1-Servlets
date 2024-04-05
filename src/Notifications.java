import db.dao.NotificationDao;
import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Notifications extends HttpServlet {

    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;

    public Notifications() {
        this.notificationService = new NotificationService(new NotificationDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            PrintWriter writer = response.getWriter();
            List<NotificationDto> notificationsData = notificationService.getAllNotifications();

            StringBuilder notifications = new StringBuilder();
            if (notificationsData.isEmpty()) {
                notifications.append("<span>No results</span>");
            } else {
                for (NotificationDto notification : notificationsData) {
                    notifications
                            .append("<tr>")
                            .append("<th scope=\"row\">").append(notification.getId()).append("</th>")
                            .append("<td>").append(notification.getName()).append("</td>")
                            .append("<td>").append(notification.getTaskName()).append("</td>")
                            .append("<td>").append(notification.getCreateDate() == null ? "-" : notification.getCreateDate()).append("</td>")
                            .append("<td>").append(notification.isRead() ? "Yes" : "<b>No</b>").append("</td>")
                            .append("<td>").append(notification.getReadDate() == null ? "-" : notification.getReadDate()).append("</td>")
                            .append("<td>").append(notification.getUserNameAssigned()).append("</td>")
                            .append("<td><a href=\"/tasks_1-Servlets/details?taskId=").append(notification.getTaskId()).append("\">Go to task</a></td>\n")
                            .append("<td><a class=\"btn btn-primary\" disabled=").append(notification.isRead()).append(" href=\"TODO=").append(notification.getId()).append("\">Mark as read</a></td>\n")
                            .append("<td>").append("<a class=\"btn btn-danger\" href=\"TODO=").append(notification.getId()).append("\">Remove</a>").append("</td>")
                            .append("</tr>\n");
                }
            }
            writer.print("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <title></title>\n" +
                    "    <script src=\"bootstrap.bundle.min.js\"></script>\n" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    UiUtils.navbarHtml() +
                    "    <table class=\"table\">\n" +
                    "      <thead>\n" +
                    "        <tr>\n" +
                    "          <th scope=\"col\">#</th>\n" +
                    "          <th scope=\"col\">Name</th>\n" +
                    "          <th scope=\"col\">Task name</th>\n" +
                    "          <th scope=\"col\">Create date</th>\n" +
                    "          <th scope=\"col\">Is read</th>\n" +
                    "          <th scope=\"col\">Read date</th>\n" +
                    "          <th scope=\"col\">User assigned</th>\n" +
                    "          <th scope=\"col\">Link</th>\n" +
                    "          <th scope=\"col\">Mark as read</th>\n" +
                    "          <th scope=\"col\">Remove</th>\n" +
                    "        </tr>\n" +
                    "      </thead>\n" +
                    "      <tbody>\n" +
                    notifications +
                    "      </tbody>\n" +
                    "    </table>" +
                    "</body>\n" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
