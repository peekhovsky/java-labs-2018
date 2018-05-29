package user;

import booking.BookedDates;
import booking.UserBookedDates;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

	public Integer id;
	public String login;
	public String hashPassword;
	public String password;
	public String type;
	public String number;

	public UserBookedDates bookedDates;

   /**This constructor just creates new empty prototype of this class*/
	public User() {
        this.type = null;
		this.id = null;
		this.login = null;
		this.hashPassword = null;
		this.password = null;
		bookedDates = new UserBookedDates();
	}

	public User(String login, String password) {
        this.type = null;
		this.id = null;
		this.login = login;
		this.hashPassword = null;
		this.password = password;
		bookedDates = new UserBookedDates();
	}

	public User(String login, String type, String hashPassword) {
		this.id = null;
		this.login = login;
		this.type = type;
		this.hashPassword = hashPassword;
		System.out.println(hashPassword);
		bookedDates = new UserBookedDates();
	}
   
	public void show() {
		System.out.println("Id: " + id);
		System.out.println("Login: " + login);
	    System.out.print("Type: " + type);
	    System.out.println("\nHash Password: " + hashPassword);
	    System.out.println();
	}

	public void compileHashPassword() {
		hashPassword = Password.getHash(password);
		password = null;
	}
	
	@Override
	public String toString() {
		return login;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof User) {
			User otherUser = (User) object;
			if (!this.login.equals(otherUser.login)) {
				return false;
			}
			if (!this.hashPassword.equals(otherUser.hashPassword)) {
				return false;
			}
			if (!this.type.equals(otherUser.type)) {
				return false;
			}
			if (!this.id.equals(otherUser.id)) {
				return false;
			}
			return true;
		}
		return false;
	}

	public String getLogin() {
		return login;
	}

	public Integer getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
