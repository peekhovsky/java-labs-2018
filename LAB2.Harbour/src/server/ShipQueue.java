package server;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Ship;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.util.ListIterator;

public class ShipQueue {

    private ObservableList<Ship> shipObservableList;
    private int size;

    public ShipQueue() {
        this.shipObservableList = FXCollections.observableArrayList();
        size = 0;
    }

    public void push(Ship ship){
        shipObservableList.add(ship);
        size++;
    }

    public Ship pop(){

        ListIterator<Ship> iterator = shipObservableList.listIterator();
        if(iterator.hasNext()) {

            int index = iterator.nextIndex();
            Ship newShip = shipObservableList.get(index);
            if(newShip != null) {
                shipObservableList.removeAll(newShip);
                return newShip;
            }
        }
        return null;
    }

    public ObservableList<Ship> getShipObservableList(){
        return shipObservableList;
    }

    public void delete(int index){
        shipObservableList.get(index).setBusy(false);
        shipObservableList.remove(index);
    }

    public int size(){
        return shipObservableList.size();
    }
}
