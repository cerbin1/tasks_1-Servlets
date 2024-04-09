import db.dao.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.EmailSendingService;
import service.TaskService;
import service.UserService;
import utils.UiUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class SearchByName extends HttpServlet {

    private final TaskService taskService;
    private final AuthenticationService authenticationService;

    public SearchByName() {
        this.taskService = new TaskService(new TaskDao(), new SubtaskDao(), new TaskFileDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            PrintWriter writer = response.getWriter();
            String name = request.getParameter("name");

            String tasks = UiUtils.taskListToHtmlTable(taskService.getTasksByName(name));

            writer.print("<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <title></title>\n" +
                    "    <script src=\"bootstrap.bundle.min.js\"></script>\n" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    UiUtils.navbarHtml() +
                    tasks +
                    "</body>\n" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
