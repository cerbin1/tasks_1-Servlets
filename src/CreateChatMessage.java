import db.dao.ChatMessageDao;
import db.dao.UserActivationLinkDao;
import db.dao.UserDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthenticationService;
import service.ChatMessageService;
import service.EmailSendingService;
import service.UserService;

import java.io.IOException;

import static conf.ApplicationProperties.APP_BASE_PATH;

public class CreateChatMessage extends HttpServlet {
    private final AuthenticationService authenticationService;
    private final ChatMessageService chatMessageService;

    public CreateChatMessage() {
        this.chatMessageService = new ChatMessageService(new ChatMessageDao());
        this.authenticationService = new AuthenticationService(new UserService(new UserDao(), new UserActivationLinkDao(), new EmailSendingService()));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.authenticate(request)) {
            String messageContent = request.getParameter("messageContent");
            String taskId = request.getParameter("taskId");
            String userId = (String) request.getSession().getAttribute("userId");
            chatMessageService.createChatMessage(userId, taskId, messageContent);
            response.sendRedirect(APP_BASE_PATH + "/details?taskId=" + taskId);
        } else {
            response.sendRedirect("authError.html");
        }
    }
}
