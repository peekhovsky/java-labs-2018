package user;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UserDatabase {

    static public DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private String username;
    private String password;
    private String connectionURL;

    private Connection connection;
    private boolean isConnected;

    private Users users;

    public UserDatabase(Users users, String username, String password, String connectionURL) {
        this.username = username;
        this.password = password;
        this.connectionURL = connectionURL;
        isConnected = false;
        this.users = users;
    }
    void establishConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(connectionURL, username, password);
            System.out.println("Connected (User Database)!");
            isConnected = true;
        } catch (SQLException ex) {
            System.out.println("Error: cannot connect to database (User Database)!");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: cannot find driver (User Database)!");
            ex.printStackTrace();
        }
    }

    void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.out.println("Error: cannot close connection (User Database)!");
        }
    }

    void readUsers() {
        users.lastId = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user_table");
            while(resultSet.next()) {
                User user = new User();
                user.id = resultSet.getInt("id");
                if (user.id > users.lastId) {
                    users.lastId = user.id + 1;
                }
                user.login = resultSet.getString("login");
                user.hashPassword = resultSet.getString("hash_password");
                user.type = resultSet.getString("type");
                users.add(user);
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read this table! (read by name)");
            ex.printStackTrace();
        }
    }
    void updateUsers() {
        try {
            for (User user : users) {
                Statement statement = connection.createStatement();
                statement.executeUpdate("UPDATE user_table SET login = '"
                        + user.login + "', hash_password = '" + user.hashPassword +
                        "', type = '" + user.type + "' WHERE id = " + user.id);
            }
        } catch (SQLException ex) {
            System.out.println("Cannot read this table! (read by name)");
            ex.printStackTrace();
        }
    }
    void updateUser(User user) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE user_table SET login = '"
                    + user.login + "', hash_password = '" + user.hashPassword +
                    "', type = '" + user.type + "' WHERE id = " + user.id);
        } catch (SQLException ex) {
            System.out.println("Cannot read this table! (read by name)");
            ex.printStackTrace();
        }
    }

    public void addOrUpdateUser(User user) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM user_table" +
                    " WHERE id = " + user.id);
            statement.executeUpdate("INSERT INTO user_table VALUES " +
                    "(" + user.id +", '" + user.login + "', '"
                    + user.hashPassword + "', '" + user.type + "')");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void deleteUser(User user) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM user_table WHERE id = " + user.id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
