package booking;

import hotel.Room;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class DateAndRoom {
    public Date date;
    public Room room;

    public DateAndRoom(Date date, Room room) {
        this.date = date;
        this.room = room;
    }

    public Date getDate() {
        return date;
    }

    public Room getRoom() {
        return room;
    }
}
