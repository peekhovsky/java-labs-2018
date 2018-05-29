package servlets.user;

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


@WebServlet("/book")
public class Book extends HttpServlet {

    private DateFormat dateFormatHTML = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Hotel hotel = new Hotel("root", "stevegates1", "jdbc:mysql://localhost:3306/hotels");
        hotel.readHotel("Hotel California");
        Users users = new Users("root",  "stevegates1", "jdbc:mysql://localhost:3306/users");
        users.readUsers();
        users.fillBookedDates(hotel);
        try {
            users.updateUsers();
            HttpSession session = req.getSession();
            if (session.getAttribute("login") != null) {
                User user = users.findUser((String) session.getAttribute("login"));
                if (user == null) {
                    resp.sendRedirect(req.getContextPath() + "/");
                    return;
                }
                Date arrivalDate = dateFormatHTML.parse(req.getParameter("arrival_date"));
                Date leavingDate = dateFormatHTML.parse(req.getParameter("leaving_date"));
                Integer id = Integer.parseInt(req.getParameter("roomId"));
                if (arrivalDate.after(leavingDate)) {
                    req.setAttribute("status", "incorrect_date");
                } else {
                    req.setAttribute("status", "correct");
                    Room room = hotel.getRoom(id);
                    room.getBookedDates().addPeriod(arrivalDate, leavingDate, user.login);
                    hotel.updateDatabase();
                    user.bookedDates.addPeriod(arrivalDate, leavingDate, room);
                }
                req.setAttribute("hotel_name", hotel.getName());
                req.setAttribute("hotel_stars", hotel.getStars());
                req.getRequestDispatcher("/jsp/book_status.jsp").forward(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/");
            }
        } catch (ParseException ex) {
            System.out.println("Error: parse exception (Book)!");
            ex.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
