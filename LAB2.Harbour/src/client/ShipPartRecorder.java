package client;

import javafx.collections.ObservableList;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import main.*;

public class ShipPartRecorder {

    /**That method reads data from existing file located in computer.
     * Then he adds new  children nodes to rootNode.
     * Uses recursion with the help of readRecursion method.
     * @param shipPark list you want to fulfill
     * @param location file with description of a tree location
     * @exception IOException happens if there is no tree in adjusted location*/
    public static void read(ShipPark shipPark, String location) throws IOException {

        FileReader fileReader = new FileReader(location);
        Scanner scanner = new Scanner(fileReader);

        ObservableList<Ship> observableList = shipPark.getObservableList();

        while(scanner.hasNextLine()) {
            Ship ship = new Ship("", "");
            ship.name = scanner.nextLine();

            if(scanner.hasNextLine()) {
                ship.goods = scanner.nextLine();
            }

            shipPark.add(ship);
        }

        scanner.close();
        fileReader.close();
    }

    /**Is used to write complete tree to file. Uses recursion (by writeRecursion).
     * @param shipPark root node of your tree that you want to write down
     * @param location location where your file will be
     * @exception IOException happens when method cannot access to adjusted location*/
    public static void write(ShipPark shipPark, String location) throws IOException {

        FileWriter fileWriter = new FileWriter(location, false);

        ObservableList<Ship> observableList = shipPark.getObservableList();

        for (Ship ship : observableList) {
            fileWriter.write(ship.name + "\n");

            if (ship.goods == null) {
                fileWriter.write( "null");
            } else {
                fileWriter.write(ship.goods);
            }
            fileWriter.write("\n");
        }
        fileWriter.close();
    }

}
