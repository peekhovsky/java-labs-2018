package servlets.admin;

import hotel.Hotel;
import hotel.Room;
import user.User;
import user.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/DeleteRoom.do")
public class DeleteRoom extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Hotel hotel = new Hotel("root", "stevegates1", "jdbc:mysql://localhost:3306/hotels");
        hotel.readHotel("Hotel California");
        Users users = new Users("root",  "stevegates1", "jdbc:mysql://localhost:3306/users");
        users.readUsers();
        users.fillBookedDates(hotel);

        if (!users.findUser((String) req.getSession().getAttribute("login")).type.equals("admin")) {
            resp.sendRedirect(req.getContextPath() + "/");
        }
        hotel.deleteRoom(Integer.parseInt(req.getParameter("roomId")));

        hotel.updateDatabase();
        resp.sendRedirect(req.getContextPath() + "/");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}