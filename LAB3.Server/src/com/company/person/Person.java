package com.company.person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**@author Rostislav Pekhovsky 2018
 * @version 0.1
 * @see java.io.Serializable
 * @see Persons*/
public class Person implements Serializable {

	/**persons personal id (must be different for all persons)*/
	public Integer id;
	/**given name of person*/
	public String givenName;
	/**middle name of person*/
	public String middleName;
	/**surname of person*/
	public String surname;
	/**date of birth of person*/
	public Date dateOfBirth;
	/**cell phone of person*/
	public String cellPhone;
	/**experience of person*/
	public String experience;
	/**a list with skills of person*/
	public ArrayList<String> skills;

	/**Constructor*/
	public Person() {
		skills = new ArrayList<>();
		givenName = " ";
		middleName = " ";
		surname = " ";
		id = null;
	}

	/**to show person in console*/
	public void show() {
		System.out.println("\nPerson");
		System.out.println("ID: " + id);
		System.out.println("Given name: " + givenName);
		System.out.println("Middle name: " + middleName);
		System.out.println("Surname: " + surname);
		System.out.println("Date of birth: " + dateOfBirth);
		System.out.println("Cell phone: " + cellPhone);
		System.out.println("Experience: " + experience);

		System.out.print("Skills: ");
		for (String skill : skills) {
			System.out.print(" -" + skill);
		}
		System.out.println(".");
		
		System.out.println("\n");
	}

	/**to add new skill to person*/
	public void addSkill(String skill) {
		skills.add(skill);
	}
	/**to remove skill*/
	public void removeSkill(int index) {
		skills.remove(index);
	}

	/**to update data of person using another person (without id)*/
	public void update(Person person) {
		this.givenName = person.givenName;
		this.middleName = person.middleName;
		this.surname = person.surname;
		this.dateOfBirth = person.dateOfBirth;
		this.cellPhone = person.cellPhone;
		this.experience = person.experience;
		this.skills = person.skills;
	}

	/**to check if person has not got name and surname*/
	public boolean isEmpty() {

		return  (surname.replaceAll(" ", "").isEmpty()) &&
				(givenName.replaceAll(" ", "").isEmpty());
	}

	@Override
	public String toString() {
		return givenName + surname;
	}

	@Override
	public boolean equals(Object object){
		if (object instanceof Person) {
			Person otherPerson = (Person) object;
			if (!this.givenName.equals(otherPerson.givenName)) {
				return  false;
			}
			if (!this.middleName.equals(otherPerson.middleName)) {
				return  false;
			}
			if (!this.surname.equals(otherPerson.surname)) {
				return  false;
			}
			if (!this.dateOfBirth.equals(otherPerson.dateOfBirth)) {
				return  false;
			}
			if (!this.experience.equals(otherPerson.experience)) {
				return  false;
			}
			if (!this.cellPhone.equals(otherPerson.cellPhone)) {
				return  false;
			}
			if (!this.skills.equals(otherPerson.skills)) {
				return  false;
			}
			return true;
		}
		return false;
	}
}