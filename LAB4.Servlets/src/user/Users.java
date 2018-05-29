package user;

import booking.BookedDates;
import booking.DateAndLogin;
import hotel.Hotel;
import hotel.Room;

import java.io.Serializable;
import java.util.ArrayList;

public class Users extends ArrayList<User> implements Serializable {

	public Integer lastId;
	private UserDatabase userDatabase;

	//constructors
	public Users(String username, String password, String connectionURL) {
		userDatabase = new UserDatabase(this, username, password, connectionURL);
		userDatabase.establishConnection();
	}


	//public methods
	public void readUsers() {
		userDatabase.readUsers();
	}
	
	public void updateUsers() {
		userDatabase.updateUsers();
	}

	public void updateUser(User user) {
		userDatabase.updateUser(user);
	}

	public void close() {
		userDatabase.closeConnection();
	}

	public void show() {
		for (User user : this) {
			System.out.println("\nUsers (Last id = " + lastId + ")");
			user.show();
		}
	}
	
	public void deleteUser(int index) {
		if (index < this.size()) {
			userDatabase.deleteUser(this.getUser(index));
			this.remove(index);

		}
	}
	
	public User getUser(int index) {
		if (index < this.size()) {
			return this.get(index);
		} 
		return null;
	}
	
	public void addUser(String name, String type, String password) {
        User user = new User(name, type, Password.getHash(password));
        user.id = lastId;
		this.add(user);
		userDatabase.addOrUpdateUser(user);
		lastId++;
	}

	public boolean addUser(User user) {
		if (findUser(user.login) != null) {
			return false;
		}
		lastId++;
		user.id = lastId;
		this.add(user);
		userDatabase.addOrUpdateUser(user);
		return true;
	}
	
	public User checkUser(String login, String password) {
		User user = findUser(login);
		if (user == null) {
			System.out.println("rrr");
			return null;
		}
		System.out.println("uyuhtt");
		if (user.hashPassword.equals(Password.getHash(password))) {
			return user;
		}
		return null;
	}
	
	public User findUser(String login) {
		for (User user : this) {
			if (user.login.equals(login)) {
				return user;
			}
		}
		return null;
	}
	
	public ArrayList<String> getList() {
		ArrayList<String> names = new ArrayList<>();
		for (User user : this) {
			names.add(user.login);
		}
		return names;
	}

	public void fillBookedDates(Hotel hotel) {
		ArrayList<Room> rooms = hotel.getRooms();
		for (Room room : rooms) {
			BookedDates bookedDates = room.getBookedDates();
			for (DateAndLogin dateAndLogin : bookedDates) {
				User user = findUser(dateAndLogin.login);
				if (user != null) {
					user.bookedDates.addDate(dateAndLogin.date, room);
					System.out.println(room.getId());
				}

			}
		}
	}

	public void removeUser(User user) {
		this.remove(user);
		userDatabase.deleteUser(user);
	}
}
