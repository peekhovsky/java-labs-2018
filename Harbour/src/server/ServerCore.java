package server;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import main.Ship;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**This class is a kind of a core for client (manager).
 * It uses multithreading to create multi-connection
 * with amount of sockets.
 * @author Rostislav Pekhovsky 2018*/
public class ServerCore {

    /**Main thread of class that makes new mono-threads*/
    public MultiServerThread multiServerThread;
    /**Manager for sockets*/
    private ServerSocket serverSocket;
    /**This is a queue where ships wait to arrive to port*/
    private ShipQueue shipQueue;

    /**to send messages to user in GUI*/
    private TextArea terminal;

    /**To close all threads of class rightly*/
    private boolean isClosed;

    /**Constructor
     * @param port a queue where ships wait to arrive to port
     * @param terminal to send messages to user in GUI
     * @param shipQueue a queue where ships wait to arrive to port*/
    public ServerCore(int port, TextArea terminal, ShipQueue shipQueue){

        this.terminal = terminal;
        this.shipQueue = shipQueue;
        isClosed = false;

        //server socket
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(3000);

        } catch (IOException ex) {
            System.out.println("ServerCore -> ServerCore() -> IOException");
            terminal.appendText("\nCannot create (open) ServerSocket!");
        }
    }

    /**This method is to determine that server must close immediately*/
    public void close() {
        isClosed = true;
    }

    /**This is to start main thread*/
    public void runServer() {
        if (multiServerThread != null) {
            if (multiServerThread.isAlive()) {
                return;
            }
        }
        multiServerThread = new MultiServerThread();
        multiServerThread.start();
    }


    /**Main thread*/
    public class MultiServerThread extends Thread {

        ExecutorService executorService = Executors.newCachedThreadPool();

        @Override
        public void run() {
            try {
                outputToTerminal("Port was opened.");
                outputToTerminal("Try to receive new ship. . . ");
                while (!serverSocket.isClosed()) {
                    if (isClosed) {
                        outputToTerminal("Try to close port...");
                        System.out.println("Try to close port...");
                        executorService.shutdown();
                        outputToTerminal("Port was successfully closed.");
                        System.out.println("Port was successfully closed.");
                        break;
                    }
                    try {
                        Socket socket = serverSocket.accept();
                        outputToTerminal("New ship has been accepted.");

                        executorService.execute(new MonoServerThread(socket));
                        outputToTerminal("Try to receive a new ship . . . ");
                    } catch (IOException ex) {
                        outputToTerminalWithoutNewLine(". ");
                    }
                    Thread.sleep(500);
                }
            } catch (InterruptedException ex) {
                outputToTerminal("Cannot open/close server.");
            }
        }
    }

    /**Mono-thread for main thread to provide exchange between sockets*/
    private class MonoServerThread extends Thread {

        Socket socket;

        MonoServerThread(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {

            ObjectOutputStream objectOutputStream;
            ObjectInputStream objectInputStream;

            double var = 356L;
            //Streams
            try {
                outputToTerminal("Try to create new DataOutputStream...");
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                outputToTerminal("DataOutputStream has been created.");

                outputToTerminal("Try to create new DataInputStream...");
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                outputToTerminal("DataInputStream has been created.");

            } catch (IOException ex) {
                outputToTerminal("Cannot connect to this socket!");
                System.out.println("IOException: ServerCore -> ServerCoreThread -> run() -> //Streams");
                return;
            }

            //Connection
            try {
                while (!socket.isClosed() && !isClosed) {
                    Thread.sleep(2000);
                    try {
                        Ship inputShip = (Ship) objectInputStream.readObject();
                        System.out.println(inputShip.toString());

                        inputShip.setBusy(true);
                        Platform.runLater(() -> shipQueue.push(inputShip));

                            while(inputShip.isBusy()) {
                                Thread.sleep(2000);
                                if (isClosed) {
                                    inputShip.setBusy(false);
                                    break;
                                }
                            }
                            objectOutputStream.writeObject(inputShip);
                            break;

                    } catch (ClassNotFoundException ex) {
                        outputToTerminal("Trying to read...");
                        System.out.println("Class not found exception");

                    } catch (IOException ex) {
                        System.out.println("\"IOException: ServerCore -> ServerCoreThread -> run() -> //Connection\"");
                        ex.printStackTrace();
                    }
                }
            } catch (InterruptedException ex) {
                outputToTerminal("Cannot connect to this socket!");
                System.out.println("InterruptedException: ServerCore -> ServerCoreThread -> run() -> //Connection");
                return;
            }

            //Closing
            try {
                outputToTerminal("Try to close this socket...");
                objectInputStream.close();
                objectOutputStream.close();

                if(!socket.isClosed()) {
                    socket.close();
                }
                outputToTerminal("Socket has been closed.");

            }  catch (IOException ex) {
                outputToTerminal("Cannot close this socket!");
                System.out.println("IOException: ServerCore -> ServerCoreThread -> run() -> //Closing");
                return;
            }
        }
    }

    /**This method is to output messages in GUI begins with new line*/
    private void outputToTerminal(String string) {
        Platform.runLater(() -> terminal.appendText("\n" + string));
    }

    /**This method is to output messages in GUI*/
    private void outputToTerminalWithoutNewLine(String string) {
        Platform.runLater(() -> terminal.appendText(string));
    }
}