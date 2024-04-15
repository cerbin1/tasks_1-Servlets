import db.dao.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.EmailSendingService;
import service.StatisticService;
import service.UserService;
import service.dto.StatisticDto;
import service.dto.TasksCountForDateDto;
import service.dto.TimeLoggedDto;
import utils.UiUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class Stats extends HttpServlet {

    private final AuthenticationService authenticationService;
    private final StatisticService statisticService;

    public Stats() {
        UserDao userDao = new UserDao();
        this.authenticationService = new AuthenticationService(new UserService(userDao, new UserActivationLinkDao(), new EmailSendingService()));
        this.statisticService = new StatisticService(userDao, new TaskDao(), new SubtaskDao(), new NotificationDao(), new WorklogDao());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            PrintWriter writer = response.getWriter();
            StatisticDto statistics = statisticService.getGeneralStatistics();


            List<TasksCountForDateDto> numberOfTasks = statisticService.getNumberOfTasks();
            StringBuilder dateValues = new StringBuilder("[");
            numberOfTasks.forEach(tasksCountForDateDto -> dateValues.append("\"").append(tasksCountForDateDto.getDate().toString()).append("\","));
            dateValues.append("]");
            String taskCounts = Arrays.toString(numberOfTasks.stream().map(TasksCountForDateDto::getCount).toArray());

            List<TimeLoggedDto> timeLoggedByUsers = statisticService.getTimeLoggedByUsers();
            String usernames = Arrays.toString(timeLoggedByUsers.stream().map(timeLoggedDto -> "\"" + timeLoggedDto.getName() + "\"").toArray());
            String timeLoggedCounts = Arrays.toString(timeLoggedByUsers.stream().map(TimeLoggedDto::getMinutesLogged).toArray());
            writer.print("<html lang=\"en\">" +
                    "<head>" +
                    "    <title></title>" +
                    "    <script src=\"bootstrap.bundle.min.js\"></script>" +
                    "    <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>" +
                    "<script src=\"https://cdn.jsdelivr.net/npm/chartjs-adapter-date-fns/dist/chartjs-adapter-date-fns.bundle.min.js\"></script>\n" +
                    "    <link rel=\"stylesheet\" href=\"bootstrap.min.css\">" +
                    "</head>" +
                    "<script>" +
                    "window.onload = (event) => {" +
                    "    const contextForChart1 = document.getElementById('numberOfTasksChart');" +
                    "    if (window.numberOfTasksChart && typeof window.numberOfTasksChart.destroy === 'function') {" +
                    "      window.numberOfTasksChart.destroy();" +
                    "    }" +
                    "    window.numberOfTasksChart = new Chart(contextForChart1, {" +
                    "      type: 'bar'," +
                    "      data: {" +
                    "        labels: " + dateValues + "," +
                    "        datasets: [{" +
                    "          label: '# of tasks'," +
                    "          data: " + taskCounts + "," +
                    "          borderWidth: 1" +
                    "        }]" +
                    "      }," +
                    "      options: {" +
                    "        scales: {" +
                    "        x: {type: 'time', time: {tooltipFormat: 'yyyy-MM-dd', unit: 'day',}, title: {display: true, text: 'Date'}}," +
                    "          y: {beginAtZero: true, title: {display: true, text: 'Count'}}" +
                    "        }" +
                    "      }" +
                    "    });" +
                    "    const contextForChart2 = document.getElementById('minutesLoggedByUsersChart');" +
                    "    if (window.minutesLoggedByUsersChart && typeof window.minutesLoggedByUsersChart.destroy === 'function') {" +
                    "      window.minutesLoggedByUsersChart.destroy();" +
                    "    }" +
                    "    window.minutesLoggedByUsersChart = new Chart(contextForChart2, {" +
                    "      type: 'bar'," +
                    "      data: {" +
                    "        labels: " + usernames + "," +
                    "        datasets: [{" +
                    "          label: '# of minutes'," +
                    "          data: " + timeLoggedCounts + "," +
                    "          borderWidth: 1" +
                    "        }]" +
                    "      }," +
                    "      options: {" +
                    "        scales: {" +
                    "        x: {title: {display: true, text: 'Username'}}," +
                    "        y: {beginAtZero: true, title: {display: true, text: 'Minutes logged'}}" +
                    "        }" +
                    "      }" +
                    "    });" +
                    "  }" +
                    "</script>" +
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
                    "    <h2>Number of tasks per day by deadline</h2>" +
                    "    <canvas id=\"numberOfTasksChart\"></canvas>" +
                    "    <h2>Number of minutes logged by user</h2>" +
                    "    <canvas id=\"minutesLoggedByUsersChart\"></canvas>" +
                    "</body>" +
                    "</html>");
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
