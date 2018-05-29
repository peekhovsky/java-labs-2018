package servlets.user;

import hotel.Hotel;
import user.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/DeleteBooking.do")
public class DeleteBooking extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Hotel hotel = new Hotel("root", "stevegates1", "jdbc:mysql://localhost:3306/hotels");
        hotel.readHotel("Hotel California");
        Users users = new Users("root",  "stevegates1", "jdbc:mysql://localhost:3306/users");
        users.readUsers();
        users.fillBookedDates(hotel);
        String type = req.getParameter("type");
        Integer roomId = null;

        try {
            roomId = Integer.parseInt(req.getParameter("roomId"));
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date date = dateFormat.parse(req.getParameter("date"));

            System.out.println("Room id: " + roomId);
            System.out.println("Date: " + dateFormat.format(date));
            hotel.getRoom(roomId).getBookedDates().removeDate(date);
        } catch (ParseException ex) {
            resp.sendRedirect(req.getContextPath() + "/booked");
            ex.printStackTrace();
        }
        hotel.updateDatabase();
        if (type.equals("admin")) {
            req.setAttribute("roomId", roomId);
            req.getRequestDispatcher("/RoomBookedDates.do").forward(req,resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/booked");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}