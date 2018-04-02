package server;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import main.Ship;
import main.Warehouse;


/**This class is to manage ships and goods that are in mooring lines
 * @see main.Warehouse
 * @see ShipQueue
 * @author Rostislav Pekhovsky 2018*/
public class TableHandler extends Thread {

    /**List with ships that stay in mooring lines*/
    private ObservableList<MooringLine> mooringLines;
    /**GUI Table to show data*/
    private TableView<MooringLine> tableView;
    /**This is a queue where ships wait to arrive to port*/
    private ShipQueue shipQueue;

    /**A count of mooring lines*/
    private int size;
    /**A count of ships that stay in mooring lines*/
    private int countOfShips;
    /***/
    private boolean isClosed;
    /**Warehouse*/
    Warehouse warehouse;

    public TableHandler(ShipQueue shipQueue, TableView<MooringLine> tableView, ObservableList<MooringLine> mooringLines, Warehouse warehouse) {

        this.mooringLines = mooringLines;
        this.tableView = tableView;
        this.shipQueue = shipQueue;
        this.warehouse = warehouse;

        size = mooringLines.size();
        countOfShips = 0;
        isClosed = false;
    }

     public void addLine(){
        mooringLines.add(new MooringLine(size));
        size++;
         updateNumeration();
        tableView.refresh();
    }


     public void deleteLine() {

         if (!tableView.getSelectionModel().isEmpty()) {
             sendBackShip();
             mooringLines.remove(tableView.getSelectionModel().getSelectedIndex());
             updateNumeration();
             tableView.refresh();
             size--;
         }
     }

     public MooringLine addShip(Ship ship){

        for (MooringLine mooringLine : mooringLines) {

            if (mooringLine.getStatus().equalsIgnoreCase("empty")) {
                mooringLine.setShip(ship);
                System.out.println("Name to add: " + ship.name);
                System.out.println("Goods: " + ship.goods);
                countOfShips++;
                tableView.refresh();
                return mooringLine;
            }
        }
        return null;
     }

     public void sendBackShip() {
        if(tableView.getSelectionModel().isEmpty()) {
            return;
        }

        if (tableView.getSelectionModel().getSelectedItem().getStatus().equals("busy")){
                tableView.getSelectionModel().getSelectedItem().getShip().setBusy(false);
                tableView.getSelectionModel().getSelectedItem().freeLine();
                countOfShips--;
                updateNumeration();
                tableView.refresh();
        }
     }

     public String popGood(){
        MooringLine mooringLine = tableView.getSelectionModel().getSelectedItem();
        if (mooringLine == null) {
            return null;
        }
        String goods = mooringLine.getGoods();

        if (goods.isEmpty() || mooringLine.getStatus().equals("empty")) {
            return null;
        }
         mooringLine.getShip().goods = "";
        mooringLine.setGoods("");
        updateNumeration();
        tableView.refresh();
        return goods;
     }

    public boolean ableToPushGood(){

        MooringLine mooringLine = tableView.getSelectionModel().getSelectedItem();

        if (mooringLine == null) {
            return false;
        }
        if (mooringLine.getStatus().equals("empty") || !mooringLine.getShip().goods.isEmpty()) {
            return false;
        }

        return true;
    }

        public boolean pushGood(String goods){

        MooringLine mooringLine = tableView.getSelectionModel().getSelectedItem();

        if (mooringLine == null) {
            return false;
        }
        if (goods == null) {
            return false;
        }
        if (goods.isEmpty() || mooringLine.getStatus().equals("empty") || !mooringLine.getShip().goods.isEmpty()) {
            return false;
        }

        mooringLine.getShip().goods = goods;
        mooringLine.setGoods(goods);
        updateNumeration();
        tableView.refresh();
        return true;
    }

     public void close() {
        isClosed = true;
     }

     private void updateNumeration(){
        int index = 1;
        for(MooringLine mooringLine : mooringLines){
            mooringLine.setNumber(index);
            index++;
        }
     }

     public int getCountOfShips(){
        return countOfShips;
     }
     public int size(){
        return size;
     }

     @Override
     public void run() {

        while (!isClosed) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                System.out.println("MooringLine: InterruptedException");
            }

            Platform.runLater(() -> {
                if ((countOfShips < size) && (size > 0)) {

                    Ship ship = shipQueue.pop();
                    if (ship != null) {

                        MooringLine mooringLine = addShip(ship);
                        if (ship.auto) {
                            Thread thread = new Thread(()-> {
                                    try {
                                            if (!mooringLine.getShip().auto) {
                                                return;
                                            }
                                                Thread.sleep(2000);
                                                String goods = mooringLine.getGoods();

                                                if (goods != null) {
                                                    if (!goods.isEmpty()) {
                                                        warehouse.add(goods);
                                                        mooringLine.setGoods("");
                                                        mooringLine.getShip().goods = "";

                                                        String newGoods = warehouse.getSelected();
                                                        tableView.refresh();

                                                        if (newGoods != null) {
                                                            Platform.runLater(()->warehouse.getObservableList().remove(newGoods));
                                                            if (!newGoods.isEmpty()) {
                                                                mooringLine.setGoods(newGoods);
                                                                mooringLine.getShip().goods = newGoods;
                                                                tableView.refresh();
                                                            }
                                                        }
                                                        Thread.sleep(1000);
                                                    }
                                                }
                                        mooringLine.getShip().setBusy(false);
                                        mooringLine.freeLine();
                                        countOfShips--;
                                        tableView.refresh();
                                    } catch (InterruptedException ex) {
                                        System.out.println("Interrupted exception!");
                                    }
                            });
                            thread.start();
                        }
                    }
                }
            });




        }
        if (isClosed) {
            deleteLine();
        }

    }
}
