package main;

import client.ClientCore;
import client.ShipPark;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import server.MooringLine;
import server.ServerCore;
import server.ShipQueue;
import server.TableHandler;



/**Main class to control GUI.
 * Controls two different parts of program: server and client.
 * @author Rostislav Pekhovsky 2018 */
public class Controller {

    //package-private

    //---------server---------//
    /**Interface for Ship Queue
     *@see ShipQueue */
    @FXML
    ListView<Ship> listViewQueueShips;

    /**Server terminal to output messages with a state of a server*/
    @FXML
    TextArea serverTerminal;

    /**Interface object for Mooring Lines*/
    @FXML
    TableView<MooringLine> serverTableView;
    /**Column of Mooring lines contains order numbers*/
    @FXML
    TableColumn<MooringLine, Integer> serverTableNumberColumn;
    /**Column of Mooring lines contains a state of line*/
    @FXML
    TableColumn<MooringLine, String> serverTableStatusColumn;
    /**Column of Mooring lines contains name of ship*/
    @FXML
    TableColumn<MooringLine, String> serverTableShipNameColumn;
    /**Column of Mooring lines contains a good of ship*/
    @FXML
    TableColumn<MooringLine, String> serverTableGoodsColumn;

    /**Interface of warehouse for port
     * @see Warehouse*/
    @FXML
    ListView<String> serverListView;


    //---------client---------//
    /**Server terminal to output messages with a state of a client manager*/
    @FXML
    TextArea clientTerminal;

    /**Interface to input a name of new good in manager warehouse*/
    @FXML
    TextField clientTextField;

    /**Interface to input a name of new ship in manager warehouse
     * @see Warehouse*/
    @FXML
    TextField clientTextFieldShip;

    /**Interface of the list of goods in warehouse
     * @see ShipPark*/
    @FXML
    ListView<String> clientListViewWarehouse;

    /**Interface of the list of ships in park
     * @see Warehouse*/
    @FXML
    ListView<Ship> clientListView;


    //private

    //------stateRecorder------//
    /**This object is for output a state of program every 5 seconds.*/
    private StateRecorder stateRecorder;


    //---------server---------//
    /**Main server object. Includes separated thread.*/
    private ServerCore serverCore;

    /**Warehouse for port*/
    private Warehouse warehouse;

    /**Queue for server: ship can wait here if there is no place in mooring lines*/
    private ShipQueue shipQueue;

    /**Tool to manage mooring lines.*/
    private TableHandler tableHandler;


    //---------client---------//
    /**Main client object. Includes separated thread.*/
    private ClientCore clientCore;

    /**Upgraded list to manage, store and save ships*/
    private ShipPark shipPark;

    /**Warehouse for manager*/
    private Warehouse clientWarehouse;


    //methods - public

