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

@WebServlet ("/SignUp.do")
public class SignUp extends HttpServlet {

    final String USERNAME_USERS = "root";
    final String PASSWORD_USERS = "stevegates1";
    final String CONNECTION_URL_USERS = "jdbc:mysql://localhost:3306/users";

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
        Hotel hotel = new Hotel("root", "stevegates1", "jdbc:mysql://localhost:3306/hotels");
        hotel.readHotel("Hotel California");
        request.setAttribute("hotel_name", hotel.getName());
        request.setAttribute("hotel_stars", hotel.getStars());

        request.setAttribute("hotel_name", hotel.getName());
        request.setAttribute("hotel_stars", hotel.getStars());
        request.getRequestDispatcher("/jsp/registration.jsp").forward(request, response);
    }
}
