package booking;

import java.util.Date;

public class DateAndLogin {
    public Date date;
    public String login;
    public DateAndLogin(Date date, String login) {
        this.date = date;
        this.login = login;
    }

    public Date getDate() {
        return date;
    }

    public String getLogin() {
        return login;
    }
}
