package hotel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Hotel {
    ArrayList<Room> rooms;

    private Integer stars;
    private String name;
    private String city;
    private String street;
    private String house;
    private String description;
    private Integer lastID;
    private HotelDatabase hotelDatabase;

    //Constructors
    public Hotel(String username, String password, String connectionURL) {
        hotelDatabase = new HotelDatabase(this, username, password, connectionURL);
        rooms = new ArrayList<>();
        hotelDatabase.establishConnection();
    }

    public void readHotel(String name) {
        hotelDatabase.readHotelByName(name);
    }

    public void addRoom(Integer amountOfBeds, Integer price, String description) {
        Room room = new Room(amountOfBeds, price, lastID);
        room.setDescription(description);
        rooms.add(room);
        hotelDatabase.addOrUpdateRoom(room);
        lastID++;
        hotelDatabase.updateLastID(this);
    }

    public boolean deleteRoom(Integer id) {
        for (Room room : rooms) {
            if (room.getId().equals(id)) {
                rooms.remove(room);
                hotelDatabase.deleteRoom(room);
                return true;
            }
        }
        return false;
    }

    public boolean updateDatabase() {
        lastID = 0;
        for (Room room : rooms) {
            if(room.getId() > lastID) {
                lastID = room.getId() + 1;
            }
        }
        return hotelDatabase.updateHotelByName();
    }

    public void show() {
        System.out.println("-----Hotel-----");
        System.out.println("Name: " + name);
        System.out.println("Stars: " + stars);
        System.out.println("City: " + city);
        System.out.println("Street: " + street);
        System.out.println("House: " + house);
        System.out.println("Description: " + description);
        System.out.println("LastID: " + lastID);
    }

    public void showRooms() {
        for (Room room : rooms) {
            room.show();
        }
    }

    public boolean addReservedDay(Integer roomId, Date date, String login) {
        for (Room room : rooms) {
            if (roomId.equals(room.getId())) {
                if(room.getBookedDates().addDate(date, login)) {
                    hotelDatabase.addOrUpdateRoom(room);
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteReservedDay(Integer roomId, Date date, String login) {
        for (Room room : rooms) {
            if (roomId.equals(room.getId())) {
                room.getBookedDates().removeDate(date, login); {
                    hotelDatabase.addOrUpdateRoom(room);
                    return;
                }
            }
        }
    }

    public void close() {
        hotelDatabase.closeConnection();
    }

    //getters - setters
    public Integer getStars() {
        return stars;
    }
    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }
    public void setHouse(String house) {
        this.house = house;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLastID() {
        return lastID;
    }
    public void setLastID(Integer lastID) {
        this.lastID = lastID;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
    public Room getRoom(Integer id) {
        for (Room room : rooms) {
            if (room.getId().equals(id)){
                return room;
            }
        }
        return null;
    }
    public static ArrayList<Room> sortRooms(ArrayList<Room> rooms, boolean reverse, String field) {

        if (field == null) {
            return rooms;
        }

        Comparator<Room> StuNameComparator = (room1, room2) -> {
            Integer id1 = room1.getId();
            Integer id2 = room2.getId();
            return id1.compareTo(id2);
        };
        switch (field) {
            case "id": {
                rooms.sort((room1, room2) -> {
                    Integer id1 = room1.getId();
                    Integer id2 = room2.getId();
                    if (reverse) {
                        return id2.compareTo(id1);
                    }
                    return id1.compareTo(id2);
                });
                break;
            }
            case "amountOfBeds": {
                rooms.sort((room1, room2) -> {
                    Integer a1 = room1.getAmountOfBeds();
                    Integer a2 = room2.getAmountOfBeds();
                    if (reverse) {
                        return a2.compareTo(a1);
                    }
                    return a1.compareTo(a2);
                });
                break;
            }
            case "pricePerDay": {
                rooms.sort((room1, room2) -> {
                    Integer p1 = room1.getPricePerDay();
                    Integer p2 = room2.getPricePerDay();
                    if (reverse) {
                        return p2.compareTo(p1);
                    }
                    return p1.compareTo(p2);
                });
                break;
            }
        }
        return rooms;
    }
}
