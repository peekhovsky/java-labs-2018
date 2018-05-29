package servlets.admin;

import hotel.Hotel;
import user.User;
import user.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/DeleteUser.do")
public class DeleteUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
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

        users.removeUser(user);
        users.updateUsers();

        resp.sendRedirect(req.getContextPath() + "/users");
    }
}
