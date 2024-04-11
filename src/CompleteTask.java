import db.dao.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.EmailSendingService;
import service.TaskService;
import service.UserService;

import java.io.IOException;

import static conf.ApplicationProperties.APP_BASE_PATH;

public class CompleteTask extends HttpServlet {

    private final TaskService taskService;
    private final AuthenticationService authenticationService;

    public CompleteTask() {
        this.taskService = new TaskService(new TaskDao(), new SubtaskDao(), new TaskFileDao(), new LabelDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            String taskId = request.getParameter("taskId");
            taskService.completeTask(taskId);
            response.sendRedirect(APP_BASE_PATH + "/myTasks");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
