import db.dao.SubtaskDao;
import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.EmailSendingService;
import service.SubtaskService;
import service.UserService;

import java.io.IOException;

import static conf.ApplicationProperties.APP_BASE_PATH;

public class RemoveSubtask extends HttpServlet {
    private final AuthenticationService authenticationService;
    private final SubtaskService subtaskService;

    public RemoveSubtask() {
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
        this.subtaskService = new SubtaskService(new SubtaskDao());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            String subtaskId = request.getParameter("subtaskId");
            subtaskService.removeSubtask(subtaskId);
            response.sendRedirect(APP_BASE_PATH + "/editTask?taskId=" + request.getParameter("taskId"));
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
