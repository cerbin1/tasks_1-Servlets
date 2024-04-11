import db.TaskCategory;
import db.dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import service.*;
import service.dto.*;
import utils.UiUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static conf.ApplicationProperties.APP_BASE_PATH;
import static conf.ApplicationProperties.FILE_UPLOADS_BASE_URL;

public class EditTask extends HttpServlet {
    private final UserService userService;
    private final PriorityService priorityService;
    private final AuthenticationService authenticationService;
    private final TaskService taskService;
    private final SubtaskService subtaskService;
    private final TaskFileDao taskFileDao;
    private final LabelService labelService;
    private final WorklogService worklogService;

    public EditTask() {
        this.taskFileDao = new TaskFileDao();
        this.taskService = new TaskService(new TaskDao(), new SubtaskDao(), taskFileDao, new LabelDao());
        this.priorityService = new PriorityService(new PriorityDao());
        this.userService = new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService());
        this.authenticationService = new AuthenticationService(userService);
        this.subtaskService = new SubtaskService(new SubtaskDao());
        this.labelService = new LabelService(new LabelDao());
        this.worklogService = new WorklogService(new WorklogDao());
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

            List<LabelDto> labelsData = labelService.getTaskLabels(taskId);
            StringBuilder labels = new StringBuilder();
            labels.append("<div class=\"d-flex align-items-center justify-content-center\">")
                    .append("<div id=\"labels\" class=\"form-group col-md-3\">");
            for (LabelDto label : labelsData) {
                labels
                        .append("<div class=\"input-group\">")
                        .append("<input class=\"form-control col-md-9\" style=\"text-align: center;\" name=\"labels[]\" value=\"").append(label.getName()).append("\" />")
                        .append("<input type=\"hidden\" name=\"labelIds[]\" value=\"").append(label.getId()).append("\" />")
                        .append("<a href=\"/tasks_1-Servlets/removeLabel?")
                        .append("taskId=").append(taskId).append("&labelId=").append(label.getId())
                        .append("\" type=\"submit\" class=\"btn btn-danger col-md-3\">Remove</a>")
                        .append("</div>");
            }
            labels.append("</div>").append("</div>");

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

            List<TaskFileDto> taskFiles = taskFileDao.findAllForTaskId(Long.parseLong(taskId));
            StringBuilder files = new StringBuilder();

            subtasks.append("<div class=\"d-flex\">")
                    .append("<div class=\"form-group col-md-6\">");

            if (taskFiles.isEmpty()) {
                files.append("<p>No files.</p>");
            } else {
                for (TaskFileDto file : taskFiles) {
                    files.append("<div class=\"input-group\">")
                            .append("<a class=\"col-md-9\" href=\"/tasks_1-Servlets/download?")
                            .append("filename=").append(file.getName()).append("&")
                            .append("filetype=").append(file.getType()).append("\"> ")
                            .append(file.getName()).append("</a>")


                            .append("<a href=\"/tasks_1-Servlets/removeFile?")
                            .append("filename=").append(file.getName())
                            .append("&taskId=").append(file.getTaskId()).append("\"")
                            .append(" type=\"submit\" class=\"btn btn-danger col-md-3\">Remove</a>")
                            .append("</div>");
                }
                files.append("</div>").append("</div>");
            }

            List<String> categoriesData = TaskCategory.listOfValues();
            StringBuilder categories = new StringBuilder();
            categoriesData.forEach(category ->
                    categories.append("<option value=\"").append(category).append("\"")
                            .append(task.getCategory().equals(category) ? " selected" : "")
                            .append(">")
                            .append(category)
                            .append("</option>\n"));


