package servlets.admin;

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

@WebServlet("/AdminAccess.do")
public class AdminAccess extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Hotel hotel = new Hotel("root", "stevegates1", "jdbc:mysql://localhost:3306/hotels");
        hotel.readHotel("Hotel California");
        hotel.show();
        Users users = new Users("root",  "stevegates1", "jdbc:mysql://localhost:3306/users");
        users.readUsers();
        users.fillBookedDates(hotel);
        users.readUsers();

        if (!users.findUser((String) req.getSession().getAttribute("login")).type.equals("admin")) {
            resp.sendRedirect(req.getContextPath() + "/");
        }
        User user = users.findUser(req.getParameter("login"));
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        if (req.getParameter("doing").equals("add")) {
            user.type = "admin";
            users.updateUser(user);
        } else if (req.getParameter("doing").equals("delete")) {
            user.type = "user";
            users.updateUser(user);
        }
        System.out.println("User id: " + user.id);
        System.out.println("User login: " + user.login);

        users.show();
        resp.sendRedirect(req.getContextPath() + "/users");
    }
}
