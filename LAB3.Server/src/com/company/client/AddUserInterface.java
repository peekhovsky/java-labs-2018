package com.company.client;

import com.company.user.Password;
import com.company.user.User;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;
import java.util.ArrayList;


/**Graphical user interface for client (add user)
 * @see ClientCore
 * @see MainInterface
 * @version 0.1
 * @author Rostislav Pekhovsky*/
class AddUserInterface {

    /**Main window backbone*/
    private Shell shell;
    /**title for login text box*/
    private Label labelLogin;
    /**title for password text box*/
    private Label labelPassword;
    /**title for repeat text box*/
    private Label labelRepeatPassword;
    /**text box to enter login*/
    private Text textLogin;
    /**text box to enter password*/
    private Text textPassword;
    /**text box to repeat password*/
    private Text textRepeatPassword;
    /**check box to add watch access tag to user*/
    private Button checkBoxWatch;
    /**check box to add send access tag to user*/
    private Button checkBoxSend;
    /**check box to add delete access tag to user*/
    private Button checkBoxDelete;
    /**check box to add save access tag to user*/
    private Button checkBoxSave;
    /**check box to add add access tag to user*/
    private Button checkBoxAdd;
    /**check box to add adduser access tag to user*/
    private Button checkBoxAddUsers;

    /**Main core of app*/
    private ClientCore clientCore;

    /**Constructor
     * @param clientCore main core of app
     * @param parentShell parent shell*/
    AddUserInterface(ClientCore clientCore, Shell parentShell) {
        this.clientCore = clientCore;

        //Display
        shell = new Shell(parentShell);
        shell.setBounds(750, 400, 400, 350);
        shell.setText("Add new user");

        //Text login
        labelLogin = new Label(shell, SWT.NONE);
        labelLogin.setText("Login: ");

        textLogin = new Text(shell,  SWT.BORDER);

        //Text password
        labelPassword = new Label(shell, SWT.NONE);
        labelPassword.setText("Password: ");
        textPassword = new Text(shell,  SWT.PASSWORD | SWT.BORDER);

        //Text repeat password
        labelRepeatPassword = new Label(shell, SWT.NONE);
        labelRepeatPassword.setText("Repeat password: ");
        textRepeatPassword = new Text(shell,  SWT.PASSWORD | SWT.BORDER);

        //Button OK
        Button buttonOK =  new Button(shell, SWT.PUSH);
        buttonOK.addSelectionListener(selectionAdapterButtonOK);
        buttonOK.setText("Add");

        //Button Cancel
        Button buttonCancel =  new Button(shell, SWT.PUSH);
        buttonCancel.addSelectionListener(selectionAdapterButtonCancel);
        buttonCancel.setText("Cancel");

        //checkbox watch
        checkBoxWatch =  new Button(shell, SWT.CHECK);
        checkBoxWatch.setText("watching");
        checkBoxWatch.setSelection(true);

        //checkbox send
        checkBoxSend = new Button(shell, SWT.CHECK);
        checkBoxSend.setText("sending");

        //checkbox add
        checkBoxAdd =  new Button(shell, SWT.CHECK);
        checkBoxAdd.setText("adding");

        //checkbox delete
        checkBoxDelete = new Button(shell, SWT.CHECK);
        checkBoxDelete.setText("deleting");

        //checkbox save
        checkBoxSave =  new Button(shell, SWT.CHECK);
        checkBoxSave.setText("saving");

        //checkbox add users
        checkBoxAddUsers =  new Button(shell, SWT.CHECK);
        checkBoxAddUsers.setText("adding users");

        //bounds
        labelLogin.setBounds            (10, 10,   360, 25);
        textLogin.setBounds             (10, 35,   360, 25);
        labelPassword.setBounds         (10, 70,   360, 25);
        textPassword.setBounds          (10, 95,   360, 25);
        labelRepeatPassword.setBounds   (10, 130,  360, 25);
        textRepeatPassword.setBounds    (10, 155,  360, 25);

        checkBoxWatch.setBounds         (10,  190, 80,  25);
        checkBoxSend.setBounds          (100, 190, 80,  25);
        checkBoxDelete.setBounds        (190, 190, 80,  25);

        checkBoxSave.setBounds          (10, 220, 80, 25);
        checkBoxAdd.setBounds           (100,  220, 80,  25);
        checkBoxAddUsers.setBounds      (190,  220, 120,  25);

        buttonOK.setBounds              (10,  260, 170, 30);
        buttonCancel.setBounds          (200, 260, 170, 30);

        //open shell
        shell.open();
    }

    //Listeners
    /**performs when button OK has been pressed. Adds new user to database.*/
    private SelectionAdapter selectionAdapterButtonOK = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent event) {
            ArrayList<String> accessTags = new ArrayList<>();

            if (checkBoxWatch.getSelection()) {
               accessTags.add("watch");
            }
            if (checkBoxSend.getSelection()) {
                accessTags.add("send");
            }
            if (checkBoxDelete.getSelection()) {
                accessTags.add("delete");
            }
            if (checkBoxSave.getSelection()) {
                accessTags.add("save");
            }
            if (checkBoxAdd.getSelection()) {
                accessTags.add("add");
            }
            if (checkBoxAddUsers.getSelection()) {
                accessTags.add("addusers");
            }

            if(textLogin.getText().isEmpty()) {
                labelLogin.setText("Login: (must be filled)");
                return;
            } else {
                labelLogin.setText("Login:");
            }

            if(textPassword.getText().isEmpty()) {
                labelPassword.setText("Password: (must be filled)");
                return;
            } else {
                labelPassword.setText("Password:");
            }


            if (!textPassword.getText().equals(textRepeatPassword.getText())) {
                labelRepeatPassword.setText("Repeat password: (passwords do not match)");
                labelRepeatPassword.update();
                return;
            } else {
                labelRepeatPassword.setText("Repeat password: ");
            }


            clientCore.processUser = new User(textLogin.getText(), accessTags, Password.getHash(textPassword.getText()));
            clientCore.setCommand("adduser");
            shell.setVisible(false);
        }
    };

    /**performs when button Cancel has been pressed*/
    private SelectionAdapter selectionAdapterButtonCancel = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent event) {
            shell.close();
        }
    };
}