            List<WorklogDto> worklogData = worklogService.getTaskWorklogs(taskId);
            StringBuilder worklogs = new StringBuilder();
            worklogs.append("<div class=\"list-group\" id=\"worklogs\" style=\"display: none\">");
            if (worklogData.isEmpty()) {
                worklogs.append("<b>No data.</b>");
            } else {
                worklogs
                        .append("            <table class=\"table\">")
                        .append("              <thead>")
                        .append("                <tr>")
                        .append("                  <th scope=\"col\">Date</th>")
                        .append("                  <th scope=\"col\">Minutes</th>")
                        .append("                  <th scope=\"col\">Comment</th>")
                        .append("                </tr>")
                        .append("              </thead>")
                        .append("              <tbody>");
                for (WorklogDto worklog : worklogData) {
                    worklogs
                            .append("<tr>")
                            .append("  <td>").append(worklog.getDate().toString()).append("</td>")
                            .append("  <td>").append(worklog.getMinutes()).append("</td>")
                            .append("  <td>").append(worklog.getComment()).append("</td>")
                            .append("</tr>");
                }
                worklogs.append("</tbody>");
                worklogs.append("</table>");
            }
            worklogs.append("</div>");
            worklogs.append("<button type=\"button\" class=\"btn btn-primary\" onclick=\"(function() { ")
                    .append("const div = document.getElementById('worklogs');")
                    .append("div.style.display = 'block';")
                    .append("})()\">Show worklogs</button>");


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
                    "    <form action=\"/tasks_1-Servlets/editTask?taskId=" + taskId + "\" enctype=\"multipart/form-data\" method=\"post\">\n" +
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
                    "        <h1>Labels</h1>" +
                    labels +
                    "<button type=\"button\" class=\"btn btn-success\" onclick=\"(function() { " +
                    "const labels = document.getElementById('labels');" +
                    "const input = document.createElement('input');" +
                    "input.classList.add('form-control');\n" +
                    "input.style.textAlign = 'center';\n" +
                    "input.name = 'newLabels[]';" +
                    "input.placeholder = 'Label name';" +
                    "labels.appendChild(input);" +
                    "})()\">Add label</button>" +

                    "        <h1>Category</h1>\n" +
                    "        <div class=\"d-flex align-items-center justify-content-center\">\n" +
                    "          <div class=\"form-group col-md-3\">\n" +
                    "            <select class=\"form-select\" name=\"category\">\n" +
                    categories +
                    "            </select>\n" +
                    "          </div>\n" +
                    "        </div>" +
                    "        <h1>Subtasks</h1>\n" +
                    subtasks +
                    "        <button id=\"addSubtask\" type=\"button\" class=\"btn btn-success\">Add subtask</button>" +
                    "        <h1>Files upload</h1>\n" +
                    files +
                    "        <input type=\"file\" multiple name=\"files\"/>" +

                    "<h1>Worklogs</h1>" +
                    worklogs +
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (authenticationService.authenticate(request)) {
            String taskId = request.getParameter("taskId");
            String name = request.getParameter("name");
            String deadline = request.getParameter("deadline");
            String userId = request.getParameter("user");
            String priorityId = request.getParameter("priority");
            String[] subtasksNames = request.getParameterValues("subtasksNames[]");
            String[] subtasksIds = request.getParameterValues("subtasksIds[]");
            String[] newSubtasks = request.getParameterValues("newSubtasks[]");
            String[] labels = request.getParameterValues("labels[]");
            String[] labelIds = request.getParameterValues("labelIds[]");
            String[] newLabels = request.getParameterValues("newLabels[]");
            String category = request.getParameter("category");
            PrintWriter writer = response.getWriter();
            if (taskService.updateTaskAndSubtasks(taskId, name, deadline, userId, priorityId, subtasksNames, subtasksIds, newSubtasks, category)) {
                labelService.updateTaskLabels(taskId, labels, labelIds, newLabels);
                uploadNewFiles(request, taskId);
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

    private void uploadNewFiles(HttpServletRequest request, String taskId) throws IOException, ServletException {
        Collection<Part> parts = request
                .getParts().stream()
                .filter(part -> part.getName().equals("files") && !part.getSubmittedFileName().isBlank())
                .collect(Collectors.toList());
        for (Part file : parts) {
            String fileName = file.getSubmittedFileName();
            String realPath = getServletContext().getRealPath(FILE_UPLOADS_BASE_URL);
            taskService.saveOrUpdateTaskFileInfo(fileName, file.getContentType(), taskId);
            file.write(realPath + File.separator + fileName);
        }
    }

}
