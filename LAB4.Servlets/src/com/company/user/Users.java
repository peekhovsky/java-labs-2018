package com.company.user;

import java.io.Serializable;
import java.util.ArrayList;

public class Users extends ArrayList<User> implements Serializable {

	public Integer lastId;
	private String location;

	//constructors
	public Users(String location) {
		this.location = location;
		read();
	}
	
	public Users() {
		this.lastId = null;
		this.location = null;
	}
	
	//public methods
	public void read() {
		UserIO.read(location, this);
	}
	
	public boolean write() {
		return UserIO.write(location, this);
	}

	public boolean write(String location) {
		return UserIO.write(location, this);
	}
	
	public void show() {
		for (User user : this) {
			System.out.println("\nUsers (Last id = " + lastId + ")");
			user.show();
		}
	}
	
	public void deleteUser(int index) {
		if (index < this.size()) {
			this.remove(index);
		}
	}
	
	public User getUser(int index) {
		if (index < this.size()) {
			return this.get(index);
		} 
		return null;
	}
	
	public void addUser(String name, ArrayList<String> accessTags, String password) {
        User user = new User(name, accessTags, Password.getHash(password));
        user.id = null;
		this.add(user);
	}

	public boolean addUser(User user) {
		if (findUser(user.login) != null) {
			return false;
		}
		lastId++;
		user.id = lastId;
		this.add(user);
		return true;
	}
	
	public User checkUser(String login, String password) {
		User user = findUser(login);
		if (user == null) {
			return null;
		}
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
}
