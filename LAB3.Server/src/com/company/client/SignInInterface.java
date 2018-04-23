package com.company.client;

import com.company.user.User;
import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

/**Graphical user interface for client (sing in)
 * @see ClientCore
 * @see MainInterface
 * @version 0.1
 * @author Rostislav Pekhovsky*/
class SignInInterface {

	/**Display with all main components*/
	private Display display;
	/**Process bar shows a motion of establishing connection*/
	private ProgressBar progressBar;
	/**Text box to enter login*/
	private Text textLogin;
	/**Text box to enter password*/
	private Text textPassword;
	/**Backbone for elements*/
	private Shell shell;

	/**Constructor*/
	SignInInterface() {
		//Display
		display = new Display();
		shell = new Shell(display);
		shell.setLocation(770, 420);
		shell.setSize(400, 250);
		shell.setText("Sing in");
		
		//Text login
		Label labelLogin = new Label(shell, SWT.NONE);
		labelLogin.setText("Login: ");
		labelLogin.setLocation(10, 10);
		labelLogin.pack();
		
		textLogin = new Text(shell,  SWT.BORDER);
		textLogin.setLocation(10, 35);
		textLogin.setSize(360, 25);
		//text.pack();

		//Text password
		Label labelPassword = new Label(shell, SWT.NONE);
		labelPassword.setText("Password: ");
		labelPassword.setLocation(10, 70);
		labelPassword.pack();
		
		textPassword = new Text(shell,  SWT.PASSWORD | SWT.BORDER);
		textPassword.setLocation(10, 95);
		textPassword.setSize(360, 25);
		
		
		//Button OK
		Button buttonOK =  new Button(shell, SWT.PUSH);
		buttonOK.addSelectionListener(selectionAdapterButtonOK);
		buttonOK.setText("Sign In");
		buttonOK.setLocation(10, 160);
		buttonOK.setSize(170, 30);
		buttonOK.setSelection(true);
		
		//Button Cancel
		Button buttonCancel =  new Button(shell, SWT.PUSH);
		buttonCancel.addSelectionListener(selectionAdapterButtonCancel);
		buttonCancel.setText("Cancel");
		buttonCancel.setLocation(200, 160);
		buttonCancel.setSize(170, 30);

		//Progress bar
		progressBar = new ProgressBar(shell, SWT.NULL);
		progressBar.setBounds(10, 130, 360, 20);

		shell.open();
		
		while (!shell.isDisposed()) {
	          if (!display.readAndDispatch ()) display.sleep();
	      } 
	      display.dispose();
	}

	/**Performs when button OK has been pressed*/
	private SelectionAdapter selectionAdapterButtonOK = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			if (!textLogin.getText().isEmpty()) {

				String password;
				if (textPassword.getText().isEmpty()) {
					password = "";
				} else {
					password = textPassword.getText();
				}

				User user = new User(textLogin.getText(), password);
				ClientCore clientCore = new ClientCore("localhost", 7789);
				clientCore.runManager(user);

				long startTime = System.nanoTime();

				do {
					if (clientCore.approved) {
						display.dispose();
						new MainInterface(clientCore);
						break;
					} else {
						if (progressBar.getSelection() <= 100) {
							progressBar.setSelection(progressBar.getSelection() + 1);
						}
						if ((System.nanoTime() - startTime) > 1_000_000_000) {
							System.out.println("Incorrect login or passport!");
							messageInvalidUser();
							clientCore.close();
							progressBar.setSelection(0);
							break;
						}
					}
				} while (true);
			}
		}
	};

	/**Performs when button Cancel has been pressed*/
	private SelectionAdapter selectionAdapterButtonCancel = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			System.exit(0);
		}
	};

	/**To output a message 'invalid user'*/
	private void messageInvalidUser() {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
		messageBox.setMessage("Incorrect login or password, or cannot connect to server!");
		messageBox.open();
	}
}
