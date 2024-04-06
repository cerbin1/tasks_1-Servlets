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

import java.io.IOException;
import java.io.PrintWriter;

public class ReadNotification extends HttpServlet {

    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;

    public ReadNotification() {
        this.notificationService = new NotificationService(new NotificationDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            String notificationId = request.getParameter("notificationId");
            PrintWriter writer = response.getWriter();
            if (notificationService.markNotificationAsRead(notificationId)) {
                String successMessage = "<div class=\"alert alert-success alert-dismissible fade show\" role=\"alert\">\n" +
                        "  <strong>Successfully marked notification as read.</strong>\n" +
                        "  <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>\n" +
                        "</div>";
                viewListOfNotificationWithMessage(writer, successMessage);
            } else {
                String failureMessage = "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\">\n" +
                        "  <strong>Error while removing notification!</strong>\n" +
                        "  <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>\n" +
                        "</div>";
                viewListOfNotificationWithMessage(writer, failureMessage);
            }
        } else {
            response.sendRedirect("authError.html");
        }
    }

    private void viewListOfNotificationWithMessage(PrintWriter writer, String message) {
        writer.print("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "    <script src=\"bootstrap.bundle.min.js\"></script>\n" +
                "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                UiUtils.navbarHtml() +
                message +
                UiUtils.notificationListToHtmlTable(notificationService.getAllNotifications()) +
                "</body>\n" +
                "</html>");
    }
}
