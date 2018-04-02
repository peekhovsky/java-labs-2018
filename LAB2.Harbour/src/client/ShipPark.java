package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import main.*;

import java.io.IOException;

/**This class is to store ships in list.
 * Has methods to add, delete, get and sort ships.
 * @author Rostislav Pekhovsky 2018
 * */
public class ShipPark {

    /**Interface of ship park*/
    private ListView listView;
    /**List with all data*/
    private ObservableList<Ship> observableList;
    /**manager warehouse*/
    private Warehouse warehouse;
    /**is used for changing a type of soaring*/
    private boolean reverseSort = false;

    /**Constructor
     * @param listView interface of ship park
     * @param warehouse manager warehouse*/
    public ShipPark(ListView<Ship> listView, Warehouse warehouse) {
        this.listView = listView;
        this.warehouse = warehouse;
        observableList = FXCollections.observableArrayList();
        listView.setItems(observableList);
    }

    /**@param ship ship that you want to add*/
    public void add(Ship ship){
        if (!ship.goods.isEmpty()) {
            warehouse.add(ship.goods);
            ship.goods = "";
        }
        observableList.add(ship);
        listView.refresh();
    }

    /**@return selected ship and deletes it from ship part
     * (like push)*/
    public Ship getSelectedAndRemove(){

        if(!listView.getSelectionModel().isEmpty()) {
            int index = listView.getSelectionModel().getSelectedIndex();
            Ship ship = observableList.get(index);
            observableList.remove(index);
            listView.refresh();
            return ship;
        }
        return null;
    }

    /**delete selected ship*/
    public void remove(){
        if (listView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        observableList.remove(listView.getSelectionModel().getSelectedIndex());
        listView.getSelectionModel().clearSelection();
        listView.refresh();
    }

    /**sort list*/
    public void sort() {
        if (!reverseSort) {
            observableList.sort(this::compare);
            reverseSort = true;
        } else {
            observableList.sort(this::reverseCompare);
            reverseSort = false;
        }
        listView.refresh();
    }


    /**@return observable list with ships*/
    public ObservableList<Ship> getObservableList(){
        return observableList;
    }

    /**saves ships to txt-file*/
    public void save(String location){
        try {
            ShipPartRecorder.write(this, location);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Access Error");
            alert.setHeaderText("Access Error");
            alert.setContentText("Cannot save file!");
            alert.showAndWait();
        }
    }

    /**loads ships from txt-file*/
    public void load(String location){
        try {
            ShipPartRecorder.read(this, location);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Access Error");
            alert.setHeaderText("Access Error");
            alert.setContentText("Cannot save file!");
            alert.showAndWait();
        }
    }

    /**@return size of a list with ships*/
    public int size(){
        return observableList.size();
    }

    /**compares two ships by name
     * @param ship1 first ship
     * @param ship2 second ship*/
    private int compare(Ship ship1, Ship ship2){
        return ship1.name.compareToIgnoreCase(ship2.name);
    }

    /**compares two ships by name
     * returns reverse result
     * @param ship1 first ship
     * @param ship2 second ship*/
    private int reverseCompare(Ship ship1, Ship ship2){
        return ship1.name.compareToIgnoreCase(ship2.name)*(-1);
    }


}
