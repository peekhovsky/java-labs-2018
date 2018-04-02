package client;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import main.Ship;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**This class is a kind of a core for client (manager).
 * It uses multithreading to create multi-connection
 * with a server.
 * @author Rostislav Pekhovsky 2018*/
public class ClientCore {

    /**main thread of class that makes new mono-threads*/
    public ClientManagerThread clientManagerThread;

    /**park in manager where ships are stored*/
    private ShipPark shipPark;
    /**ship that will be send next time*/
    private Ship shipToSend;
    /**port of connection*/
    private int port;
    /**to send messages to user in GUI*/
    private TextArea terminal;
    /**is used to close all threads correctly*/
    private boolean isClosed;

    /**Constructor
     * @param port port to establish connection
     * @param terminal to output messages to this textArea
     * @param shipPark park in manager where ships are stored*/
    public ClientCore(int port, TextArea terminal, ShipPark shipPark) {
        this.port = port;
        this.terminal = terminal;
        this.shipPark = shipPark;
        isClosed = false;

    }

    /**Sets up new ship that will be send next time if previous has been sent*/
    public void setNewShipToSend(String goods){
        if (shipToSend == null) {
            shipToSend = shipPark.getSelectedAndRemove();
            if(goods != null) {
                if (!goods.isEmpty()) {
                    shipToSend.goods = goods;
                }
            }
        }
    }
    public void setNewShipToSendAuto(String goods){
        if (shipToSend == null) {

            shipToSend = shipPark.getSelectedAndRemove();
            if(goods != null) {
                if (!goods.isEmpty()) {
                    shipToSend.goods = goods;
                }
            }
            shipToSend.auto = true;
        }
    }

    /**Starts main thread correctly.
     * Do nothing is thread has been started.*/
    public void runManager() {
        if (clientManagerThread != null) {
            if (clientManagerThread.isAlive()) {
                return;
            }
        }
        clientManagerThread = new ClientManagerThread();
        clientManagerThread.start();

    }

    /**give sign that all threads must be terminated*/
    public void close() {
        isClosed = true;
    }

    /**main thread that starts another mono-threads for connection*/
    public class ClientManagerThread extends Thread {

        private ExecutorService executorService = Executors.newCachedThreadPool();

        @Override
        public void run() {
            outputToTerminal("Client Manager has been started.");
                while (true) {

                    try {
                        Thread.sleep(100);
                        if (shipToSend == null) {
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException ex) {
                        System.out.println("InterruptedException: ClientCore -> ClientManagerThread -> run()");
                    }

                    Socket socket;

                    if (isClosed) {
                        executorService.shutdown();
                        break;
                    }

                    if (shipToSend != null) {
                        try {

                            socket = new Socket("localhost", port);
                            outputToTerminal("Socket has been created.");
                        } catch (IOException ex) {
                            outputToTerminal("Cannot create socket");
                            System.out.println("IOException: ClientCore -> ClientManagerThread -> run()");
                            continue;
                        }
                        executorService.execute(new MonoClientThread(socket, shipToSend));
                        shipToSend = null;

                    }
                }
                outputToTerminal("Trying to close client...");
                executorService.shutdown();
        }
    }

    /**mono-thread that connects with server,
     * sends a ship, and waits ship that will be send back*/
    private class MonoClientThread extends Thread {

        Socket socket;
        Ship ship;

        MonoClientThread(Socket socket, Ship ship){
            this.socket = socket;
            this.ship = ship;
           // start();
        }

        @Override
        public void run(){

            ObjectInputStream objectInputStream;
            ObjectOutputStream  objectOutputStream;

            //Streams
            try {
                outputToTerminal(ship + ": try to create new DataInputStream...");
                objectOutputStream  = new ObjectOutputStream(socket.getOutputStream());
                outputToTerminal(ship + ": dataInputStream has been created.");

                outputToTerminal(ship + ": try to create new DataOutputStream...");
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                outputToTerminal(ship + ": DataOutputStream has been created.");

            } catch (IOException ex) {
                outputToTerminal(ship + ": cannot create DataOutputStream/DataInputStream!");
                System.out.println("IOException: ClientCore -> ClientCoreThread -> run() -> //Streams");
                return;
            }

            /*---------------------------------------Logic Part------------------------------------------*/
            try {
                outputToTerminal(ship + ": try to send to server...");
                objectOutputStream.writeObject(ship);
                outputToTerminal(ship + ": has been sent.");

            } catch (IOException ex) {
                outputToTerminal(ship + ": cannot connect to this socket!");
                System.out.println("IOException: ClientCore -> ClientCoreThread -> run() -> //write object");
            }

            try {
                while (true) {
                    Thread.sleep(500);
                    try {
                        Ship inputShip = (Ship) objectInputStream.readObject();
                        if (inputShip != null) {
                            outputToTerminal(ship + ": got this.");
                            outputToTerminal(ship + ": close socket.");
                            ship = inputShip;
                            Platform.runLater(() -> shipPark.add(ship));
                            break;
                        }
                    } catch (ClassNotFoundException ex) {
                        continue;
                    }

                    if (isClosed) {
                        break;
                    }
                }
            } catch (IOException ex) {
                outputToTerminal(ship + ": cannot connect to this socket!");
                System.out.println("IOException: ClientCore -> ClientCoreThread -> run() -> //Connection");
                return;
            } catch (InterruptedException ex) {
                outputToTerminal(ship + ": cannot connect to this socket!");
                System.out.println("InterruptedException: ClientCore -> ClientCoreThread -> run() -> //Connection");
                return;
            }
            /*---------------------------------------------------------------------------------------------*/



            //Closing
            try {
                if(!socket.isClosed()) {
                    outputToTerminal(ship + ": try to close this socket..");
                    socket.close();
                    outputToTerminal(ship + ": socket has been closed.");
                }
            }  catch (IOException ex) {
                outputToTerminal(ship + ": Cannot close this socket!");
                System.out.println("IOException: ServerCore -> ServerCoreThread -> run() -> //Closing");
                return;
            }
        }

    }

    /**This method is to output data to manager terminal correctly*/
    private void outputToTerminal(String string) {
        Platform.runLater(() -> terminal.appendText("\n" + string));
    }

}