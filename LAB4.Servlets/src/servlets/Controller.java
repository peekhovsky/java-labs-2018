package servlets;

import hotel.Hotel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/")
public class Controller extends HttpServlet {

    private boolean reverseSorting = true;
    private boolean reverseSortById = true;
    private boolean reverseSortByAmountOfBeds = true;
    private boolean reverseSortByPrice = true;

    private DateFormat dateFormatHTML;

    private Hotel hotel;

    @Override
    public void init() throws ServletException {

        super.init();
    }

    @Override
    public void destroy() {
        hotel.close();
        System.out.println("Destroying...");
    }

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
        System.out.println("Initialization...");
        dateFormatHTML = new SimpleDateFormat("yyyy-MM-dd");
        hotel = new Hotel("root", "stevegates1", "jdbc:mysql://localhost:3306/hotels");
        hotel.readHotel("Hotel California");
        hotel.show();

        request.setAttribute("hotel_name", hotel.getName());
        request.setAttribute("min_date", dateFormatHTML.format(new Date()));
        request.setAttribute("max_date", "2019-01-01");
        if (request.getParameter("room_sorting") != null) {
            switch (request.getParameter("room_sorting")) {
                case "id": {
                    reverseSortById = !reverseSortById;
                    reverseSorting = reverseSortById;
                    break;
                }
                case "amountOfBeds": {
                    reverseSortByAmountOfBeds = !reverseSortByAmountOfBeds;
                    reverseSorting = reverseSortByAmountOfBeds;
                    break;
                }
                case "pricePerDay": {
                    reverseSortByPrice = !reverseSortByPrice;
                    reverseSorting = reverseSortByPrice;
                    break;
                }
            }
        }
        request.setAttribute("hotel_rooms", Hotel.sortRooms(hotel.getRooms(), reverseSorting, request.getParameter("room_sorting")));
        request.setAttribute("hotel_stars", hotel.getStars());
        request.getRequestDispatcher("/jsp/hotel.jsp").forward(request, response);
    }
}
