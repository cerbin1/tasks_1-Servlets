import conf.ApplicationProperties;
import db.dao.TaskFileDao;
import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.EmailSendingService;
import service.UserService;

import java.io.File;
import java.io.IOException;

import static conf.ApplicationProperties.FILE_UPLOADS_BASE_URL;

public class RemoveFile extends HttpServlet {
    private final AuthenticationService authenticationService;
    private final TaskFileDao taskFileDao;

    public RemoveFile() {
        this.taskFileDao = new TaskFileDao();
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            String filename = request.getParameter("filename");
            if (taskFileDao.removeTaskFileByName(filename)) {
                deleteFile(filename);
                response.sendRedirect(ApplicationProperties.APP_BASE_PATH + "/editTask?taskId=" + request.getParameter("taskId"));
            }
        } else {
            response.sendRedirect("authError.html");
        }
    }

    private void deleteFile(String filename) {
        String filePath = getServletContext().getRealPath(FILE_UPLOADS_BASE_URL) + File.separator + filename;
        if (!new File(filePath).delete()) {
            throw new RuntimeException("Problem when deleting file: " + filename);
        }
    }
}
