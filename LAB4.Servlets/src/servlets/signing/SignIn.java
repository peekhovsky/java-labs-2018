package servlets.signing;

import com.sun.net.httpserver.HttpServer;
import user.User;
import user.UserDatabase;
import user.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class SignIn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }


    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Users users = new Users("root","stevegates1", "jdbc:mysql://localhost:3306/users");
        users.readUsers();

        //get password
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        HttpSession session = request.getSession(true);
        System.out.println("Login: " + login);
        User user = users.checkUser(login, password);

        if (user != null) {
            session.setAttribute("login", user.login);
            session.setAttribute("type", user.type);
            request.getRequestDispatcher("/").forward(request, response);
        } else {
            session.setAttribute("login","incorrect");
            session.setAttribute("type", "incorrect");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
