import db.dao.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.EmailSendingService;
import service.StatisticService;
import service.UserService;
import service.dto.StatisticDto;
import utils.UiUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class Stats extends HttpServlet {

    private final AuthenticationService authenticationService;
    private final StatisticService statisticService;

    public Stats() {
        UserDao userDao = new UserDao();
        this.authenticationService = new AuthenticationService(new UserService(userDao, new UserActivationLinkDao(), new EmailSendingService()));
        this.statisticService = new StatisticService(userDao, new TaskDao(), new SubtaskDao(), new NotificationDao());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            PrintWriter writer = response.getWriter();
            StatisticDto statistics = statisticService.getStatistics();
            writer.print("<html lang=\"en\">" +
                    "<head>" +
                    "    <title></title>" +
                    "    <script src=\"bootstrap.bundle.min.js\"></script>" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">" +
                    "</head>" +
                    "<body>" +
                    UiUtils.navbarHtml() +
                    "<h2>General statistics</h2>" +
                    "      <div className=\"card\">" +
                    "        <div className=\"card-body\">" +
                    "          Number of users:" +
                    "          <span className=\"fw-bold px-1\">" + statistics.getNumberOfUsers() + "</span>" +
                    "        </div>" +
                    "      </div>" +
                    "      <div className=\"card\">" +
                    "        <div className=\"card-body\">" +
                    "          Number of tasks created:" +
                    "          <span className=\"fw-bold px-1\">" + statistics.getNumberOfCreatedTasks() + "</span>" +
                    "        </div>" +
                    "      </div>" +
                    "      <div className=\"card\">" +
                    "        <div className=\"card-body\">" +
                    "          Number of tasks completed:" +
                    "          <span className=\"fw-bold px-1\">" + statistics.getNumberOfCompletedTasks() + "</span>" +
                    "        </div>" +
                    "      </div>" +
                    "      <div className=\"card\">" +
                    "        <div className=\"card-body\">" +
                    "          Number of subtasks:" +
                    "          <span className=\"fw-bold px-1\">" + statistics.getNumberOfSubtasks() + "</span>" +
                    "        </div>" +
                    "      </div>" +
                    "      <div className=\"card\">" +
                    "        <div className=\"card-body\">" +
                    "          Number of notifications:" +
                    "          <span className=\"fw-bold px-1\">" + statistics.getNumberOfNotifications() + "</span>" +
                    "        </div>" +
                    "      </div>" +
                    "</body>" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
