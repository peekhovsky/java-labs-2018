package main;

import client.ShipPark;
import server.ShipQueue;
import server.TableHandler;

import javax.xml.crypto.Data;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**This class is used to record a statement of this program
 * (port and ship manager) every 5 seconds to file.
 * Uses separate thread.
 * @author Rostislav Pekhovsky 2018*/
public class StateRecorder extends Thread {

    /**To determine how many ships in ship part*/
    private ShipPark shipPark;
    /**To determine how many goods in manager warehouse*/
    private Warehouse clientWarehouse;
    /**To determine how many ships in queue*/
    private ShipQueue queue;
    /**To determine how many ships in mooring lines
     * and how many mooring lines is available*/
    private TableHandler tableHandler;
    /**To determine how many goods in port warehouse*/
    private Warehouse serverWarehouse;
    /**Location of file to record in it data*/
    private String location;
    /**To stop this thread and close file*/
    private boolean isClosed;

    /**Constuctor
     * @param shipPark ship park in manager
     * @param clientWarehouse warehouse in manager
     * @param queue queue with ships
     * @param tableHandler mooring lines
     * @param serverWarehouse warehouse in port
     * @param location location of file to record
     * */
    StateRecorder(ShipPark shipPark, Warehouse clientWarehouse, ShipQueue queue,
                         TableHandler tableHandler, Warehouse serverWarehouse, String location){
        this.shipPark = shipPark;
        this.clientWarehouse = clientWarehouse;
        this.queue = queue;
        this.tableHandler = tableHandler;
        this.serverWarehouse = serverWarehouse;
        this.location = location;
        isClosed = false;
    }

    /**Thread with updating data in file*/
    @Override
    public void run(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        try {
            FileWriter writer = new FileWriter(location);
            while(!isClosed) {
                Thread.sleep(5000);
                Date date = new Date();

                writer.write("\nTime: " + dateFormat.format(date));
                writer.write("\nShips in Ship Park: " + shipPark.size());
                writer.write("\nGoods in Manager Warehouse: " + clientWarehouse.size());
                writer.write("\nShips in Queue: " + queue.size());
                writer.write("\nMooring lines: " + tableHandler.size());
                writer.write("\nShips in mooring lines: " + tableHandler.getCountOfShips());
                writer.write("\nGoods in Port Warehouse: " + serverWarehouse.size());
                writer.write("\n");
            }
            writer.close();
        } catch (IOException ex) {
            System.out.println("StateRecorder -> run() -> IOException");
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            System.out.println("StateRecorder -> run() -> InterruptedException");
            ex.printStackTrace();
        }
    }

    /**Used to close this thread*/
    public void close() {
        isClosed = true;
    }
}
