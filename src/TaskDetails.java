import db.dao.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.*;
import service.dto.*;
import utils.UiUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TaskDetails extends HttpServlet {

    private final TaskService taskService;
    private final AuthenticationService authenticationService;
    private final ChatMessageService chatMessageService;
    private final SubtaskService subtaskService;
    private final TaskFileDao taskFileDao;
    private final LabelService labelService;
    private final WorklogService worklogService;

    public TaskDetails() {
        this.taskFileDao = new TaskFileDao();
        this.taskService = new TaskService(new TaskDao(), new SubtaskDao(), taskFileDao, new LabelDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
        this.chatMessageService = new ChatMessageService(new ChatMessageDao());
        this.subtaskService = new SubtaskService(new SubtaskDao());
        this.labelService = new LabelService(new LabelDao());
        this.worklogService = new WorklogService(new WorklogDao());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            String taskId = request.getParameter("taskId");
            TaskDto task = taskService.getTask(taskId);
            String userId = (String) request.getSession().getAttribute("userId");
            PrintWriter writer = response.getWriter();

            List<LabelDto> labelsData = labelService.getTaskLabels(taskId);
            StringBuilder labels = new StringBuilder();
            if (labelsData.isEmpty()) {
                labels.append("<p>No labels added.</p>");
            } else {
                for (LabelDto label : labelsData) {
                    labels.append("<div class=\"d-flex align-items-center justify-content-center\">")
                            .append("<div class=\"input-group mb-1\">")
                            .append("<span class=\"form-control\">")
                            .append(label.getName())
                            .append("</span>")
                            .append("</div>")
                            .append("</div>");
                }
            }

            List<ChatMessageDto> taskChatMessages = chatMessageService.getTaskChatMessages(taskId);
            StringBuilder chatMessages = new StringBuilder();
            if (taskChatMessages.isEmpty()) {
                chatMessages.append("<p>No messages yet.</p>");
            } else {
                for (ChatMessageDto chatMessage : taskChatMessages) {
                    if (chatMessage.getSenderId().equals(userId)) {
                        chatMessages.append("<div class=\"d-flex flex-row\">");
                    } else {
                        chatMessages.append("<div class=\"d-flex flex-row-reverse\">");
                    }
                    chatMessages.append("<div>");
                    chatMessages.append("<b>").append(chatMessage.getSenderName()).append(":</b>");
                    chatMessages.append("<p>").append(chatMessage.getContent()).append("</p>");
                    chatMessages.append("</div>");
                    chatMessages.append("</div>");
                }
            }

            List<TaskFileDto> taskFiles = taskFileDao.findAllForTaskId(Long.parseLong(taskId));
            StringBuilder files = new StringBuilder();
            if (taskFiles.isEmpty()) {
                files.append("<p>No files.</p>");
            } else {
                for (TaskFileDto file : taskFiles) {
                    files.append("<div>");
                    files.append("<a href=\"/tasks_1-Servlets/download?")
                            .append("filename=").append(file.getName()).append("&")
                            .append("filetype=").append(file.getType()).append("\"> ")
                            .append(file.getName()).append("</a>");
                    files.append("</div>");
                }
            }

            List<SubtaskDto> subtasksData = subtaskService.getTaskSubtasks(taskId);
            StringBuilder subtasks = new StringBuilder();
            if (subtasksData.isEmpty()) {
                subtasks.append("<p>No subtasks yet.</p>");
            } else {
                for (SubtaskDto subtask : subtasksData) {
                    subtasks.append("<div class=\"d-flex align-items-center justify-content-center\">")
                            .append("<div class=\"col-md-3\">")
                            .append("<div class=\"input-group\">")
                            .append("<div id=\"").append(subtask.getSequence()).append("\" class=\"col-md-9\" style=\"textAlign: center\">").append(subtask.getName()).append("</div>")
                            .append("<button type=\"button\" class=\"btn btn-success col-md-3\" onclick=\"(function() { document.getElementById('")
                            .append(subtask.getSequence()).append("').style.textDecoration = 'line-through'; })()\">Done</button>")
                            .append("</div>")
                            .append("</div>")
                            .append("</div>");
                }
            }

            List<WorklogDto> worklogData = worklogService.getTaskWorklogs(taskId);
            StringBuilder worklogs = new StringBuilder();
            worklogs.append("<div class=\"list-group\">");
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
                        .append("                  <th scope=\"col\">Edit</th>")
                        .append("                  <th scope=\"col\">Delete</th>")
                        .append("                </tr>")
                        .append("              </thead>")
                        .append("              <tbody>");
                for (WorklogDto worklog : worklogData) {
                    worklogs

                            .append("<tr>")
                            .append("<form action=\"/tasks_1-Servlets/updateWorklog?")
                            .append("worklogId=").append(worklog.getId()).append("&taskId=").append(taskId)
                            .append("   \" method=\"post\">")
                            .append("  <td><input type=\"date\" class=\"form-control\" name=\"date\" value=\"").append(worklog.getDate().toString()).append("\" /></td>")
                            .append("  <td><input type=\"number\" class=\"form-control\" name=\"minutes\" min=\"1\" max=\"1000\"")
                            .append("value=\"").append(worklog.getMinutes()).append("\" /></td>")
                            .append("  <td><input class=\"form-control\" name=\"comment\" ")
                            .append("value=\"").append(worklog.getComment()).append("\" /></td>")
                            .append("<td>")
                            .append("       <button type=\"submit\" class=\"btn btn-primary\">Update</button></td>")
                            .append("</form>")
                            .append("<td>")
                            .append("   <form action=\"/tasks_1-Servlets/removeWorklog?")
                            .append("worklogId=").append(worklog.getId()).append("&taskId=").append(taskId)
                            .append("   \" method=\"post\">")
                            .append("       <button type=\"submit\" class=\"btn btn-danger\">Delete</button></td>")
                            .append("   </form>")
                            .append("</tr>");
                }
                worklogs.append("</tbody>");
                worklogs.append("</table>");
                worklogs.append("</div>");
            }

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
                    labels +
                    "        <h1>Category</h1>\n" +
                    "        <div class=\"row\">\n" +
                    "          <label class=\"col-sm-4 col-form-label fw-bold\">Category</label>\n" +
                    "          <p class=\"col-sm-8\">" + task.getCategory() + "</p>\n" +
                    "        </div>" +
                    "        <h1>Subtasks</h1>\n" +
                    subtasks +
                    "        <h1>Chat</h1>\n" +
                    chatMessages +
                    "            <form action=\"/tasks_1-Servlets/createChatMessage?taskId=" + taskId + "\" method=\"post\">\n" +
                    "              <input type=\"text\" class=\"form-control\" name=\"messageContent\"  placeholder='Message content'/>\n" +
                    "              <button type=\"submit\" class=\"btn btn-primary\">Send Message</button>\n" +
                    "            </form>" +
                    "        <h1>Files</h1>\n" +
                    files +
                    "        <h1>Worklogs</h1>\n" +
                    worklogs +
                    "        <div class=\"form-group row\">\n" +
                    "            <div class='form-control'>\n" +
                    "                <a href=\"/tasks_1-Servlets/myTasks\" class=\"btn btn-secondary\">Back</a>\n" +
                    "                <button class=\"btn btn-primary\" data-bs-toggle=\"modal\" data-bs-target=\"#logTimeModal\" ref={openModal}>\n" +
                    "                   Log time\n" +
                    "                 </button>\n" +
                    "            </div>\n" +
                    "        </div>\n" +

                    "        <div class=\"modal fade\" id=\"logTimeModal\" tabIndex=\"-1\" aria-labelledby=\"#logTimeModalLabel\" >\n" +
                    "          <div class=\"modal-dialog\">\n" +
                    "            <div class=\"modal-content\">\n" +
                    "              <div class=\"modal-header\">\n" +
                    "                <h5 class=\"modal-title\" id=\"logTimeModalLabel\">Worklog</h5>\n" +
                    "                <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\" ref={closeModal}></button>\n" +
                    "              </div>\n" +
                    "              <div class=\"modal-body\">\n" +
                    "                <form action=\"/tasks_1-Servlets/createWorklog?creatorId=" + userId + "&taskId=" + taskId + "\" method=\"post\">\n" +
                    "                  <input type=\"date\" class=\"form-control\" id=\"date\" name=\"date\" />\n" +
                    "                  <input type=\"number\" class=\"form-control\" name=\"minutes\" placeholder=\"Minutes worked\" min=\"1\" max=\"1000\" />\n" +
                    "                  <input type=\"text\" class=\"form-control\" name=\"comment\" placeholder='Comment' />\n" +
                    "                  <div class=\"modal-footer\">\n" +
                    "                    <button type=\"button\" class=\"btn btn-secondary\" data-bs-dismiss=\"modal\"\n" +
                    "                    >Close</button>\n" +
                    "                    <button type=\"submit\" class=\"btn btn-primary\">Log Time</button>\n" +
                    "                  </div>\n" +
                    "                </form>\n" +
                    "              </div>\n" +
                    "            </div>\n" +
                    "          </div>\n" +
                    "        </div>" +
                    "</div>" +
                    "</body>\n" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
