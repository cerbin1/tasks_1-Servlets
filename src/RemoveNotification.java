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

import static conf.ApplicationProperties.APP_BASE_PATH;

public class RemoveNotification extends HttpServlet {

    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;

    public RemoveNotification() {
        this.notificationService = new NotificationService(new NotificationDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            PrintWriter writer = response.getWriter();
            String notificationId = request.getParameter("notificationId");
            if (notificationService.removeNotification(notificationId)) {
                response.sendRedirect(APP_BASE_PATH + "/notifications");
            } else {
                writer.print("<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <title></title>\n" +
                        "    <script src=\"bootstrap.bundle.min.js\"></script>\n" +
                        "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                        "</head>\n" +
                        "<body>\n" +
                        UiUtils.navbarHtml() +
                        "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\">\n" +
                        "  <strong>Error while removing notification!</strong>\n" +
                        "  <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>\n" +
                        "</div>" +
                        "</body>\n" +
                        "</html>");
            }
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
