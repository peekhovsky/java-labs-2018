package main;

import javafx.collections.ObservableList;
import main.Warehouse;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class WarehouseRecorder {

    /**That method reads data from existing file located in computer.
     * Then he adds new  children nodes to rootNode.
     * Uses recursion with the help of readRecursion method.
     * @param warehouse list you want to fulfill
     * @param location file with description of a tree location
     * @exception IOException happens if there is no tree in adjusted location*/
    public static void read(Warehouse warehouse, String location) throws IOException {

        FileReader fileReader = new FileReader(location);
        Scanner scanner = new Scanner(fileReader);

        ObservableList<String> observableList = warehouse.getObservableList();

        while(scanner.hasNextLine()) {
            String str = scanner.nextLine();
            warehouse.add(str);
        }
        scanner.close();
        fileReader.close();
    }

    /**Is used to write complete tree to file. Uses recursion (by writeRecursion).
     * @param warehouse list you want to write down
     * @param location location where your file will be
     * @exception IOException happens when method cannot access to adjusted location*/
    public static void write(Warehouse warehouse, String location) throws IOException {

        FileWriter fileWriter = new FileWriter(location, false);

        ObservableList<String> observableList = warehouse.getObservableList();

        for (String str : observableList) {
            fileWriter.write(str + "\n");
        }
        fileWriter.close();
    }
}
