import db.dao.TaskDao;
import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.*;

import java.io.IOException;
import java.io.PrintWriter;

public class TaskDetails extends HttpServlet {

    private final TaskService taskService;
    private final AuthenticationService authenticationService;

    public TaskDetails() {
        this.taskService = new TaskService(new TaskDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            String taskId = request.getParameter("taskId");
            TaskDto task = taskService.getTask(taskId);
            PrintWriter writer = response.getWriter();

            writer.print("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <title></title>\n" +
                    "    <script src=\"bootstrap.bundle.min.js\"></script>\n" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    UiUtils.navbarHtml() +
                    "<div class='container'>\n" +
                    "    <div>\n" +
                    "        <div class=\"row\">\n" +
                    "            <label class=\"col-sm-4 col-form-label fw-bold\">Name</label>\n" +
                    "            <p class=\"col-sm-8\">" + task.getName() + "</p>\n" +
                    "        </div>\n" +
                    "        <div class=\"row\">\n" +
                    "            <label class=\"col-sm-4 col-form-label fw-bold\">Deadline</label>\n" +
                    "            <p class=\"col-sm-8\">" + task.getDeadline() + "</p>\n" +
                    "        </div>\n" +
                    "\n" +
                    "        <div class=\"row\">\n" +
                    "            <label class=\"col-sm-4 col-form-label fw-bold\">Assignee</label>\n" +
                    "            <p class=\"col-sm-8\">" + task.getAssignee() + "</p>\n" +
                    "        </div>\n" +
                    "\n" +
                    "        <div class=\"row\">\n" +
                    "            <label class=\"col-sm-4 col-form-label fw-bold\">Priority</label>\n" +
                    "            <p class=\"col-sm-8\">" + task.getPriority() + "</p>\n" +
                    "        </div>\n" +
                    "\n" +
                    "        <h1>Labels</h1>\n" +
                    "        <h1>Category</h1>\n" +
                    "        <h1>Subtasks</h1>\n" +
                    "        <h1>Chat</h1>\n" +
                    "        <h1>Files</h1>\n" +
                    "        <h1>Worklogs</h1>\n" +
                    "    </div>\n" +
                    "</div>" +
                    "</body>\n" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
