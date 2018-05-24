package com.company.server;

import java.util.NoSuchElementException;
import java.util.Scanner;

import com.company.person.*;
import com.company.user.*;


public class Server {

	public static void main(String[] args) {
		
		Persons persons = new Persons("List.xml", "List.xld");
		if (!persons.read()) {
			System.out.println("Error! Cannot read file!");
			return;
		}

		Users users = new Users("Users.xml");
		
		ServerCore serverCore = new ServerCore(7789, persons, users);
		
		Scanner scanner = new Scanner(System.in);

		serverCore.runServer();

		while (true) {
			System.out.println("Enter command: (*help* to see a list of commands)");
			String command;
			
			command = scanString(scanner);
			
			switch (command) {
			case "run":
				serverCore.runServer();
				break;

			case "stop":
				serverCore.close();
				break;

			case "help":
				System.out.println("run, stop, exit");
				System.out.println("show database, save database, add person, delete person");
				System.out.println("show users, save user, add users, delete users");
				break;

			case "show database":
				persons.show();
				break;
			
			case "save database":
				persons.write();
				break;
				
			case "add person":
				
				break;
				
			case "delete person":
				break;
				
			case "show users":
				users.show();
				break;
			
			case "save users":
				users.write();
				break;
				
			case "add user":
				System.out.println("Enter a login for new user: ");
				String login = scanString(scanner);

				System.out.println("Enter a password for new user: ");
				String password = scanString(scanner);

				//System.out.println("Enter an access tag for this user (observer, changer, admin): ");
				//String tag = scanString(scanner);
				//User user = new User(login, tag, Password.getHash(password));
				//users.addUser(user);
				break;
				
			case "delete user":
				break;
				
			case "exit":
			case "quit":
			case "0":
				scanner.close();
				serverCore.close();
				System.exit(0);
				break;
				
			default:
				System.out.println("Wrong command.");
				break;
			}		
		}
	}

	public static String scanString(Scanner scanner) {
		String string;
		do {
			try {
				string = scanner.nextLine();
				break;
			} catch (NoSuchElementException ex) { }
		} while (true);
		return string;
	}
}