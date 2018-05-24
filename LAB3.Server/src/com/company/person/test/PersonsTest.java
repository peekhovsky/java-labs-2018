package com.company.person.test;

import com.company.person.Person;
import com.company.person.Persons;

import java.util.Iterator;

import static org.junit.Assert.*;

public class PersonsTest {

    @org.junit.Test
    public void checkReadAndWrite() {
        Persons persons1 = new Persons("List.xml", "List.xld");
        assertTrue(persons1.write("ListTest.xml"));
        Persons persons2 = new Persons("ListTest.xml","List.xld");
        Iterator<Person> iterator1 = persons1.iterator();
        Iterator<Person> iterator2 = persons2.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()){
            assertTrue(iterator1.next().equals(iterator2.next()));
        }
        assertTrue(!iterator1.hasNext());
        assertTrue(!iterator2.hasNext());
    }

    @org.junit.Test
    public void setPerson() {
    }

    @org.junit.Test
    public void addPerson() {
    }

    @org.junit.Test
    public void getList() {
    }
}