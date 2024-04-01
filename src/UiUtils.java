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
                "                <a class=\"nav-link\" href=\"tasks.html\">List tasks</a>\n" +
                "            </li>\n" +
                "            <li class=\"nav-item\">\n" +
                "                <a class=\"nav-link\" href=\"/tasks_1-Servlets/createTask\">Create task</a>\n" +

                "            </li>\n" +
                "            <li class=\"nav-item\">\n" +
                "                <a class=\"nav-link\" href=\"\">My Tasks</a>\n" +
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
}
