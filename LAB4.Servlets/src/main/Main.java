package main;

import hotel.Hotel;
import hotel.HotelDatabase;
import user.Password;
import user.User;
import user.Users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final String USERNAME_HOTEL = "root";
        final String PASSWORD_HOTEL = "stevegates1";
        final String CONNECTION_URL_HOTEL = "jdbc:mysql://localhost:3306/hotels";

        final String USERNAME_USERS = "root";
        final String PASSWORD_USERS = "stevegates1";
        final String CONNECTION_URL_USERS = "jdbc:mysql://localhost:3306/users";

        Hotel hotel = new Hotel(USERNAME_HOTEL, PASSWORD_HOTEL, CONNECTION_URL_HOTEL);
        hotel.readHotel("Hotel California");
        hotel.show();

        Users users = new Users(USERNAME_USERS, PASSWORD_USERS, CONNECTION_URL_USERS);
        users.readUsers();
        users.addUser(new User("pekh", "admin", Password.getHash("rost")));

        users.show();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("1 - add room, 2 - delete room," +
                        " 3 - show rooms, 4 - update table, 5 - add reserved day");
                System.out.println("6 - add user, 7 - delete user, 8 - show users, 0 - exit");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1: {
                        System.out.println("Enter an amount of beds: ");
                        Integer amountOfBeds = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Enter a price per day: ");
                        Integer pricePerDay = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Enter a description: ");
                        String description = "this is a description";

                        hotel.addRoom(amountOfBeds, pricePerDay, description);

                        break;
                    }
                    case 2: {
                        System.out.println("Enter an ID: ");
                        Integer id = scanner.nextInt();
                        if (hotel.deleteRoom(id)) {
                            System.out.println("Room has been deleted!");
                        } else {
                            System.out.println("Cannot find this room!");
                        }
                        scanner.nextLine();
                        break;
                    }
                    case 3:
                        hotel.showRooms();
                        break;
                    case 4:
                        System.out.println("Updating...");
                        if (hotel.updateDatabase()) {
                            System.out.println("Updated.");
                        } else {
                            System.out.println("Cannot update database!");
                        }
                        break;
                    case 5: {
                        System.out.println("Enter an ID: ");
                        Integer id = scanner.nextInt();
                        System.out.println("Enter date (dd.MM.yyyy): ");
                        scanner.nextLine();
                        Date date = HotelDatabase.dateFormat.parse(scanner.nextLine());

                        System.out.println("Enter login: ");
                        scanner.nextLine();
                        String login = scanner.nextLine();
                        if (hotel.addReservedDay(id, date, login)) {
                            System.out.println("This day has been reserved!");
                        } else {
                            System.out.println("Cannot reserve this day (check the id of this room)!");
                        }
                        break;
                    }
                    case 6: {
                        User user = new User();
                        System.out.println("Enter a login: ");
                        user.login = scanner.nextLine();

                        System.out.println("Enter a password: ");
                        user.password = scanner.nextLine();
                        user.compileHashPassword();

                        System.out.println("Enter a type: ");
                        user.type = scanner.nextLine();

                        users.addUser(user);
                        users.updateUsers();
                        break;
                    }
                    case 7: {
                        System.out.println("Enter an ID: ");
                        Integer id = scanner.nextInt();
                        users.deleteUser(id);
                        break;
                    }
                    case 8: {
                        users.show();
                        break;
                    }
                    case 0: {
                        System.exit(0);
                        break;
                    }
                }
            } catch (InputMismatchException ex) {
                System.out.println("Incorrect input! Try again");
                scanner.nextLine();
            } catch (ParseException ex) {
                System.out.println("Incorrect date!");
            }
        }
    }
}
