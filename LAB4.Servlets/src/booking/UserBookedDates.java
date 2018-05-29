package booking;

import hotel.Room;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

public class UserBookedDates extends ArrayList<DateAndRoom> {
    public boolean addDate(Date date, Room room) {
        Date currentDate = new Date();
        if (currentDate.before(date)) {
            add(new DateAndRoom(date, room));
            updateDates();
            return true;
        } else {
            return false;
        }
    }
    public boolean addPeriod(Date startDate, Date finishDate, Room room) {
        if (!checkDates(startDate, finishDate)) {
            return false;
        } else {
            Date date = (Date) startDate.clone();
            while (date.before(finishDate)) {
                this.add(new DateAndRoom(date, room));
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                date = c.getTime();
            }
            if (date.equals(finishDate)) {
                this.add(new DateAndRoom(date, room));
            }
            updateDates();
            return true;
        }
    }
    public boolean checkDate(Date date) {
        for (DateAndRoom dateAndRoom : this) {
            if (dateAndRoom.date.equals(date)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkDates(Date startDate, Date finishDate) {
        for (DateAndRoom dateAndRoom : this) {
            Date date = (Date) startDate.clone();
            while (date.before(finishDate)) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                date = c.getTime();
                if (dateAndRoom.date.equals(date)) {
                    return false;
                }
            }
            if (dateAndRoom.date.equals(date)) {
                return false;
            }
        }
        System.out.println("aaa");
        return true;
    }
    public void removeDate(Date date, String login) {
        this.remove(new DateAndLogin(date, login));
    }

    public void updateDates() {
        Date currentDate = new Date();
        for (DateAndRoom dateAndRoom : this) {
            if (currentDate.after(dateAndRoom.date)) {
                this.remove(dateAndRoom);
            }
        }
    }

    public ArrayList<DateAndRoom> getReservedDates() {
        return new ArrayList<>(this);
    }

}
