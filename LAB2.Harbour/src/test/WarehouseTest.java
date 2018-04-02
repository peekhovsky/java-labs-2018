package test;

import javafx.scene.control.ListView;
import main.Warehouse;
import org.junit.Test;


import java.util.ListIterator;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class WarehouseTest {
    @Test
    public void readWriteWarehouse(){
        Warehouse warehouse1 = new Warehouse(null);
        for (int i = 0; i < 1000; i++) {
            warehouse1.add(getRandomString());
        }
        warehouse1.save("testwarehouse.txt");

        Warehouse warehouse2 = new Warehouse(null);
        warehouse2.load("testwarehouse.txt");

        ListIterator<String> iterator1 = warehouse1.getObservableList().listIterator();
        ListIterator<String> iterator2 = warehouse2.getObservableList().listIterator();

        while (iterator1.hasNext()) {
            String str1 = iterator1.next();
            String str2 = iterator2.next();
            assertEquals(str1, str2);
        }
        assertFalse(iterator2.hasNext());
    }

    private String getRandomString(){
        char[] str;

        Random random = new Random();

        str = new char[Math.abs(random.nextInt()) % 100];

        for (int i = 0; i < str.length; i++)
            str[i] = (char)(20 + Math.abs(random.nextInt() % 100));

        return new String(str);
    }

}