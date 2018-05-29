package hotel;


import booking.DateAndLogin;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HotelDatabase {

    static public DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private String username;
    private String password;
    private String connectionURL;

    private Connection connection;
    private boolean isConnected;

    private Hotel hotel;

    //--------------------------connection------------------------------//
    HotelDatabase(Hotel hotel, String username, String password, String connectionURL) {
        this.username = username;
        this.password = password;
        this.connectionURL = connectionURL;
        isConnected = false;
        this.hotel = hotel;
    }

    void establishConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
          //  Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(connectionURL, username, password);
            System.out.println("Connected!");
            isConnected = true;
        } catch (SQLException ex) {
            System.out.println("Error: cannot connect to database!");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: cannot find driver!");
            ex.printStackTrace();
        }
    }

    void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.out.println("Error: cannot close connection!");
        }
    }

    //----------------------------read-------------------------------//
    void readHotelByName(String name) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM hotel_table WHERE name like '" + name + "'");
            resultSet.next();
            hotel.setStars(resultSet.getInt("stars"));
            hotel.setName(resultSet.getString("name"));
            hotel.setCity(resultSet.getString("city"));
            hotel.setStreet(resultSet.getString("street"));
            hotel.setHouse(resultSet.getString("house"));
            hotel.setDescription(resultSet.getString("description"));
            hotel.setLastID(resultSet.getInt("lastID"));
            hotel.rooms.addAll(readRooms());

        } catch (SQLException ex) {
            System.out.println("Cannot read this table! (read by name)");
            ex.printStackTrace();
        }
    }

    private ArrayList<Room> readRooms() {
        ArrayList<Room> rooms = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM room_table "
                    + "WHERE hotel_name LIKE '" + hotel.getName() + "'");

            while (resultSet.next()) {
                Room room = new Room();
                room.setAmountOfBeds(resultSet.getInt("amountOfBeds"));
                room.setPricePerDay(resultSet.getInt("pricePerDay"));
                room.setId(resultSet.getInt("id"));
                room.setDescription(resultSet.getString("description"));
                room.getBookedDates().addAll(readDates(room.getId()));
                rooms.add(room);
                System.out.println("lll");
            }

        } catch (SQLException ex) {
            System.out.println("Cannot read this table! (read rooms)");
            ex.printStackTrace();
        }
        return rooms;
    }

    private ArrayList<DateAndLogin> readDates(Integer roomID) {

        String tableName = "res_table_" + roomID;
        System.out.println("Room id: " + roomID);
        ArrayList<DateAndLogin> datesAndLogin = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            while (resultSet.next()) {
                try {
                    datesAndLogin.add(new DateAndLogin(dateFormat.parse(resultSet.getString("date")),
                            resultSet.getString("login")));
                } catch (ParseException ex) {
                    System.out.println("Error: date parsing!");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read this table! (read dates)");
            ex.printStackTrace();
        }
        return datesAndLogin;
    }


    //----------------------------write------------------------------//
    boolean updateHotelByName() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE hotel_table SET stars = "
                    + hotel.getStars() + ", name = '" + hotel.getName() +
                    "', city = '" + hotel.getCity() + "', street = '" + hotel.getStreet() +
                    "', house = '" + hotel.getHouse() + "', description = '" + hotel.getDescription() +
                    "', lastID = '" + hotel.getLastID() + "' WHERE name = '" + hotel.getName() + "'");

            for (Room room : hotel.rooms) {
                addOrUpdateRoom(room);
            }
            return true;
        } catch (SQLException ex) {
            System.out.println("Cannot read this table! (read by name)");
            ex.printStackTrace();
            return false;
        }
    }

    public void addOrUpdateRoom(Room room) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM room_table" +
                    " WHERE hotel_name = '" + hotel.getName() + "' AND id = " + room.getId());
            statement.executeUpdate("INSERT INTO room_table VALUES " +
                    "('" + hotel.getName() +"', " + room.getAmountOfBeds() + ", "
                    + room.getPricePerDay() + ", " + room.getId() + ", '" + room.getDescription() + "')");

            addOrUpdateDate(room);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void deleteRoom(Room room) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM room_table" +
                    " WHERE hotel_name = '" + hotel.getName() + "' AND id = " + room.getId());
            deleteDateTable(room);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addOrUpdateDate(Room room) {
        String tableName = "res_table_" + room.getId().toString();
        try {
            Statement statement = connection.createStatement();
            try {
                statement.executeUpdate("DROP TABLE " + tableName);
            } catch (SQLSyntaxErrorException ex) { }
            statement.executeUpdate("CREATE TABLE "+ tableName +" (date CHAR(20), login CHAR(20))");

            for (DateAndLogin dateAndLogin : room.getBookedDates()) {
                statement.executeUpdate("INSERT INTO " + tableName +
                    " VALUES ('" + dateFormat.format(dateAndLogin.date) + "', '" + dateAndLogin.login + "')");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void deleteDateTable(Room room) {
        String tableName = "res_table_" + room.getId().toString();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE " + tableName);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void updateLastID(Hotel hotel) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE hotel_table SET  lastID = '" + hotel.getLastID()
                    + "' WHERE name = '" + hotel.getName() + "'");

            for (Room room : hotel.rooms) {
                addOrUpdateRoom(room);
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read this table! (read by name)");
            ex.printStackTrace();
        }
    }
}
