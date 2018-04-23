package com.company.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.company.person.*;
import com.company.user.*;


public class ServerCore {

  
    private MultiServerThread multiServerThread;

    private ServerSocket serverSocket;
    private boolean isClosed;
    private Persons persons;
    private Users users;

    ServerCore(int port, Persons persons, Users users) {
    	
    	this.persons = persons;
        this.users = users;
        isClosed = false;
        
        try {
            serverSocket = new ServerSocket(port);
            outputToTerminal("Server Socket has been created.");
            serverSocket.setSoTimeout(3000);

        } catch (IOException ex) {
            outputToTerminal("ServerCore -> ServerCore() -> IOException");
            outputToTerminal("\nCannot create (open) ServerSocket!");
        }
    }

   
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

            outputToTerminal("Server has been created.");
                
            while (!serverSocket.isClosed()) {
                if (isClosed) {
                    outputToTerminal("Try to close server socket...");
                    executorService.shutdown();
                    outputToTerminal("Server has been successfully closed.");
                    break;
                }
                try {
                    Socket socket = serverSocket.accept();
                    outputToTerminal("New connection has been accepted.");
                    outputToTerminal("Try to receive a new connection...");
                    executorService.execute(new MonoServerThread(socket));
                } catch (IOException ex) {
                   // outputToTerminalWithoutNewLine(".");
                }
                sleep(1000);
            }
        }

        private void sleep(int ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class MonoServerThread extends Thread {

        private Socket socket;
        private ObjectOutputStream objectOutputStream;
        private ObjectInputStream objectInputStream;

        private User user;



        MonoServerThread(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
    
            //Streams
            try {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                outputToTerminal("DataOutputStream has been created.");

                objectInputStream = new ObjectInputStream(socket.getInputStream());
                outputToTerminal("DataInputStream has been created.");

            } catch (IOException ex) {
                outputToTerminal("Cannot connect to this socket!");
                System.out.println("IOException: ServerCore -> ServerCoreThread -> run() -> //Streams");
                return;
            }

            //check user
            user = receiveAndCheckUser();
            if (user == null) {
                outputToTerminal("User is not valid!");
                return;
            }

            //sending a list of users
            sendList();

            //supporting connection
            supportConnection();

            //closing
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

        void supportConnection() {
            while (!socket.isClosed() && !isClosed) {
                sleep(200);
                try {
                    String command = (String) objectInputStream.readObject();

                    if (command != null) {
                        System.out.println("Command: " + command);
                        switch (command) {
                            case "get": {
                                if (user.checkAssessTag("watch")) {
                                Integer index = (Integer) objectInputStream.readObject();
                                    System.out.println("Index: " + index);
                                    if (index != null) {
                                        System.out.println("Write object.");
                                        objectOutputStream.writeObject(persons.get(index));
                                    }
                                }
                                break;

                            }

                            case "set": {
                                if (user.checkAssessTag("send")) {
                                    Person receivedPerson = (Person) objectInputStream.readObject();
                                    if (receivedPerson != null) {
                                        persons.setPerson(receivedPerson);
                                    } else {
                                        System.out.println("AAA");
                                    }
                                    sendList();
                                }
                                break;
                            }

                            case "add": {
                                if (user.checkAssessTag("add")) {
                                    Person receivedPerson = (Person) objectInputStream.readObject();
                                    if (receivedPerson != null) {
                                        persons.addPerson(receivedPerson);
                                    }
                                    sendList();
                                }
                                break;
                            }

                            case "delete": {
                                if (user.checkAssessTag("delete")) {
                                    Integer index = (Integer) objectInputStream.readObject();
                                    if (index != null) {
                                        persons.deletePerson(index);
                                    }
                                    sendList();
                                }
                                break;
                            }

                            case "save": {
                                boolean isWritten = false;
                                if (user.checkAssessTag("save")) {
                                    isWritten = (persons.write()) && (users.write());
                                }
                                objectOutputStream.writeObject(isWritten);
                                break;
                            }

                            case "adduser": {
                                if (user.checkAssessTag("adduser")) {
                                    User user = (User) objectInputStream.readObject();
                                    objectOutputStream.writeObject(users.addUser(user));
                                }
                                break;
                            }
                        }
                        sleep(500);
                    }
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    //outputToTerminal("ServerCore -> supportConnection() -> IOException!");
                    break;
                }
            }
        }


        private void sleep(int ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        private void sendList() {
            ArrayList<String> list = persons.getList();
            try {
                for (String item : list) {
                    System.out.println(item);
                    objectOutputStream.writeObject(item);
                }
                objectOutputStream.writeObject("quit");
            } catch (IOException ex) {
                System.out.println("IOException!");
            }
        }

        private User receiveAndCheckUser() {
            try {
                User newUser = (User) objectInputStream.readObject();

                user = users.checkUser(newUser.login, newUser.password);

                if (user != null) {
                    objectOutputStream.writeObject("approved");

                    objectOutputStream.writeObject(user);
                    return user;

                } else {
                    objectOutputStream.writeObject("failure");
                }

            } catch (ClassNotFoundException ex) {
                System.out.println("ClassNotFoundException: receiveAndCheckUser()");
            } catch (IOException ex) {
                System.out.println("IOException: receiveAndCheckUser()");
            }
            return null;
        }

    }

  
    private void outputToTerminal(String string) {
        System.out.println(string);
    }

    private void outputToTerminalWithoutNewLine(String string) {
      System.out.print(string);
    }
}