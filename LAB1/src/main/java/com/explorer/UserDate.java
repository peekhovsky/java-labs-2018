package main.java.com.explorer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class UserDate {

    public Date date;
    public int usingBytes;

    void read(String location) {

        try {
            FileReader reader = new FileReader(location);
            Scanner scanner = new Scanner(reader);
            DateFormat dateFormat = new SimpleDateFormat("DDD.yyyy");

            if(!scanner.hasNextLine()) System.out.println("Error!");
            String str = scanner.nextLine();
            System.out.println(str);
            date = dateFormat.parse(str);

            System.out.println(date);

            usingBytes = scanner.nextInt();

            scanner.close();
            reader.close();

        } catch (ParseException | IOException ex) {
            System.out.println("IO | Parse Exception!");
            date = null;
            usingBytes = 0;
        }
    }


    void write(String location){
        try {
            FileWriter writer = new FileWriter(location);
            DateFormat dateFormat = new SimpleDateFormat("DDD.yyyy");
            writer.write(dateFormat.format(date));
            writer.write("\n" + Integer.toString(usingBytes));

            writer.close();

        } catch(IOException ex){
            System.out.println("IO Exception");
        }

    }

}
