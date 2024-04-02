import db.TaskRepository;
import db.UserActivationLinkRepository;
import db.UserRepository;
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
        this.taskService = new TaskService(new TaskRepository());
        this.authenticationService = new AuthenticationService(new UserService(new UserRepository(), new UserActivationLinkRepository(), new EmailSendingService()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            PrintWriter writer = response.getWriter();

            List<TaskDto> tasksData = taskService.getAllTasks();
            StringBuilder taskList = new StringBuilder();
            if (tasksData.isEmpty()) {
                taskList.append("<span>No results</span>");
            } else {
                for (TaskDto task : tasksData) {
                    taskList
                            .append("<tr>")
                            .append("<th scope=\"row\">").append(task.getId()).append("</th>")
                            .append("<td>").append(task.getName()).append("</td>")
                            .append("<td>").append(task.getDeadline().toString()).append("</td>")
                            .append("<td>").append(task.getAssignee()).append("</td>")
                            .append("<td>").append(task.getPriority()).append("</td>")
                            .append("<td>").append(task.isCompleted() ? "Yes" : "No").append("</td>")
                            .append("<td>").append(task.getCompleteDate() == null ? "-" : task.getCompleteDate().toString()).append("</td>")
                            .append("</tr>\n");
                }
            }

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
