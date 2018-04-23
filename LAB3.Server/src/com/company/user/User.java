package com.company.user;


import com.company.person.Person;

import java.io.Serializable;
import java.rmi.UnexpectedException;
import java.util.ArrayList;

public class User implements Serializable {

	public Integer id;
	public ArrayList<String> accessTags;
	public String login;
	public String hashPassword;
	public String password;

   /**This constructor just creates new empty prototype of this class*/
	public User() {
        this.accessTags = new ArrayList<>();
		this.id = null;
		this.login = null;
		this.hashPassword = null;
		this.password = null;
	}	
	
	public User(String login) {
        this.accessTags = new ArrayList<>();
		this.id = null;
	    this.login = login;
	   	this.hashPassword = null;
	   	this.password = null;
	}

	public User(String login, String password) {
        this.accessTags = new ArrayList<>();
		this.id = null;
		this.login = login;
		this.hashPassword = null;
		this.password = password;
	}

	public User(String login, ArrayList<String> accessTags, String hashPassword) {
		this.id = null;
		this.login = login;
		this.accessTags = accessTags;
		this.hashPassword = hashPassword;
	}

	public void addAccessTag(String accessTag) {
		accessTags.add(accessTag);
	}
	
	public void removeAccessTag(int index) {
		if (index > 0 && index < accessTags.size()) {
			accessTags.remove(index);
		}
	}

	public boolean checkAssessTag(String checkingAccessTag) {
		for (String accessTag : accessTags) {
			if (accessTag.equals(checkingAccessTag)) {
				return true;
			}
		}
		return false;
	}
   
	public void show() {
		System.out.println("Id: " + id);
		System.out.println("Login: " + login);
	    System.out.print("Access tags: ");
	    for (String tag : accessTags) {
	    	System.out.print(tag + " ");
	    }
	    System.out.println("\nHash Password: " + hashPassword);
	    System.out.println();
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
			if (!this.accessTags.equals(otherUser.accessTags)) {
				return false;
			}
			if (!this.id.equals(otherUser.id)) {
				return false;
			}
			return true;
		}
		return false;
	}
}
