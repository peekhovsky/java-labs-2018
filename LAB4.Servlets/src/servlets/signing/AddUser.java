package servlets.signing;

import hotel.Hotel;
import user.User;
import user.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/AddUser.do")
public class AddUser extends HttpServlet {

    final String USERNAME_USERS = "root";
    final String PASSWORD_USERS = "stevegates1";
    final String CONNECTION_URL_USERS = "jdbc:mysql://localhost:3306/users";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Users users = new Users(USERNAME_USERS,PASSWORD_USERS, CONNECTION_URL_USERS);
        users.readUsers();
        Hotel hotel = new Hotel("root", "stevegates1", "jdbc:mysql://localhost:3306/hotels");
        hotel.readHotel("Hotel California");
        users.readUsers();

        req.setAttribute("hotel_name", hotel.getName());
        req.setAttribute("hotel_stars", hotel.getStars());

        //get password
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String repeatPassword = req.getParameter("repeatPassword");

        if (users.findUser(login) != null) {
            req.setAttribute("status", "This login is already used.");
            req.getRequestDispatcher("/jsp/registration.jsp").forward(req,resp);
        }
        if (!password.equals(repeatPassword)) {
            req.setAttribute("status", "Passwords do not match.");
            req.getRequestDispatcher("/jsp/registration.jsp").forward(req, resp);
        }

        users.addUser(login, "user", password);
        users.updateUsers();

        req.setAttribute("title", "Done!");
        req.setAttribute("message", "You have been registered!");
        req.getRequestDispatcher("jsp/info.jsp").forward(req, resp);
    }
}
