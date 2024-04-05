import db.dao.TaskDao;
import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.EmailSendingService;
import service.TaskService;
import service.UserService;

import java.io.IOException;
import java.io.PrintWriter;

public class RemoveTask extends HttpServlet {
    private final AuthenticationService authenticationService;
    private final TaskService taskService;

    public RemoveTask() {
        this.taskService = new TaskService(new TaskDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            String taskId = request.getParameter("taskId");
            boolean removed = taskService.removeTask(taskId);
            PrintWriter writer = response.getWriter();
            String tasks = UiUtils.taskListToHtmlTable(taskService.getAllTasks());

            writer.print("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <title></title>\n" +
                    "    <script src=\"bootstrap.bundle.min.js\"></script>\n" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    UiUtils.navbarHtml() +
                    (removed ?
                            "<div class=\"alert alert-success alert-dismissible fade show\" role=\"alert\">\n" +
                                    "Task removed!\n" +
                                    "<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>\n" +
                                    "</div>" :
                            "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\">\n" +
                                    "Error while deleting task!\n" +
                                    "<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>\n" +
                                    "</div>") +
                    tasks +
                    "</body>\n" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
