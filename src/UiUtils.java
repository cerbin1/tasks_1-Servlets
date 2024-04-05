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
                "                <a class=\"nav-link\" href=\"\">Notifications {count}</a>\n" +
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
                        "</table>\n";
    }
}
