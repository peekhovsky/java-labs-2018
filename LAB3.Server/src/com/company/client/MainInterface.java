package com.company.client;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

import com.company.date.DateTranslation;
import com.company.person.Person;
import com.company.user.User;

import java.util.ArrayList;
import java.util.Date;

/**Graphical user interface for client (main part)
 * @see ClientCore
 * @see AddUserInterface
 * @version 0.1
 * @author Rostislav Pekhovsky*/
public class MainInterface {

    /**Main window backbone*/
	private Shell shell;
	/**Table with person that processes right now*/
	private Table table;
	/**Box with preliminary list of persons in database*/
	private List list;
	/**Text box to change person*/
	private Text text;
	/**Text box to find person in a list of persons*/
	private Text findText;
	/**Button to add new person to database*/
	private Button buttonAddNew;
    /**Button to watch selected person in list*/
	private Button buttonWatch;
	/**Button to send back processed person*/
	private Button buttonSend;
    /**Button to delete person from database selected in list*/
	private Button buttonDelete;
	/**Button to save database*/
	private Button buttonSave;
	/**Button to add new user*/
	private Button buttonAddUser;

	/**Main logic part of application*/
	private ClientCore clientCore;

	/**Constructor
     * @param clientCore main logic part of application*/
	MainInterface(ClientCore clientCore) {

	    //Display
		Display display = new Display();
        shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shell.setLocation(600, 300);
		shell.setSize(800, 550);
		shell.setText("Client");

		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				shell.setVisible(false);
				if (clientCore != null) {
					clientCore.close();
				}
			}
		});

		//List
		list = new List(shell, SWT.BORDER | SWT.V_SCROLL);
		list.addSelectionListener(selectionListenerList);
		list.setBounds(20, 20, 300, 400);

		Label label = new Label(shell, SWT.NULL);
		label.setText("Find: ");
		label.setBounds(20, 430, 300, 20);

		findText = new Text(shell, SWT.SINGLE | SWT.BORDER);
		findText.setBounds(20, 450, 300, 25);
		findText.addModifyListener(modifyListenerFindText);


		String[] comboItems = {"Given name", "Middle name", "Surname", "Date of birth", "Cell phone", "Experience", "Item"};

		//Table
		table = new Table(shell, SWT.BORDER | SWT.H_SCROLL | SWT.LAST_LINE_SELECTION);

		table.setBounds(460, 20, 305, 300);

		TableColumn tableColumn1 = new TableColumn(table, SWT.READ_ONLY);
		tableColumn1.setWidth(130);

		TableColumn tableColumn2 = new TableColumn(table, SWT.READ_ONLY);
		tableColumn2.setWidth(170);

		//Text field
		text = new Text(shell, SWT.SINGLE | SWT.BORDER);
		text.setBounds(460, 385, 305, 25);

		//Button Change
		Button buttonChange = new Button(shell, SWT.PUSH);
		buttonChange.addSelectionListener(selectionAdapterButtonChange);
		buttonChange.setText("Change");
		buttonChange.setBounds(460, 420, 150, 30);

		//Button addSkill
		Button buttonAddSkill = new Button(shell, SWT.PUSH);
		buttonAddSkill.addSelectionListener(selectionAdapterButtonAddSkill);
		buttonAddSkill.setText("Add skill");
		buttonAddSkill.setBounds(615, 420, 150, 30);
		buttonAddSkill.setEnabled(true);

		//Button addSkill
		Button buttonDeleteSkill = new Button(shell, SWT.PUSH);
		buttonDeleteSkill.addSelectionListener(selectionAdapterButtonDeleteItem);
		buttonDeleteSkill.setText("Delete item");
		buttonDeleteSkill.setBounds(615, 325, 150, 30);
		buttonDeleteSkill.setEnabled(true);

		//Button Create new
		Button buttonCreateNew = new Button(shell, SWT.PUSH);
		buttonCreateNew.addSelectionListener(selectionAdapterButtonCreateNew);
		buttonCreateNew.setText("Create new");
		buttonCreateNew.setBounds(460, 455, 305, 30);

		//Button Get
		buttonWatch = new Button(shell, SWT.PUSH);
		buttonWatch.addSelectionListener(selectionAdapterButtonWatch);
		buttonWatch.setText("Watch");
		buttonWatch.setBounds(330, 20, 120, 30);
		buttonWatch.setEnabled(false);

		//Button Send
		buttonSend = new Button(shell, SWT.PUSH);
		buttonSend.addSelectionListener(selectionAdapterButtonSend);
		buttonSend.setText("Send");
		buttonSend.setBounds(330, 55, 120, 30);
		buttonSend.setEnabled(false);

		//Button Delete
		buttonDelete = new Button(shell, SWT.PUSH);
		buttonDelete.addSelectionListener(selectionAdapterButtonDelete);
		buttonDelete.setText("Delete");
		buttonDelete.setBounds(330, 90, 120, 30);
		buttonDelete.setEnabled(false);

		//Button Add new
		buttonAddNew = new Button(shell, SWT.PUSH);
		buttonAddNew.addSelectionListener(selectionAdapterButtonAddNew);
		buttonAddNew.setText("Add as new");
		buttonAddNew.setBounds(330, 145, 120, 30);
		buttonAddNew.setEnabled(false);

		//Button Save
		buttonSave = new Button(shell, SWT.PUSH);
		buttonSave.addSelectionListener(selectionAdapterButtonSave);
		buttonSave.setText("Save database");
		buttonSave.setBounds(330, 200, 120, 30);
		buttonSave.setEnabled(false);


		//Button AddUser
		buttonAddUser = new Button(shell, SWT.PUSH);
		buttonAddUser.addSelectionListener(selectionAdapterButtonAddUser);
		buttonAddUser.setText("Add user");
		buttonAddUser.setBounds(330, 245, 120, 30);
		buttonAddUser.setEnabled(false);

		shell.open();

		//Client core
		this.clientCore = clientCore;

		updateButtonVisible(false);

		while (!shell.isDisposed()) {
			if (clientCore.isProcessPersonChange) {
				updateTable(clientCore.processPerson);
				clientCore.isProcessPersonChange = false;
			}
			if (clientCore.isListChange) {
				updateList(clientCore.list, findText.getText());
				clientCore.isListChange = false;
			}
			if (clientCore.isMessageCannotConnect) {
                clientCore.isMessageCannotConnect = false;
                messageCannotConnect();
			}
			if (clientCore.isMessageEmptyPerson) {
				clientCore.isMessageEmptyPerson = false;
				messageEmptyPerson();
			}
			if (clientCore.isSavedMessage) {
				clientCore.isSavedMessage = false;
				messageSaved(true);
			}
			if (clientCore.isNotSavedMessage) {
				clientCore.isNotSavedMessage = false;
				messageSaved(false);
			}
			if (clientCore.isMessageUserAddingError) {
			    clientCore.isMessageUserAddingError = false;
			    messageUserAddingError();
            }
			if (!display.readAndDispatch()) {
				display.sleep();
			}

		}
		display.dispose();
	}

	//Messages
	/**To output a message 'cannot connect'*/
	private void messageCannotConnect() {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
		messageBox.setMessage("Cannot connect to server! Try to restart.");
		messageBox.open();
	}
    /**To output a message 'empty person'*/
	private void messageEmptyPerson() {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
		messageBox.setMessage("Person is empty!");
		messageBox.open();
	}
    /**To output a message 'adding error'*/
    private void messageUserAddingError() {
        MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
        messageBox.setMessage("Cannot add user! Login is not empty");
        messageBox.open();
    }
    /**To output a message 'message saved or not'*/
	private void messageSaved(boolean isSaved) {
		if (isSaved) {
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
			messageBox.setMessage("Database has been saved!");
			messageBox.open();
		} else {
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
			messageBox.setMessage("Cannot save database!");
			messageBox.open();
		}
	}

	//Updates
    /**To update preliminary list by new data*/
	private void updateList(ArrayList<String> list, String findingName) {
		this.list.removeAll();
		if (findingName.isEmpty()) {
			for (String name : list) {
				this.list.add(name);
			}
		} else {
			for (String name : list) {
				if (name.toLowerCase().contains(findingName.toLowerCase())) {
					this.list.add(name);
				}
			}
		}
		this.list.update();
	}

	/**To uptate button enabling. Depends on user access.*/
	private void updateButtonVisible(boolean isCreateNew) {
		ArrayList<String> accessTags = clientCore.user.accessTags;
		for (String accessTag : accessTags) {
			System.out.println("Access tag: " + accessTag);
			switch (accessTag){
				case "watch":
					buttonWatch.setEnabled(true);
					buttonWatch.update();
					break;
				case "add":
					buttonAddNew.setEnabled(isCreateNew);
					buttonAddNew.update();
					break;
				case "delete":
					buttonDelete.setEnabled(true);
					buttonDelete.update();
					break;
				case "send":
					//buttonSend.setEnabled(true);
					buttonSend.setEnabled(!isCreateNew);
					buttonSend.update();
					break;
				case "save":
					buttonSave.setEnabled(true);
					buttonSave.update();
					break;
				case "adduser":
					buttonAddUser.setEnabled(true);
					buttonAddUser.update();
					break;
			}
		}
	}

    /**To update a table with processed person by new data*/
	private void updateTable(Person person) {
		table.removeAll();
		if (person == null) {
			return;
		}

		System.out.println("Update table");
		TableItem tableItemGivenName = new TableItem(table, SWT.NULL);
		TableItem tableItemMiddleName = new TableItem(table, SWT.NULL);
		TableItem tableItemSurname = new TableItem(table, SWT.NULL);
		TableItem tableItemDateOfBirth = new TableItem(table, SWT.NULL);
		TableItem tableItemCellPhone = new TableItem(table, SWT.NULL);
		TableItem tableItemExperience = new TableItem(table, SWT.NULL);

		tableItemGivenName.setText(0, "Given name:");
		if (person.givenName != null) {
			tableItemGivenName.setText(1, person.givenName);
		}
		tableItemMiddleName.setText(0, "Middle name:");
		if (person.middleName != null) {
			tableItemMiddleName.setText(1, person.middleName);
		}
		tableItemSurname.setText(0, "Surname:");
		if (person.surname != null) {
			tableItemSurname.setText(1, person.surname);
		}
		tableItemDateOfBirth.setText(0, "Date of birth:");
		if (person.dateOfBirth != null) {
			tableItemDateOfBirth.setText(1, DateTranslation.convertDateToString(person.dateOfBirth));
		}
		tableItemCellPhone.setText(0, "Cell phone:");
		if (person.cellPhone != null) {
			tableItemCellPhone.setText(1, person.cellPhone);
		}
		tableItemExperience.setText(0, "Experience:");
		if (person.experience != null) {
			tableItemExperience.setText(1, person.experience);
		}

		for (String skill : person.skills) {
			TableItem tableItemSkill = new TableItem(table, SWT.NULL);

			if (skill != null) {
				tableItemSkill.setText("Skill:");

				tableItemSkill.setText(0, "Skill:");
				tableItemSkill.setText(1, skill);
			}
		}
		table.update();
	}


	//Listeners

    /**Performs when button change has been pressed */
	private SelectionAdapter selectionAdapterButtonChange = new SelectionAdapter() {
		String items[] = {"Given name", "Middle name", "Surname", "Date of birth", "Cell phone", "Experience", "Skill"};

		@Override
		public void widgetSelected(SelectionEvent event) {

			if (text.getText().equals("")) {
				return;
			}
			TableItem[] tableItems = table.getSelection();
			if (tableItems.length == 0) {
				return;
			}
 			switch (tableItems[0].getText()) {
				case "Given name:":
					clientCore.processPerson.givenName = text.getText();
					break;
				case "Middle name:":
					clientCore.processPerson.middleName = text.getText();
					break;
				case "Surname:":
					clientCore.processPerson.surname = text.getText();
					break;
				case "Date of birth:":
					Date date = DateTranslation.convertStringToDate(text.getText());
					if (date != null) {
						clientCore.processPerson.dateOfBirth = date;
					}
					break;
				case "Cell phone:":
					clientCore.processPerson.cellPhone = text.getText();
					break;

				case "Experience:":
					clientCore.processPerson.experience = text.getText();
					break;
				case "Skill:":
					clientCore.processPerson.skills.set((table.getSelectionIndex() - 6), text.getText());
					break;

			}

			clientCore.isProcessPersonChange = true;
			text.setText("");
		}
	};

    /**Performs when button create has been pressed */
	private SelectionAdapter selectionAdapterButtonCreateNew = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			updateButtonVisible(true);
		    clientCore.processPerson =  new Person();
		    clientCore.isProcessPersonChange = true;
		}
	};

    /**Performs when button add new has been pressed */
	private SelectionAdapter selectionAdapterButtonAddNew = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			clientCore.setCommand("add");
		}
	};

    /**Performs when button watch has been pressed */
	private SelectionAdapter selectionAdapterButtonWatch = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			updateButtonVisible(false);
			clientCore.setCommand("get");
			// clientCore.selectIndex = selectIndex;
		}
	};

    /**Performs when button send has been pressed */
	private SelectionAdapter selectionAdapterButtonSend = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			clientCore.setCommand("set");

		}
	};

    /**Performs when button delete has been pressed */
	private SelectionAdapter selectionAdapterButtonDelete = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			clientCore.setCommand("delete");
		}
	};

    /**Performs when button save has been pressed */
	private SelectionAdapter selectionAdapterButtonSave = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			clientCore.setCommand("save");
		}
	};

    /**Performs when button add user has been pressed */
	private SelectionAdapter selectionAdapterButtonAddUser = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {

			new AddUserInterface(clientCore, shell);
		}
	};

	/**Performs when button add skill has been pressed */
	private SelectionAdapter selectionAdapterButtonAddSkill = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			if (!text.getText().isEmpty()&& clientCore.processPerson != null) {
				clientCore.processPerson.addSkill(text.getText());
				updateTable(clientCore.processPerson);
			}
		}
	};

	/**Performs when button delete item has been pressed */
	private SelectionAdapter selectionAdapterButtonDeleteItem = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {

			TableItem[] tableItems = table.getSelection();
			if (tableItems.length == 0) {
				System.out.println("AAA");
				return;
			}
			switch (tableItems[0].getText()) {
				case "Given name:":
					clientCore.processPerson.givenName = " ";
					break;
				case "Middle name:":
					clientCore.processPerson.middleName = " ";
					break;
				case "Surname:":
					clientCore.processPerson.surname = " ";
					break;
				case "Date of birth:":
					Date date = DateTranslation.convertStringToDate(text.getText());
					if (date != null) {
						clientCore.processPerson.dateOfBirth = null;
					}
					break;
				case "Cell phone:":
					clientCore.processPerson.cellPhone = null;
					break;

				case "Experience:":
					clientCore.processPerson.experience = null;
					break;
				case "Skill:":
					clientCore.processPerson.skills.remove((table.getSelectionIndex() - 6));
					break;

			}
			clientCore.isProcessPersonChange = true;
		}
	};


    /**Performs when selection in list  has been changed */
	private SelectionListener selectionListenerList = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent event) {
			clientCore.selectIndex = list.getSelectionIndex();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			clientCore.selectIndex = list.getSelectionIndex();
		}
	};

    /**Performs when text in findText has been changed */
	ModifyListener modifyListenerFindText = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			updateList(clientCore.list, findText.getText());
		}
	};
}











