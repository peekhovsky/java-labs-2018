package main.java.com.explorer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;


/**
 * This class is created to provide main interface of this app.
 * It creates main buttons, tree and list.
 *
 * Has 3 types of using:
 * 1) Guest (can only read and open files)
 * 2) User (can add files up to 10MB)
 * 3) Administrator (Can do anything he wants)
 *
 * If you wanna create this class you should choose any of this types of using.
 * My whole program provides another class to find out a type of user.
 * @see UserControl
 *
 * @author Rostislav Pekhovsky (2018)
 * @version 0.1
 * */

public class Explorer extends JFrame {

    //Variables
    //Tree classes
    /**
     * Defines main object of this class. Stores all file nodes used by this program
     */
    private JTree tree;

    /**
     * Is like iterator for JTree.
     *
     * @see DefaultMutableTreeNode
     * Use this iterator to get an object that is selected now.
     */
    private DefaultMutableTreeNode selectedNode;

    /**
     * This iterator points to main root node
     */
    private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root", true);


    //List classes
    /**
     * This list store file nodes (leaves) that is in selected folder and appear in list in the explorer
     */
    private JList<ExplorerFile> list;

    /**
     * It is iterator for list with files
     *
     * @see JList list
     */
    private DefaultListModel<ExplorerFile> listModel;

    /**
     * This class is used to get any new names or another text information from user
     */
    private JTextField textField;


    //Selection

    /**
     * Use this enum type to define what was selected last:
     * file in tree of in list
     */
    private enum Selection {
        Tree, List
    }

    /**
     * Use this variable to define what was selected
     *
     * @see Selection
     */
    private Selection selection = Selection.Tree;


    /**
     * Next 3 variables is used to control bytes limit for user in adding files.
     *
     * @see UserControl.TypeOfUser
     */
    private UserControl.TypeOfUser typeOfUser;

    private UserDate userDate;

    /**
     * This variable counts bytes of files added by user
     */
    private long countOfUserBytes = 0;

    /**
     * This value defines max bytes that user can add
     */
    private final static long maxCountOfUserBytes = 10000000;

    MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (e.getClickCount() == 2) {
                openFile();
            }
        }
    };

    //Constructors

    /**
     * Constructor of this class. Creates all main components in frame
     * Firstly setting new JFrame, than add elements: JTree, JList, several JButton.
     * After that it set up access permissions.
     *
     * @param typeOfUserNew sets a type of user (it affects data access.
     */
    public Explorer(UserControl.TypeOfUser typeOfUserNew, String location) {

        //Setting JFrame
        super("Explorer");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setBounds(200, 200, 620, 400);

        //Setting JPanel
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setLayout(null);
        panel.setBounds(0, 0, 620, 300);
        this.add(panel);

        //Setting Tree
        //Reading from file
        try {
            ExplorerIO.read(rootNode, location);
        } catch (IOException ex) {
            System.out.println("IOException!");
        }
        //Creating interface
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode, true);
        tree = new JTree(treeModel);
        JScrollPane scrollPaneTree = new JScrollPane(tree);
        scrollPaneTree.setBounds(5, 0, 290, 300);
        panel.add(scrollPaneTree);

        selectedNode = rootNode;
        //Setting up interface
        tree.addTreeSelectionListener((TreeSelectionEvent event) ->
        {
            selection = Selection.Tree;
            selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            upgradeList(selectedNode);
            list.updateUI();
        });

        tree.addMouseListener(mouseAdapter);

        // to provide action when user select any object in Tree
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); // user can select only one object
        tree.setEditable(false);                // user cannot edit tree
        tree.setCellRenderer(new IconRenderer());     // to make icons from windows
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);             // user cannot see root folder


        //List
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);

        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                try {

                    if (value instanceof ExplorerFile) {

                        label.setIcon(((ExplorerFile) value).getIcon());

                    }

                } catch (NullPointerException ex) {
                    return null;
                }
                return label;
            }
        });

        list.addMouseListener(mouseAdapter);
        list.addListSelectionListener((ListSelectionEvent event) -> selection = Selection.List);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Adding tree to scroll pane
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.createVerticalScrollBar();
        scrollPane.setBounds(305, 0, 290, 300);
        scrollPane.setVisible(true);
        this.add(scrollPane);

        //Labels
        JLabel labelSetName = new JLabel("New name: ");
        labelSetName.setBounds(45, 305, 140, 20);
        add(labelSetName);


        //Setting Buttons
        ButtonListener buttonListener = new ButtonListener();

        //button for Adding Folder
        JButton buttonAddFolder = new JButton("Add Folder");
        buttonAddFolder.addActionListener(buttonListener);
        buttonAddFolder.setActionCommand("AddFolder");
        buttonAddFolder.setBounds(5, 330, 140, 20);
        this.add(buttonAddFolder);

        //button for Removing
        JButton buttonAddFile = new JButton("Add File");
        buttonAddFile.addActionListener(buttonListener);
        buttonAddFile.setActionCommand("AddFile");
        buttonAddFile.setBounds(305, 330, 140, 20);
        this.add(buttonAddFile);

        //button for Removing
        JButton buttonRemove = new JButton("Remove");
        buttonRemove.addActionListener(buttonListener);
        buttonRemove.setActionCommand("Remove");
        buttonRemove.setBounds(155, 330, 140, 20);
        this.add(buttonRemove);

        //button for Opening
        JButton buttonOpen = new JButton("Open");
        buttonOpen.addActionListener(buttonListener);
        buttonOpen.setActionCommand("Open");
        buttonOpen.setBounds(455, 330, 140, 20);
        this.add(buttonOpen);

        //button for saving
        JButton buttonSave = new JButton("Save");
        buttonSave.addActionListener(buttonListener);
        buttonSave.setActionCommand("Save");
        buttonSave.setBounds(305, 305, 140, 20);
        this.add(buttonSave);

        //button for renaming
        JButton buttonRename = new JButton("Find");
        buttonRename.addActionListener(buttonListener);
        buttonRename.setActionCommand("Find");
        buttonRename.setBounds(455, 305, 140, 20);
        this.add(buttonRename);


        //textField
        textField = new JTextField("");
        textField.setBounds(155, 305, 140, 20);
        this.add(textField);

        //
        setVisible(true);

        //setting connected with type of user
        typeOfUser = typeOfUserNew;

        if (typeOfUser.equals(UserControl.TypeOfUser.GUEST)) {
            buttonAddFile.setEnabled(false);
            buttonAddFolder.setEnabled(false);
            buttonRemove.setEnabled(false);
            buttonSave.setEnabled(false);
            buttonRename.setEnabled(false);
            labelSetName.setEnabled(false);
            textField.setEnabled(false);
        }


        userDate = new UserDate();
        userDate.read("UserDate.txt");

        DateFormat dateFormat = new SimpleDateFormat("DDD.yyyy");
        Date date = new Date();
        dateFormat.format(date);
        if (dateFormat.format(date).equals(dateFormat.format(userDate.date))) {
            countOfUserBytes = userDate.usingBytes;
        } else {

            userDate = new UserDate();
            userDate.date = date;
            userDate.write("UserDate.txt");

        }
    }


    //Methods

    /**
     * This method is employed by class to add new folders.
     * Folder is a kind of node that can have children. Object for folders is string
     *
     * @param object name for this folder
     * @param node   parent node*
     */
    public static DefaultMutableTreeNode addNode(Object object, DefaultMutableTreeNode node) {

        try {
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(object, true);

            if (node.isLeaf() && !node.isRoot()) {
                node = (DefaultMutableTreeNode) node.getParent();
            }
            node.add(newNode);
            return newNode;

        } catch (IllegalStateException exception) {
            System.out.println("Exception: java.lang.IllegalStateException");
            return null;
        }
    }


    /**
     * This method for adding new files from computer. It uses class ExplorerFile to describe adding files.
     *
     * @param file         new file that you add
     * @param previousNode folder where this file gonna be located*
     * @see ExplorerFile
     */
    public static void addLeaf(ExplorerFile file, DefaultMutableTreeNode previousNode) {

        try {
            DefaultMutableTreeNode tempNode = !(previousNode.getAllowsChildren()) ? (DefaultMutableTreeNode) previousNode.getParent() : previousNode;
            tempNode.add(new DefaultMutableTreeNode(file, false));
            System.out.println(file.getExtension());

        } catch (Exception exception) {
            System.out.println("Exception: addLeaf");
        }
    }


    /**
     * That method is for deleting folders or files.
     * You just need to give this node and it deletes itself and all children
     *
     * @param node folder or file that you want to delete*
     */
    private static void removeNode(DefaultMutableTreeNode node) {
        if (node != null && !node.isRoot()) {
            node.removeAllChildren();
            node.removeFromParent();
        }
    }


    /**
     * Class uses this method to find new children in folder and upgrade list with this children
     *
     * @param folder folder that you want to upgrade
     */
    private void upgradeList(DefaultMutableTreeNode folder) {

        if (folder == null) {
            listModel.removeAllElements();
            return;
        }
        try {

            if (folder.isLeaf() && !folder.isRoot()) {
                folder = (DefaultMutableTreeNode) folder.getParent();
            }

            Enumeration<TreeNode> nodes = folder.children();
            listModel.removeAllElements();

            while (nodes.hasMoreElements()) {

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement();
                Object object = node.getUserObject();

                if (object instanceof ExplorerFile) {
                    listModel.addElement((ExplorerFile) object);
                }
            }

        } catch (NullPointerException ex) {
            System.out.println("upgradeList -> NullPrtException");
        }
    }

    /**
     * This method adds new folder (node allowed having children) to tree to selected item.
     * If selected item is file (leaf) it adds new folder to folder of this file.
     *
     * @param name name of folder you want to add
     */
    private void addFolder(String name) {
        if (!name.isEmpty()) {
            addNode(textField.getText(), selectedNode);
            textField.setText("");
        }
    }

    /**
     * This method adds new file (leaf) to tree to selected item.
     * If selected item is file (leaf) it adds new file to folder of this file.
     */
    private void addFile() {
        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showDialog(null, "Открыть файл");

        if (response == JFileChooser.APPROVE_OPTION) {

            ExplorerFile explorerFile = new ExplorerFile(fileChooser.getSelectedFile());

            long fileSize = explorerFile.getFile().length();

            System.out.println("File size: " + fileSize);

            if (typeOfUser.equals(UserControl.TypeOfUser.USER) && ((fileSize + countOfUserBytes) >= maxCountOfUserBytes)) {
                JOptionPane.showMessageDialog(Explorer.this, "You add files over 10MB!", "Access permissions", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                countOfUserBytes += fileSize;
                userDate.usingBytes += countOfUserBytes;
                userDate.write("UserDate.txt");
            }

            addLeaf(explorerFile, selectedNode);
            textField.setText("");
        }
    }

    /**
     * This method opens selected file in tree or in list.
     * It does nothing if selected item is folder
     */
    private void openFile() {
        Object object = null;

        if (selection.equals(Selection.Tree)) {
            if (selectedNode.isLeaf()) {
                object = selectedNode.getUserObject();
            }
        } else {
            object = list.getSelectedValue();
        }

        try {
            if (object instanceof ExplorerFile) {
                ((ExplorerFile) object).open();
            }

        } catch (IOException | IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(Explorer.this, "Cannot open file! Check file location.", "Access error", JOptionPane.ERROR_MESSAGE);

        } catch (NullPointerException ex) {
            System.out.println("ButterListener -> case Open -> NullPointerException");
        }
    }

    /**
     * Save tree to file using class ExplorerIO.
     *
     * @see ExplorerIO
     */
    private void save() {
        try {

            ExplorerIO.write(rootNode, "Tree.txt");
            JOptionPane.showMessageDialog(Explorer.this, "Saved!", "Saving", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(Explorer.this, "Cannot access!", "Access error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void find(){
        if(!textField.getText().isEmpty()) {
            findRecursion(rootNode, textField.getText());
        }
        textField.setText("");
    }

    private void findRecursion(DefaultMutableTreeNode newRootNode, String findingName) {

        Enumeration<TreeNode> nodes = newRootNode.children();

        while (nodes.hasMoreElements()) {

            TreeNode node = nodes.nextElement();

            DefaultMutableTreeNode defaultNode = (DefaultMutableTreeNode) node;

            if (node.isLeaf()) {
                Object object = defaultNode.getUserObject();

                if (object instanceof ExplorerFile) {
                    //System.out.println("Node is instance of ExplorerFile.");
                    String str = object.toString();
                    if (str.equals(findingName)) {
                        selectedNode = defaultNode;
                        openFile();
                        break;
                    }
                }
            } else {
                    findRecursion(defaultNode, findingName);
            }

        }
    }

    /** Renames node does not mater if it is file or folder*/
    private void renameNode(String name){
        if(!name.isEmpty()) {
            Object obj = selectedNode.getUserObject();

            if (obj instanceof ExplorerFile) {
                ((ExplorerFile) obj).setFileName(name);

            } else if(obj instanceof String) {
                selectedNode.setUserObject(textField.getText());
            }
        }
    }

    /**@return root node of explorer tree*/
    public DefaultMutableTreeNode getRootNode(){
        return rootNode;
    }

    // interface

    /** This class is for listening buttons and performing actions depending on which button was clicked.* */
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            switch (actionEvent.getActionCommand()) {

                case "AddFolder":
                    addFolder(textField.getText());
                    textField.setText("");
                    break;

                case "AddFile":
                    addFile();
                    break;

                case "Remove":
                    removeNode(selectedNode);
                    selectedNode = rootNode;
                    break;

                case "Open":
                    openFile();
                    break;

                case "Save":
                    save();

                case "Find":
                    find();
                    break;
            }

            upgradeList(selectedNode);
            tree.updateUI();
            list.updateUI();
        }

    }


    /** Use this class to add icon loaded from existing file.
     * This file is provided by ExplorerFile
     * @see ExplorerFile* */
    class IconRenderer extends DefaultTreeCellRenderer {

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
                                                      boolean expanded, boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            Object nodeObj = ((DefaultMutableTreeNode)value).getUserObject();
            if(nodeObj instanceof ExplorerFile){
                setIcon(((ExplorerFile) nodeObj).getIcon());
            }


            return this;
        }
    }
}
