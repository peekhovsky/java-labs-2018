package com.company.user.test;

import com.company.person.Person;
import com.company.person.Persons;
import com.company.user.User;
import com.company.user.Users;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class UsersTest {

    @org.junit.Test
    public void checkReadAndWrite() {
        Users users1 = new Users("Users.xml");
        assertTrue(users1.write("UsersTest.xml"));
        Users users2 = new Users("UsersTest.xml");
        Iterator<User> iterator1 = users1.iterator();
        Iterator<User> iterator2 = users2.iterator();

        while (iterator1.hasNext() && iterator2.hasNext()){
            assertTrue(iterator1.next().equals(iterator2.next()));
        }

        assertTrue(!iterator1.hasNext());
        assertTrue(!iterator2.hasNext());
    }
}