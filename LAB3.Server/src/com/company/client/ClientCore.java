package com.company.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.company.person.Person;
import com.company.user.*;


/**Main class that establish connection between server and client.
 * It uses special command to have some kind of understandable relations for both of them
 * @see com.company.server.ServerCore
 * @version 0.1
 * @author Rostislav Pekhovsky 2018*/
public class ClientCore {

    //person
    /**Variable with a person that is processed right now in app. Equals null if nothing is processed*/
    public Person processPerson;
    /***/
    public boolean isProcessPersonChange;
    /**Variable with a user that is processed right now in app. Equals null if nothing is processed*/
    public User processUser;
    /**Sets to true if suggested user is approved by server*/
    public boolean approved;
    /**Sets true to out a message 'Server cannot connect!' */
    public boolean isMessageCannotConnect;
    /**Sets true to out a message 'Empty Person'*/
    public boolean isMessageEmptyPerson;
    /**Sets true to out a message 'Database is saved'*/
    public boolean isSavedMessage;
    /**Sets true to out a message 'Database is not saved'*/
    public boolean isNotSavedMessage;
    /**Sets true to out a message 'User is not added'*/
    public boolean isMessageUserAddingError;

    //persons
    /**A list with preliminary list of persons*/
    public ArrayList<String> list;
    /**Changes to true to say that list is changed*/
    public boolean isListChange;
    /**Points to index in a list with persons which is selected*/
    public int selectIndex;

    //user
    /**User to reveal his access*/
    public User user;

    //client connection thread
    /**Main thread*/
    private ClientThread clientThread;
    /**Connection port*/
    private int port;
    /**Connection host*/
    private String host;
    /**Sets true to close client thread*/
    private boolean isClosed;
    /**Command to perform by client thread*/
    private String command;


    /**Constructor
     * @param host connection host
     * @param port connection port*/
	ClientCore(String host, int port) {
        this.port = port;
        this.host = host;
        this.isClosed = false;
        this.approved = false;
        isMessageCannotConnect = false;
        isMessageEmptyPerson = false;
        isSavedMessage = false;
        isNotSavedMessage = false;
    }

    /**To close main thread*/
    public void close() {
        this.isClosed = true;
    }

    /**To set new command for main thread*/
    public void setCommand(String command) {
        this.command = command;
    }

    /**To output to console debug messages */
    private void outputToTerminal(String string) {
        System.out.println(string);
    }

    /**To output to console debug messages without new line*/
    private void outputToTerminalWithoutNewLine(String string) {
        System.out.print(string);
    }


    /**Main thread*/
    private class ClientThread extends Thread {

        private Socket socket;
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;

        /**Constructor
         * @param newUser to check access*/
        ClientThread(User newUser) {
            user = newUser;
        }

        @Override
        public void run() {
            //create socket
            do {
                socket = acceptSocket();
                if (socket != null) {
                    outputToTerminal("Socket has been accepted.");
                    break;
                }
                outputToTerminalWithoutNewLine(".");
                sleep(1000);
            } while (!isClosed);

            if (isClosed) {
                closeSocket(socket);
                return;
            }

            //create ObjectOutputStream and ObjectInputStream
            try {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                outputToTerminal("DataInputStream has been created.");

                objectInputStream = new ObjectInputStream(socket.getInputStream());
                outputToTerminal("DataOutputStream has been created.");

            } catch (IOException ex) {
                outputToTerminal("Cannot create DataOutputStream/DataInputStream!");
                connectionError();
                ex.printStackTrace();
                return;
            }

            //logic part
            if (sendAndCheckUser()) {
                approved = true;
                list = receiveList();
                isListChange = true;
                supportConnection();
            }

            //close
            closeSocket(socket);
            System.out.println("Socket has been closed.");
        }

