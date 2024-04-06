import service.NotificationDto;
import service.TaskDto;

import java.util.List;

public interface UiUtils {

    static String navbarHtml() {
        return "<nav class=\"navbar navbar-expand-md navbar-light bg-light\">\n" +
                "    <a class=\"navbar-brand\" href=\"#\">Navbar</a>\n" +
                "    <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarNav\"\n" +
                "            aria-controls=\"navbarNav\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n" +
                "        <span class=\"navbar-toggler-icon\"></span>\n" +
                "    </button>\n" +
                "    <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\n" +
                "        <ul class=\"navbar-nav\">\n" +
                "            <li class=\"nav-item active\">\n" +
                "                <a class=\"nav-link\" href=\"\">Main Page</a>\n" +
                "            </li>\n" +
                "            <li class=\"nav-item\">\n" +
                "                <a class=\"nav-link\" href=\"/tasks_1-Servlets/tasks\">List tasks</a>\n" +
                "            </li>\n" +
                "            <li class=\"nav-item\">\n" +
                "                <a class=\"nav-link\" href=\"/tasks_1-Servlets/createTask\">Create task</a>\n" +
                "            </li>\n" +
                "            <li class=\"nav-item\">\n" +
                "                <a class=\"nav-link\" href=\"/tasks_1-Servlets/myTasks\">My Tasks</a>\n" +
                "            </li>\n" +
                "            <li class=\"nav-item\">\n" +
                "                <a class=\"nav-link\" href=\"/tasks_1-Servlets/notifications\">Notifications</a>\n" +
                "            </li>\n" +
                "            <li class=\"nav-item\">\n" +
                "                <a class=\"nav-link\" href=\"\">User List</a>\n" +
                "            </li>\n" +
                "            <li class=\"nav-item\">\n" +
                "                <a class=\"nav-link\" href=\"\">Stats</a>\n" +
                "            </li>\n" +
                "            <li class=\"nav-item\">\n" +
                "                <a class=\" btn btn-warning\" href=\"/tasks_1-Servlets/logout\">Logout</a>\n" +
                "            </li>\n" +
                "        </ul>\n" +
                "    </div>\n" +
                "</nav>\n";
    }

    static String taskListToHtmlTable(List<TaskDto> tasksData) {
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
                        .append("<td>").append("<a href=\"editTask?taskId=").append(task.getId()).append("\">Edit</a>").append("</td>")
                        .append("<td>").append("<a class=\"btn btn-danger\" href=\"removeTask?taskId=").append(task.getId()).append("\">Remove</a>").append("</td>")
                        .append("</tr>\n");
            }
        }
        return
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
                        "</table>\n" +
                        "\n<h1>Filters</h1>" +
                        "<div class=\"form-group row\">\n" +
                        "<form action=\"/tasks_1-Servlets/searchByName\" method=\"get\">\n" +
                        "<input type=\"text\" class=\"form-control\" name=\"name\" />\n" +
                        "<button type=\"submit\" class=\"btn btn-primary\">Search by name</button>\n" +
                        "</div>";
    }

    static String notificationListToHtmlTable(List<NotificationDto> notificationsData) {
        StringBuilder notifications = new StringBuilder();
        if (notificationsData.isEmpty()) {
            notifications.append("<span>No results</span>");
        } else {
            for (NotificationDto notification : notificationsData) {
                notifications
                        .append("<tr>")
                        .append("<th scope=\"row\">").append(notification.getId()).append("</th>")
                        .append("<td>").append(notification.getName()).append("</td>")
                        .append("<td>").append(notification.getTaskName()).append("</td>")
                        .append("<td>").append(notification.getCreateDate() == null ? "-" : notification.getCreateDate()).append("</td>")
                        .append("<td>").append(notification.isRead() ? "Yes" : "<b>No</b>").append("</td>")
                        .append("<td>").append(notification.getReadDate() == null ? "-" : notification.getReadDate()).append("</td>")
                        .append("<td>").append(notification.getUserNameAssigned()).append("</td>")
                        .append("<td><a href=\"/tasks_1-Servlets/details?taskId=").append(notification.getTaskId()).append("\">Go to task</a></td>\n")
                        .append("<td><a class=\"btn btn-primary ").append(notification.isRead() ? "btn disabled\"" : "\"").append(" href=\"/tasks_1-Servlets/readNotification?notificationId=").append(notification.getId()).append("\">Mark as read</a></td>\n")
                        .append("<td>")
                        .append("<form action=\"/tasks_1-Servlets/removeNotification?notificationId=").append(notification.getId()).append("\" method=\"post\">\n")
                        .append("<button class=\"btn btn-danger\" type=\"submit\">Remove</button>")
                        .append("</form>")
                        .append("</td>")
                        .append("</tr>\n");
            }
        }

        return "    <table class=\"table\">\n" +
                "      <thead>\n" +
                "        <tr>\n" +
                "          <th scope=\"col\">#</th>\n" +
                "          <th scope=\"col\">Name</th>\n" +
                "          <th scope=\"col\">Task name</th>\n" +
                "          <th scope=\"col\">Create date</th>\n" +
                "          <th scope=\"col\">Is read</th>\n" +
                "          <th scope=\"col\">Read date</th>\n" +
                "          <th scope=\"col\">User assigned</th>\n" +
                "          <th scope=\"col\">Link</th>\n" +
                "          <th scope=\"col\">Mark as read</th>\n" +
                "          <th scope=\"col\">Remove</th>\n" +
                "        </tr>\n" +
                "      </thead>\n" +
                "      <tbody>\n" +
                notifications +
                "      </tbody>\n" +
                "    </table>";
    }
}