    /**Executes when class creates. Something like constructor.
     * Contains initializations of objects and
     * set up parameters for this objects*/
    @FXML
    public void initialize(){

        /*------server------*/
        //observableLists
        ObservableList<MooringLine> mooringLineObservableList = FXCollections.observableArrayList();
        //queue
        shipQueue = new ShipQueue();
        listViewQueueShips.setItems(shipQueue.getShipObservableList());
        //warehouse
        warehouse = new Warehouse(serverListView);
        warehouse.load("warehouse.txt");
        //table
        serverTableNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        serverTableStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        serverTableShipNameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));
        serverTableGoodsColumn.setCellValueFactory(new PropertyValueFactory<>("goods"));
        serverTableView.setItems(mooringLineObservableList);
        tableHandler = new TableHandler(shipQueue, serverTableView, mooringLineObservableList, warehouse);

        //core
        serverCore = new ServerCore(7788, serverTerminal, shipQueue);

        /*------client------*/
        //warehouse
        clientWarehouse = new Warehouse(clientListViewWarehouse);
        clientWarehouse.load("clientwarehouse.txt");
        //ship park
        shipPark = new ShipPark(clientListView, clientWarehouse);
        shipPark.load("shippark.txt");
        //core
        clientCore = new ClientCore(7788, clientTerminal, shipPark);

        /*---stateRecorder--*/
        stateRecorder = new StateRecorder(shipPark, clientWarehouse, shipQueue, tableHandler, warehouse, "report.txt");

        /*---start-server---*/
        serverConnect();
        clientConnect();
    }

    /**Performs when program closes.
     * It is to close all threads rightly.*/
    public void stop() {
        try {
            stateRecorder.close();
            while (stateRecorder.isAlive()) {
                Thread.sleep(20);
            }

            tableHandler.close();
            while (tableHandler.isAlive()) {
                Thread.sleep(20);
            }
            System.out.println("TableHandler was closed");
            if (serverCore.multiServerThread != null) {
                if (!serverCore.multiServerThread.getState().equals(Thread.State.NEW)) {
                    serverCore.close();
                    while (serverCore.multiServerThread.isAlive()) {
                        Thread.sleep(20);
                    }
                }
            }
            System.out.println("ServerCore was closed");
            if (clientCore.clientManagerThread != null) {
                if (!clientCore.clientManagerThread.getState().equals(Thread.State.NEW)) {
                    clientCore.close();
                    while (clientCore.clientManagerThread.isAlive()) {
                        Thread.sleep(20);
                    }
                }
            }
            System.out.println("ClientCore was closed");

        } catch (InterruptedException ex){
            ex.printStackTrace();
            System.out.println("Controller -> stop() -> InterruptedException");
        }
    }


    //package-private
    //server
    /**Performs to add new line to mooring lines*/
    @FXML
    void pressedAddLineButton(){
        tableHandler.addLine();
    }

    /**Performs to delete selected line in mooring lines*/
    @FXML
    void pressedDeleteLineButton(){
        tableHandler.deleteLine();
    }

    /**Performs to send back ship from line. If there is no ship do nothing. */
    @FXML
    void pressedSendBackButton(){
        tableHandler.sendBackShip();
    }

    /**Performs to send back ship that stays in queue.*/
    @FXML
    void pressedSendBackShipQueueButton(){
        if(!listViewQueueShips.getSelectionModel().isEmpty()) {
            shipQueue.delete(listViewQueueShips.getSelectionModel().getSelectedIndex());
        }
    }

    /**Performs to transport goods from ship in mooring lines to warehouse*/
    @FXML
    void pressedGetButton(){
        warehouse.add(tableHandler.popGood());
    }

    /**Performs to transport goods from warehouse to ship in mooring lines*/
    @FXML
    void pressedSetButton(){
        if (tableHandler.ableToPushGood()) {
            tableHandler.pushGood(warehouse.getSelectedAndRemove());
        }
    }

    /**Saves all goods in port warehouse to txt-file.*/
    @FXML
    void pressedSaveWarehouseButton(){
        warehouse.save("warehouse.txt");
    }

    /**Sorts items in port warehouse. First pressing from A to Z, second - vice versa.*/
    @FXML
    void pressedServerSortButton(){
        warehouse.sort();
    }

    /**Deletes item in warehouse*/
    @FXML
    void pressedServerDeleteButton(){
        warehouse.remove();
    }


    //client
    /**Performs sending selected ship with selected good. If there is no selection in good it will send ship without good.*/
    @FXML
    void pressedClientSendButton(){
        clientCore.setNewShipToSend(clientWarehouse.getSelectedAndRemove());
    }

    /**Adds new ship to ship park*/
    @FXML
    void pressedAddShipButton(){

        if (!clientTextFieldShip.getText().isEmpty()) {
            shipPark.add(new Ship(clientTextFieldShip.getText()));
            clientTextFieldShip.setText("");
        }
    }

    /**Deletes selected ship in ship park*/
    @FXML
    void pressedDeleteShipButton(){
        shipPark.remove();
    }

    /**Saves all ships in manager ship park to txt-file.*/
    @FXML
    void pressedSaveShipButton(){
        shipPark.save("shippark.txt");
        clientWarehouse.save("clientwarehouse.txt");
    }

    /**Sorts items in ship park. First pressing from A to Z, second - vice versa.*/
    @FXML
    void pressedClientSortButton(){
        shipPark.sort();
    }

    /**Adds new goods to manager warehouse.*/
    @FXML
    void pressedAddGoodsButton(){
        if (!clientTextField.getText().isEmpty()) {
            clientWarehouse.add(clientTextField.getText());
        }
    }

    /**Deletes goods in manager warehouse.*/
    @FXML
    void pressedDeleteGoodsButton(){
        clientWarehouse.remove();
    }

    /**Sorts items in manager warehouse. First pressing from A to Z, second - vice versa.*/
    @FXML
    void pressedSortGoodsButton() {
        clientWarehouse.sort();
    }
    @FXML
    void pressedClientSendAutoButton(){
        clientCore.setNewShipToSendAuto(clientWarehouse.getSelectedAndRemove());
    }

    //private

    /**Starts new server thread */
    private void serverConnect(){
        serverCore.runServer();
        if (tableHandler != null) {
            if (tableHandler.isAlive()) {
                return;
            }
        }
        if (tableHandler != null) {
            tableHandler.start();
        }

        stateRecorder.start();
    }

    /**Starts new client thread*/
    private void clientConnect() {
        clientCore.runManager();
    }


}