        /**Supports connection between server and client.
         * Uses commands: get, set, add, delete, adduser*/
        private void supportConnection() {
            outputToTerminal("Supporting connection...");
            while (!isClosed) {
                try {
                    if (command == null) {
                        command = null;
                        outputToTerminalWithoutNewLine(".");
                        sleep(500);
                        continue;
                    }

                    switch (command) {
                        case "get": {
                            objectOutputStream.writeObject("get");
                            outputToTerminal("Index: " + selectIndex);
                            objectOutputStream.writeObject(selectIndex);

                            Person person = receivePerson();

                            if (person != null) {
                                processPerson = person;
                                isProcessPersonChange = true;
                            }
                            sleep(200);
                            break;
                        }
                        case "set": {
                            if (processPerson == null) {
                                break;
                            }
                            if (processPerson.isEmpty()) {
                                emptyPersonError();
                                break;
                            }
                            objectOutputStream.writeObject("set");
                            sleep(200);
                            objectOutputStream.writeObject(processPerson);
                            sleep(200);
                            list = receiveList();
                            processPerson = null;
                            isProcessPersonChange = true;
                            break;
                        }
                        case "add": {
                            if (processPerson == null) {
                                break;
                            }
                            if (processPerson.isEmpty()) {
                                emptyPersonError();
                                break;
                            }
                            objectOutputStream.writeObject("add");
                            sleep(200);
                            objectOutputStream.writeObject(processPerson);
                            sleep(200);
                            list = receiveList();
                            break;
                        }
                        case "delete": {
                            objectOutputStream.writeObject("delete");
                            objectOutputStream.writeObject(selectIndex);
                            sleep(200);
                            list = receiveList();
                            break;
                        }
                        case "save": {
                            objectOutputStream.writeObject("save");
                            sleep(200);
                            try {
                                boolean isSaved = (boolean)objectInputStream.readObject();
                                savedMessage(isSaved);
                            } catch (ClassNotFoundException ex) {
                                ex.printStackTrace();
                                savedMessage(false);
                            }
                        }
                        case "adduser": {
                            if (processUser == null) {
                                break;
                            }
                            try {
                                objectOutputStream.writeObject("adduser");
                                objectOutputStream.writeObject(processUser);
                                boolean isUserAdded = (boolean)objectInputStream.readObject();
                                if (!isUserAdded) {
                                    userAddingError();
                                }
                                processUser = null;
                            } catch (IOException | ClassNotFoundException ex) {
                                connectionError();
                            }
                            break;
                        }
                    }
                    command = null;
                } catch (IOException ex) {
                    command = "";
                    connectionError();
                }
            }
        }
        /**Sends user and gets checking result*/
        private boolean sendAndCheckUser() {
            try {
                objectOutputStream.writeObject(user);
                outputToTerminal(user + ": has been sent.");

                String receivedCommand = (String) objectInputStream.readObject();
                if (receivedCommand != null) {
                    if (receivedCommand.equals("approved")) {
                        outputToTerminal("Approved!");
                        sleep(200);
                        user = (User) objectInputStream.readObject();
                        user.show();
                        return true;
                    } else if (receivedCommand.equals("failure")) {
                        outputToTerminal("Failure!");
                    } else {
                        connectionError();
                    }
                }
            } catch (IOException ec) {
                System.out.println("IOException: ClientCore -> ClienThread -> run() -> //get command");
                connectionError();
            } catch (ClassNotFoundException ec) {
                System.out.println("ClassNotFoundException: ClientCore -> ClienThread -> run() -> //get command");
                connectionError();
            }
            return false;
        }

        /**Receives a list with preliminary list of users*/
		private ArrayList<String> receiveList() {
			ArrayList<String> list = new ArrayList<>();
			try {
				while (true) {
					String name = (String) objectInputStream.readObject();

					if(name.equals("quit")) {
						break;
					}
					list.add(name);
				}
			} catch (ClassNotFoundException ex) {
				System.out.println("ReceiveList -> ClassNotFoundException");
			} catch (IOException ex) {
				System.out.println("ReceiveList -> IOException");
			}
			System.out.println("List has been received.");
			isListChange = true;
			return list;
		}

		/**Receives a person*/
        private Person receivePerson() throws ClassCastException {
			Person person;
			try {
				person  = (Person) objectInputStream.readObject();
        	} catch (ClassNotFoundException ex) {
        		outputToTerminal("ClassNotFoundException: ClientCore -> ClientThread -> receivePerson()");
        		ex.printStackTrace();
        		return null;
        	} catch (IOException ex) {
                outputToTerminal("IOException: ClientCore -> ClientThread -> receivePerson()");
        		ex.printStackTrace();
       			return null;
       		} catch (ClassCastException ex) {
                outputToTerminal("ClassCastException: ClientCore -> ClientThread -> receivePerson()");
			    ex.printStackTrace();
                return null;
            }
       		return person;
		}

		/**Stop thread on x milliseconds*/
        private void sleep(int ms) {
		    try {
		        Thread.sleep(ms);
            } catch (InterruptedException ex) {
		        ex.printStackTrace();
            }
        }

        /**Creates a socket*/
        private Socket acceptSocket() {
            Socket socket;
		    try {
		        socket = new Socket(host, port);
		    } catch (IOException ex) {
                connectionError();
		        return null;
		    }
            return socket;
        }

        /**Closes socket*/
        private void closeSocket(Socket socket) {
            if (socket != null) {
                if (!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        /**To output a message about connection error*/
        private void connectionError() {
            outputToTerminal("Cannot connect to server!");
            isMessageCannotConnect = true;
        }

        /**To output a message about empty person error*/
        private void emptyPersonError() {
            outputToTerminal("Empty person!");
            isMessageEmptyPerson = true;
        }

        /**To output a message about user adding error*/
        private void userAddingError() {
            outputToTerminal("User adding error!");
            isMessageUserAddingError = true;
        }

        /**To output a message about saving*/
        private void savedMessage(boolean isSaved) {
            outputToTerminal("Saved status: " + isSaved);
            isSavedMessage = isSaved;
        }

    }


    public void runManager(User user) {
        if (clientThread == null || !clientThread.isAlive()) {
            clientThread = new ClientThread(user);
            clientThread.start();
        }
    }
}

