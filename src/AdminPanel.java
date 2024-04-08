import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.EmailSendingService;
import service.UserService;

import java.io.IOException;
import java.io.PrintWriter;

public class AdminPanel extends HttpServlet {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AdminPanel() {
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
        this.userService = new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticateAdmin(request)) {
            PrintWriter writer = response.getWriter();
            if (userService.removeUser(request.getParameter("userId"))) {
                String successMessage = "<div class=\"alert alert-success alert-dismissible fade show\" role=\"alert\">" +
                        "  <strong>User removed!</strong>" +
                        "  <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>" +
                        "</div>";
                viewListOfUsersWithMessage(writer, successMessage);
            } else {
                String failureMessage = "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\">" +
                        "  <strong>Error - user not deleted!</strong>" +
                        "  <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>" +
                        "</div>";
                viewListOfUsersWithMessage(writer, failureMessage);
            }
        } else {
            response.sendRedirect("authError.html");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticateAdmin(request)) {
            PrintWriter writer = response.getWriter();
            writer.print("<html lang=\"en\">" +
                    "<head>" +
                    "    <title></title>" +
                    "    <script src=\"bootstrap.bundle.min.js\"></script>" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">" +
                    "</head>" +
                    "<body>" +
                    UiUtils.navbarHtml() +
                    UiUtils.adminPanelUserListListToHtmlTable(userService.getUsersForAdminPanel()) +
                    "</body>" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }

    private void viewListOfUsersWithMessage(PrintWriter writer, String message) {
        writer.print("<html lang=\"en\">" +
                "<head>" +
                "    <title></title>" +
                "    <script src=\"bootstrap.bundle.min.js\"></script>" +
                "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">" +
                "</head>" +
                "<body>" +
                UiUtils.navbarHtml() +
                message +
                UiUtils.adminPanelUserListListToHtmlTable(userService.getUsersForAdminPanel()) +
                "</body>" +
                "</html>");
    }
}
