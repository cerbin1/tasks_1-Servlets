import db.UserRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Register extends HttpServlet {
    private final UserService userService;

    public Register() {
        userService = new UserService(new UserRepository());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.registerUser(request.getParameter("email"),
                request.getParameter("username"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("surname"));

        response.sendRedirect("login.html");
    }
}
