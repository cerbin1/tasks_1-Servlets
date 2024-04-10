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

    public TaskDetails() {
        this.taskFileDao = new TaskFileDao();
        this.taskService = new TaskService(new TaskDao(), new SubtaskDao(), taskFileDao, new LabelDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
        this.chatMessageService = new ChatMessageService(new ChatMessageDao());
        this.subtaskService = new SubtaskService(new SubtaskDao());
        this.labelService = new LabelService(new LabelDao());
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
                    "    </div>\n" +
                    "        <div class=\"form-group row\">\n" +
                    "            <div class='form-control'>\n" +
                    "                <a href=\"/tasks_1-Servlets/myTasks\" class=\"btn btn-secondary\">Back</a>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "</div>" +
                    "</body>\n" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
