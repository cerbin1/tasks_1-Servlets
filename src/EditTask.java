import db.PriorityRepository;
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

import static conf.ApplicationProperties.APP_BASE_PATH;

public class EditTask extends HttpServlet {
    private final UserService userService;
    private final PriorityService priorityService;
    private final AuthenticationService authenticationService;
    private final TaskService taskService;

    public EditTask() {
        this.taskService = new TaskService(new TaskRepository());
        this.priorityService = new PriorityService(new PriorityRepository());
        this.userService = new UserService(new UserRepository(), new UserActivationLinkRepository(), new EmailSendingService());
        this.authenticationService = new AuthenticationService(userService);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            PrintWriter writer = response.getWriter();
            String taskId = request.getParameter("taskId");

            EditTaskDto task = taskService.getTask(taskId);

            List<UserDto> usersData = userService.getUsersData();
            StringBuilder usersMappedToOptions = new StringBuilder();
            for (UserDto userDto : usersData) {
                usersMappedToOptions.append("<option ");
                if (userDto.getId().equals(task.getAssigneeId())) {
                    usersMappedToOptions.append("selected ");
                }
                usersMappedToOptions
                        .append("value=\"").append(userDto.getId()).append("\">")
                        .append(userDto.getName()).append(" ").append(userDto.getSurname())
                        .append("</option>\n");
            }

            List<PriorityDto> prioritiesData = priorityService.getPrioritiesData();
            StringBuilder prioritiesMappedToOptions = new StringBuilder();
            for (PriorityDto priorityDto : prioritiesData) {
                prioritiesMappedToOptions.append("<option ");
                if (priorityDto.getId().equals(task.getPriorityId())) {
                    prioritiesMappedToOptions.append("selected ");
                }
                prioritiesMappedToOptions
                        .append("value=\"").append(priorityDto.getId()).append("\">")
                        .append(priorityDto.getValue())
                        .append("</option>\n");
            }

            writer.print("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <title>Create task</title>\n" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    UiUtils.navbarHtml() +
                    "<div class='container'>\n" +
                    "    <form action=\"/tasks_1-Servlets/editTask?taskId=" + taskId + "\" method=\"post\">\n" +
                    "        <div class=\"form-group row\">\n" +
                    "            <label for=\"name\" class=\"col-sm-2 col-form-label\">Name</label>\n" +
                    "            <div class=\"col-sm-10\">\n" +
                    "                <input required type=\"text\" class=\"form-control\" id=\"name\" name=\"name\"" +
                    "value=\"" + task.getName() + "\"/>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "        <div class=\"form-group row\">\n" +
                    "            <label for=\"deadline\" class=\"col-sm-2 col-form-label\">Deadline</label>\n" +
                    "            <div class=\"col-sm-10\">\n" +
                    "                <input required class=\"form-control\" id=\"deadline\" name=\"deadline\" type=\"datetime-local\"" +
                    "value=\"" + task.getDeadline() + "\"" +
                    " />\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "\n" +
                    "        <div class=\"form-group row\">\n" +
                    "            <label for=\"user\" class=\"col-sm-2 col-form-label\">Assignee</label>\n" +
                    "            <div class=\"col-sm-10\">\n" +
                    "                <select class=\"form-select\" id=\"user\" name=\"user\">\n" +
                    usersMappedToOptions +
                    "                </select>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "\n" +
                    "        <div class=\"form-group row\">\n" +
                    "            <label for=\"priority\" class=\"col-sm-2 col-form-label\">Priority</label>\n" +
                    "            <div class=\"col-sm-10\">\n" +
                    "                <select class=\"form-select\" id=\"priority\" name=\"priority\">\n" +
                    prioritiesMappedToOptions +
                    "                </select>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "\n" +
                    "        <h1>Labels</h1>\n" +
                    "        <h1>Category</h1>\n" +
                    "        <h1>Subtasks</h1>\n" +
                    "        <h1>Files upload</h1>\n" +
                    "\n" +
                    "        <div class=\"form-group row\">\n" +
                    "            <div class='form-control'>\n" +
                    "                <button type=\"button\" class=\"btn btn-secondary\">Cancel</button>\n" +
                    "                <button type=\"submit\" class=\"btn btn-primary\">Update</button>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "    </form>\n" +
                    "</div>" +
                    "</body>\n" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            String name = request.getParameter("name");
            String deadline = request.getParameter("deadline");
            String userId = request.getParameter("user");
            String priorityId = request.getParameter("priority");
            PrintWriter writer = response.getWriter();
            if (taskService.updateTask(request.getParameter("taskId"), name, deadline, userId, priorityId)) {
                response.sendRedirect(APP_BASE_PATH + "/tasks");
            } else {
                writer.print("<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <title></title>\n" +
                        "    <script src=\"bootstrap.bundle.min.js\"></script>\n" +
                        "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                        "</head>\n" +
                        "<body>\n" +
                        UiUtils.navbarHtml() +
                        "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\">\n" +
                        "  <strong>Error while updating task!</strong>\n" +
                        "  <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>\n" +
                        "</div>" +
                        "</body>\n" +
                        "</html>");
            }
        } else {
            response.sendRedirect("authError.html");
        }
    }

}
