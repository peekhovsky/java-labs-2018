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
import java.io.IOException;

@WebServlet("/RoomBookedDates.do")
public class RoomBookedDates extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        Hotel hotel = new Hotel("root", "stevegates1", "jdbc:mysql://localhost:3306/hotels");
        hotel.readHotel("Hotel California");
        Users users = new Users("root",  "stevegates1", "jdbc:mysql://localhost:3306/users");
        users.readUsers();
        users.fillBookedDates(hotel);
        users.updateUsers();

        if (!users.findUser((String) req.getSession().getAttribute("login")).type.equals("admin")) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        Room room = null;

        if (req.getParameter("roomId") == null) {
            if (req.getAttribute("roomId") != null) {
                room = hotel.getRoom(Integer.parseInt((String) req.getAttribute("roomId")));
            }
        } else {
            room = hotel.getRoom(Integer.parseInt(req.getParameter("roomId")));
        }
        if (room == null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.setAttribute("hotel_name", hotel.getName());
        req.setAttribute("hotel_stars", hotel.getStars());
        req.setAttribute("roomId", room.getId());
        req.setAttribute("datesAndLogins", room.getBookedDates());
        req.getRequestDispatcher("/jsp/booked_dates.jsp").forward(req, resp);
    }
}