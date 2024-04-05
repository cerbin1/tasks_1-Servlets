import db.dao.TaskDao;
import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Tasks extends HttpServlet {

    private final TaskService taskService;
    private final AuthenticationService authenticationService;

    public Tasks() {
        this.taskService = new TaskService(new TaskDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            PrintWriter writer = response.getWriter();
            StringBuilder taskList = UiUtils.taskListToHtml(taskService.getAllTasks());

            writer.print("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <title></title>\n" +
                    "    <script src=\"bootstrap.bundle.min.js\"></script>\n" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    UiUtils.navbarHtml() +
                    "<table class=\"table\">" +
                    "<thead>" +
                    "<tr>" +
                    "<th scope=\"col\">#</th>" +
                    "<th scope=\"col\">Name</th>" +
                    "<th scope=\"col\">Deadline</th>" +
                    "<th scope=\"col\">Assignee</th>" +
                    "<th scope=\"col\">Priority</th>" +
                    "<th scope=\"col\">Is Completed</th>" +
                    "<th scope=\"col\">Complete date</th>" +
                    "<th scope=\"col\">Edit</th>" +
                    "<th scope=\"col\">Remove</th>" +
                    "</tr>" +
                    "</thead>" +
                    "<tbody>" +
                    taskList +
                    "</tbody>" +
                    "</table>" +
                    "</body>\n" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
