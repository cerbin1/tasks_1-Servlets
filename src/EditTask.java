import db.dao.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.*;
import service.dto.EditTaskDto;
import service.dto.PriorityDto;
import service.dto.SubtaskDto;
import service.dto.UserDto;
import utils.UiUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static conf.ApplicationProperties.APP_BASE_PATH;

public class EditTask extends HttpServlet {
    private final UserService userService;
    private final PriorityService priorityService;
    private final AuthenticationService authenticationService;
    private final TaskService taskService;
    private final SubtaskService subtaskService;

    public EditTask() {
        this.taskService = new TaskService(new TaskDao());
        this.priorityService = new PriorityService(new PriorityDao());
        this.userService = new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService());
        this.authenticationService = new AuthenticationService(userService);
        this.subtaskService = new SubtaskService(new SubtaskDao());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            PrintWriter writer = response.getWriter();
            String taskId = request.getParameter("taskId");

            EditTaskDto task = taskService.getTaskForEdit(taskId);

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

            List<SubtaskDto> subtasksData = subtaskService.getTaskSubtasks(taskId);

            StringBuilder subtasks = new StringBuilder();
            subtasks.append("<div class=\"d-flex align-items-center justify-content-center\">")
                    .append("<div id=\"subtasks\" class=\"form-group col-md-3\">");
            for (SubtaskDto subtask : subtasksData) {
                subtasks
                        .append("<div class=\"input-group\">")
                        .append("<input class=\"form-control col-md-9\" style=\"text-align: center;\" name=\"subtasksNames[]\" value=\"").append(subtask.getName()).append("\" />")
                        .append("<input type=\"hidden\" name=\"subtasksIds[]\" value=\"").append(subtask.getId()).append("\" />")
                        .append("<a href=\"/tasks_1-Servlets/removeSubtask?")
                        .append("taskId=").append(taskId).append("&subtaskId=").append(subtask.getId())
                        .append("\" type=\"submit\" class=\"btn btn-danger col-md-3\">Remove</a>")
                        .append("</div>");
            }
            subtasks.append("</div>").append("</div>");

            writer.print("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <title>Create task</title>\n" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                    "</head>\n" +
                    "            <script>\n" +
                    "                    document.addEventListener('DOMContentLoaded', function() {\n" +
                    "                    var addSubtaskButton = document.getElementById('addSubtask');\n" +
                    "                    addSubtaskButton.addEventListener('click', function() {\n" +
                    "                    var newSubtask = document.createElement('input');\n" +
                    "                    newSubtask.type = 'text';\n" +
                    "                    newSubtask.classList.add('form-control');\n" +
                    "                    newSubtask.style.textAlign = 'center';\n" +
                    "                    newSubtask.name = 'newSubtasks[]';\n" +
                    "                    newSubtask.placeholder = 'Subtask name';\n" +

                    "                    var subtasksContainer = document.getElementById('subtasks');\n" +
                    "                    subtasksContainer.appendChild(newSubtask);\n" +
                    "                });\n" +
                    "               });\n" +
                    "            </script>" +
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
                    subtasks +
                    "        <button id=\"addSubtask\" type=\"button\" class=\"btn btn-success\">Add subtask</button>" +
                    "        <h1>Files upload</h1>\n" +
                    "\n" +
                    "        <div class=\"form-group row\">\n" +
                    "            <div class='form-control'>\n" +
                    "                <a href=\"/tasks_1-Servlets/tasks\" class=\"btn btn-secondary\">Cancel</a>\n" +
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
            String taskId = request.getParameter("taskId");
            String name = request.getParameter("name");
            String deadline = request.getParameter("deadline");
            String userId = request.getParameter("user");
            String priorityId = request.getParameter("priority");
            String[] subtasksNames = request.getParameterValues("subtasksNames[]");
            String[] subtasksIds = request.getParameterValues("subtasksIds[]");
            String[] newSubtasks = request.getParameterValues("newSubtasks[]");
            PrintWriter writer = response.getWriter();
            if (taskService.updateTaskAndSubtasks(taskId, name, deadline, userId, priorityId, subtasksNames, subtasksIds, newSubtasks)) {
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
