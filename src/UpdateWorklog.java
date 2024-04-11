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

public class UpdateWorklog extends HttpServlet {
    private final AuthenticationService authenticationService;
    private final WorklogService worklogService;

    public UpdateWorklog() {
        this.worklogService = new WorklogService(new WorklogDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            String date = request.getParameter("date");
            String minutes = request.getParameter("minutes");
            String comment = request.getParameter("comment");
            String worklogId = request.getParameter("worklogId");
            String taskId = request.getParameter("taskId");
            worklogService.updateWorklog(worklogId, date, minutes, comment);
            response.sendRedirect(APP_BASE_PATH + "/details?taskId=" + taskId);
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
