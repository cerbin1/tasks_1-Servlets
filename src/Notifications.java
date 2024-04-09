import db.dao.NotificationDao;
import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.EmailSendingService;
import service.NotificationService;
import service.UserService;
import utils.UiUtils;

import java.io.IOException;
import java.io.PrintWriter;

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
            writer.print("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <title></title>\n" +
                    "    <script src=\"bootstrap.bundle.min.js\"></script>\n" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    UiUtils.navbarHtml() +
                    UiUtils.notificationListToHtmlTable(notificationService.getAllNotifications()) +
                    "</body>\n" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
