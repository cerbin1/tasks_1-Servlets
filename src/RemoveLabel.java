import db.dao.LabelDao;
import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.EmailSendingService;
import service.LabelService;
import service.UserService;

import java.io.IOException;

import static conf.ApplicationProperties.APP_BASE_PATH;

public class RemoveLabel extends HttpServlet {
    private final AuthenticationService authenticationService;
    private final LabelService labelService;

    public RemoveLabel() {
        this.labelService = new LabelService(new LabelDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            String labelId = request.getParameter("labelId");
            labelService.removeLabel(labelId);
            response.sendRedirect(APP_BASE_PATH + "/editTask?taskId=" + request.getParameter("taskId"));
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
