import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import db.dao.WorklogDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.EmailSendingService;
import service.UserService;
import service.WorklogService;

import java.io.IOException;

import static conf.ApplicationProperties.APP_BASE_PATH;

public class RemoveWorklog extends HttpServlet {
    private final AuthenticationService authenticationService;
    private final WorklogService worklogService;

    public RemoveWorklog() {
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
        this.worklogService = new WorklogService(new WorklogDao());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            String worklogId = request.getParameter("worklogId");
            worklogService.removeWorklog(worklogId);
            response.sendRedirect(APP_BASE_PATH + "/details?taskId=" + request.getParameter("taskId"));
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
