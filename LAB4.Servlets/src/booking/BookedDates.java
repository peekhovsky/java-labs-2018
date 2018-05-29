package booking;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BookedDates extends ArrayList<DateAndLogin> {

    public boolean addDate(Date date, String login) {
        Date currentDate = new Date();
        if (currentDate.before(date)) {
            add(new DateAndLogin(date, login));
            return true;
        } else {
            return false;
        }
    }

    public boolean checkDate(Date date) {
        for (DateAndLogin dateAndLogin : this) {
            if (dateAndLogin.date.equals(date)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkDates(Date startDate, Date finishDate) {
        for (DateAndLogin dateAndLogin : this) {
            Date date = (Date) startDate.clone();
            while (date.before(finishDate)) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                date = c.getTime();
                if (dateAndLogin.date.equals(date)) {
                    return false;
                }
            }
            if (dateAndLogin.date.equals(date)) {
                return false;
            }
        }
        return true;
    }

    public boolean addPeriod(Date startDate, Date finishDate, String login) {
        Date date = (Date) startDate.clone();

        if (checkDates(startDate, finishDate)) {
            while (date.before(finishDate)) {
                this.add(new DateAndLogin(date, login));
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                date = c.getTime();
            }
            this.add(new DateAndLogin(date, login));
            updateDates();
            return true;
        } else {
            return false;
        }
    }

    public void removeDate(Date date, String login) {
        this.remove(new DateAndLogin(date, login));
    }
    public void removeDate(Date date) {
        for (DateAndLogin dateAndLogin : this) {
            if (dateAndLogin.date.equals(date)) {
                this.remove(dateAndLogin);
                return;
            }
        }
    }

    public void updateDates() {
        Date currentDate = new Date();
        for (DateAndLogin dateAndLogin : this) {
            if (currentDate.after(dateAndLogin.date)) {
                this.remove(dateAndLogin);
            }
        }
    }

    public ArrayList<DateAndLogin> getBookedDates() {
       return new ArrayList<>(this);
    }

}
