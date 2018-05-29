package hotel;

import booking.BookedDates;

public class Room {
    private Integer id;
    private Integer amountOfBeds;
    private Integer pricePerDay;
    private String description;
    private BookedDates bookedDates;

    //Constructors
    public Room() {
        this.id = null;
        this.amountOfBeds = null;
        this.pricePerDay = null;
        this.description = null;
        this.bookedDates = new BookedDates();
    }
    public Room(Integer amountOfBeds, Integer pricePerDay, Integer id) {
        this.id = id;
        this.amountOfBeds = amountOfBeds;
        this.pricePerDay = pricePerDay;
        this.description = "*description of this room*";
        this.bookedDates = new BookedDates();
    }

    public void show() {
        System.out.println("-----Room-----");
        System.out.println("ID: " + id);
        System.out.println("Amount of beds: " + amountOfBeds);
        System.out.println("Price per day: " + pricePerDay);
        System.out.println("Description: " + description);
    }

    //Getters - Setters
    public Integer getAmountOfBeds() {
        return amountOfBeds;
    }
    public void setAmountOfBeds(Integer amountOfBeds) {
        this.amountOfBeds = amountOfBeds;
    }
    public Integer getPricePerDay() {
        return pricePerDay;
    }
    public void setPricePerDay(Integer pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public BookedDates getBookedDates() {
        return bookedDates;
    }

    public void setReservedDates(BookedDates reservedDates) {
        this.bookedDates = reservedDates;
    }


}
