package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import java.io.IOException;

public class Warehouse {

    private ListView<String> listView;
    private ObservableList<String> observableList;
    private boolean reverseSort = false;

    public Warehouse(ListView<String> listView){
        this.listView = listView;
        observableList = FXCollections.observableArrayList();
       if(listView != null) {
           listView.setItems(observableList);
       }
    }

    public void add(String goods){
        if(goods != null) {
            observableList.add(goods);
            refresh();
        }
    }




    public String getSelectedAndRemove(){
        if (!listView.getSelectionModel().isEmpty()) {
            int index = listView.getSelectionModel().getSelectedIndex();
            String string = observableList.get(index);
            if (string != null) {
                observableList.remove(index);
                refresh();
                return string;
            }
        }
        return null;
    }

    public String getSelected(){
        if (!listView.getSelectionModel().isEmpty()) {
            int index = listView.getSelectionModel().getSelectedIndex();
            String string = observableList.get(index);
            if (string != null) {
                refresh();
                return string;
            }
        }
        return null;
    }

    public void remove(){
        if(listView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        observableList.remove(listView.getSelectionModel().getSelectedIndex());
        listView.getSelectionModel().clearSelection();
        refresh();
    }

    public void sort(){
        if (!reverseSort) {
            observableList.sort(this::compare);
            reverseSort = true;
        } else {
            observableList.sort(this::reverseCompare);
            reverseSort = false;
        }
        refresh();
    }

    public int compare(String string1, String string2){
        return string1.compareToIgnoreCase(string2);
    }

    public int reverseCompare(String string1, String string2){
        return string1.compareToIgnoreCase(string2)*(-1);
    }

    public ObservableList<String> getObservableList(){
        return observableList;
    }

    public void save(String location){
        try {
            WarehouseRecorder.write(this, location);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Access Error");
            alert.setHeaderText("Access Error");
            alert.setContentText("Cannot save file!");
            alert.showAndWait();
        }
    }

    public void load(String location){
        try {
            WarehouseRecorder.read(this, location);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Access Error");
            alert.setHeaderText("Access Error");
            alert.setContentText("Cannot save file!");
            alert.showAndWait();
        }
    }

    public int size(){
        return observableList.size();
    }

    private void refresh(){
        if (listView != null) {
            listView.refresh();
        }
    }
}
