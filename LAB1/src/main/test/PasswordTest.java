package main.test;

import main.java.com.explorer.Explorer;
import main.java.com.explorer.ExplorerIO;
import main.java.com.explorer.Password;
import main.java.com.explorer.UserControl;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;


public class PasswordTest {

   @NotNull
   private String getRandomString(){
        char[] str;

        Random random = new Random();

        str = new char[Math.abs(random.nextInt()) % 100];

        for(int i = 0; i < str.length; i++)
            str[i] =  (char)(20 + Math.abs(random.nextInt() % 200));

        return new String(str);
    }

    @Test
    public void readWritePassword() {
        Password password1 = new Password();

        for (int i = 0; i < 200; i++) {
            String randomStr = getRandomString();
            password1.addPassword(0, randomStr);
        }
        try {
            password1.writePassword("TestPassword.txt");
            Password password2 = new Password("TestPassword.txt");

            for (int i = 0; i < 200; i++) {
                assertEquals(password1.getHashPassword(i), password2.getHashPassword(i));
            }

        } catch (IOException exception) {
            System.out.println("PasswordTest -> SetPassword failed! (IOException)");
            fail();
        }
    }

    @Test
    public void readWriteTree() {
        Explorer explorer = new Explorer(UserControl.TypeOfUser.ADMINISTRATOR, "Tree.txt");

        try {

            ExplorerIO.write(explorer.getRootNode(), "TestTree.txt");

            FileReader fileReader1 = new FileReader("Tree.txt");
            FileReader fileReader2 = new FileReader("TestTree.txt");

            Scanner scanner1 = new Scanner(fileReader1);
            Scanner scanner2 = new Scanner(fileReader2);

            while(scanner1.hasNextLine() && scanner2.hasNextLine()) {
                String string1 = scanner1.nextLine();
                String string2 = scanner2.nextLine();
                if(!string1.equals(string2)) System.out.println("Error");
                assertEquals(string1, string2);
            }
            scanner1.close();
            scanner2.close();
            fileReader1.close();
            fileReader2.close();

        } catch (IOException exception) {
            System.out.println("PasswordTest -> SetPassword failed! (IOException)");
            fail();
        }

       // explorer.save();



    }
}