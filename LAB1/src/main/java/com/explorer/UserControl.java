package main.java.com.explorer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


/**This class is used to create three user types: guest, user and administrator.
 * It produces when program starts to find out what type of user is trying to access.
 * User and administrator protected by password.
 * @see Password
 * @author Rostislav Pekhovksy*/
public class UserControl extends JFrame implements ActionListener {

    //variables

    /**This enumeration defines 3 types of user.*/
    public enum TypeOfUser { USER, GUEST, ADMINISTRATOR}

    /**Signalize if user complete checking*/
    private boolean complete = false;

    /**Defines a type of user using TypeOfUser Enumeration*/
    private TypeOfUser typeOfUser;

    /**Password field where user will write password (filled by black dots instead of symbols)*/
    private JPasswordField passwordField;

    /**Here user choose a type of user*/
    private JComboBox<String> comboBox;

    /**Names password field*/
    private JLabel passwordLabel;


    //constructors

    /**Constructor creates frame with all elements*/
    UserControl() {

        //Frame
        super("Control");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setBounds(350,300,320,200);
        setVisible(true);

        //JComboBox
        String[] typesOfUsers = {"USER", "GUEST", "ADMINISTRATOR"};
        typeOfUser = TypeOfUser.USER;
        comboBox = new JComboBox<>(typesOfUsers);
        comboBox.setBounds(25, 40, 250, 20);
        comboBox.addActionListener(this);
        add(comboBox);

        //JPasswordField
        passwordField = new JPasswordField();
        passwordField.setBounds(25,80, 250,20);
        passwordField.setActionCommand("PasswordEnter");
        passwordField.addActionListener(this);
        passwordField.setText("");
        add(passwordField);


        //Buttons
        JButton buttonOK = new JButton("OK");
        buttonOK.setBounds(25, 115, 120,20);
        buttonOK.setActionCommand("OK");
        buttonOK.addActionListener(this);
        add(buttonOK);

        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.setBounds(155, 115, 120,20);
        buttonCancel.setActionCommand("Cancel");
        buttonCancel.addActionListener(this);
        add(buttonCancel);


        //Labels
        JLabel userLabel = new JLabel("Select a type of a user: ");
        userLabel.setBounds(25, 20, 250,20);
        add(userLabel);

        passwordLabel = new JLabel("Enter a password: ");
        passwordLabel.setBounds(25, 60, 250,20);
        add(passwordLabel);

        //Password password = new Password();
        //password.setPassword(0, "stevegates1");
        //password.setPassword(1, "williamgates2");

        //try {
        //    password.writePassword("Password.txt");
        //} catch (IOException ex){
        //     System.out.println("UserControl -> UserControl() -> IOException");
        //}

    }


    //methods

    /**This method starts explorer if user presses "OK" and completes checking*/
    private void StartExplorer(){
        SwingUtilities.invokeLater(()->new Explorer(typeOfUser, "Tree.txt"));
        this.dispose();
    }


    /**Returns true if user complete checking*/
    public boolean isCompleted() {return complete; }


    //interface
    /**Listens buttons. If user presses "OK" checking starts.
     * If user presses "Cancel" program ends.
     * @param event listening tool defined in constructor by buttons*/
    @Override
    public void actionPerformed(ActionEvent event){

        //ComboBox
        if (event.getActionCommand().equals("comboBoxChanged")) {

            try {
                typeOfUser = TypeOfUser.valueOf(comboBox.getSelectedItem().toString());
                switch (typeOfUser) {

                    case GUEST:
                        passwordField.setEnabled(false);
                        passwordLabel.setEnabled(false);
                        break;

                    case USER:
                    case ADMINISTRATOR:
                        passwordField.setEnabled(true);
                        passwordLabel.setEnabled(true);
                        break;

                }
            } catch (IllegalArgumentException | NullPointerException ex) {
                JOptionPane.showMessageDialog(this, "Cannot access!");
            }
        } else if (event.getActionCommand().equals("OK") || event.getActionCommand().equals("PasswordEnter")) {


            switch (typeOfUser) {

                case GUEST:

                    complete = true;

                    StartExplorer();

                    break;

                case USER:
                    /*without break*/

                case ADMINISTRATOR:

                    int index;
                    if(typeOfUser.equals(TypeOfUser.USER)){
                        index = 0;
                    } else {
                        index = 1;
                    }

                    String newPassword = String.valueOf(passwordField.getPassword());

                    try {
                        Password passwordStorage = new Password("Password.txt");


                        if (passwordStorage.isPasswordCorrect(index, newPassword)) {

                            //JOptionPane.showMessageDialog(this, "Right Password!");
                            complete = true;
                            StartExplorer();

                        } else {
                            JOptionPane.showMessageDialog(this, "Wrong Password!", "Error", JOptionPane.ERROR_MESSAGE);
                            passwordField.setText("");
                        }

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Cannot access!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }

        } else if (event.getActionCommand().equals("Cancel")) {
            this.dispose();
        }

    }
}

