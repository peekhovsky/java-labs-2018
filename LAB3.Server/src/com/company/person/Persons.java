package com.company.person;

import java.io.Serializable;
import java.util.ArrayList;

/**Extended ArrayList special for persons.
 * @author Rostislav Pekhovsky 2018
 * @version 0.1
 * @see Person
 * @see PersonsIO*/
public class Persons extends ArrayList<Person> implements Serializable {

	/**location with xml file with a list of persons*/
	private String location;
	/**last id */
	public Integer lastId;

	/**Constructor
	 * @param location location with xml-file with a list of persons*/
	public Persons(String location) {
		this.location = location;
		read();
	}

	/**reads data from xml-file*/
	private boolean read() {
		return PersonsIO.read(location, this);
	}

	/**writes data to xml-file*/
	public boolean write() {
		return PersonsIO.write(location, this);
	}

	/**writes data to xml-file
	 * @param location location of new file*/
	public boolean write(String location) {
		return PersonsIO.write(location, this);
	}

	/**to output information to console*/
	public void show() {
		for (Person person : this) {
			person.show();
		}
	}

	/**to delete person by index*/
	public void deletePerson(int index) {
		if (index < this.size()) {
			this.remove(index);
			this.trimToSize();
			show();
		}
	}

	/**to change (update) person by id*/
	public void setPerson(Person newPerson) {
		System.out.println("new: " + newPerson.id);
		for (Person person : this) {
			System.out.println("old: " + person.id);
			if (person.id.equals(newPerson.id)) {
				person.update(newPerson);
				System.out.println("updated.");
			}
		}
	}

	/**to add new person to list*/
	public void addPerson(Person person) {
		lastId++;
		person.id = lastId;
		this.add(person);
	}

	/**to get a preliminary list of persons (only given names, surnames, middle names)*/
	public ArrayList<String> getList() {
		ArrayList<String> names = new ArrayList<>();
		for (Person person : this) {
			names.add(person.surname + " " + person.givenName + " " + person.middleName);
		}
		return names;
	}
}
