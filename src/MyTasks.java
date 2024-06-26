import db.dao.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.EmailSendingService;
import service.TaskService;
import service.UserService;
import service.dto.TaskDto;
import utils.UiUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MyTasks extends HttpServlet {

    private final TaskService taskService;
    private final AuthenticationService authenticationService;

    public MyTasks() {
        this.taskService = new TaskService(new TaskDao(), new SubtaskDao(), new TaskFileDao(), new LabelDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            PrintWriter writer = response.getWriter();

            List<TaskDto> tasksData = taskService.getUserTasks((String) request.getSession(false).getAttribute("userId"));
            StringBuilder taskList = new StringBuilder();
            if (tasksData.isEmpty()) {
                taskList.append("<tr><td><span>No results</span></td></tr>");
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
                            .append("<td>")
                            .append("<a href=\"/tasks_1-Servlets/details?taskId=").append(task.getId()).append("\">Details</a>\n")
                            .append("</td>")
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
                    "<th scope=\"col\">Details</th>" +
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
