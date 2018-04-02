package main.java.com.explorer;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Scanner;


/**     Use this class for writing Trees to file and reading them from file
 *      This class uses DefaultMutableTreeNodes to access Trees.
 *      Uses recursion to provide reading and writing.
 *      File structure:
 *
 *                      Folder
 *                      *NameOfFolder1*
 *
 *                              Folder
 *                              *NameOfFolder2*
 *
 *                                      File
 *                                      *NameOfFile1*
 *
 *                                      File
 *                                      *NameOfFile2*
 *
 *                              EndFolder
 *
 *                              File
 *                              *NameOfFile3*
 *
 *                      EndFolder
 *
 *      Beware of that is no description of root folder in file.
 *
 * @author Rostislav Pekhovsky
 * @see ExplorerFile
 */
public class ExplorerIO {

    //reading

    /**That method reads data from existing file located in computer.
     * Then he adds new  children nodes to rootNode.
     * Uses recursion with the help of readRecursion method.
     * @param rootNode root node from your tree that you want to fulfill
     * @param location file with description of a tree location
     * @exception IOException happens if there is no tree in adjusted location*/
    public static void read(DefaultMutableTreeNode rootNode, String location) throws IOException {

        FileReader fileReader = new FileReader(location);
        Scanner scanner = new Scanner(fileReader);

        //starting recursion for reading
        readRecursion(rootNode, scanner);

        scanner.close();
        fileReader.close();
    }

    /**Is used by method "read" for recursion reading.
     * @param rootNode node that is root (in recursion that is parent)
     * @param scanner reading tool*/
    private static void readRecursion(DefaultMutableTreeNode rootNode, Scanner scanner) {

        while (scanner.hasNextLine()) {

            String string = scanner.nextLine();

            switch (string) {

                case "Folder":

                    DefaultMutableTreeNode newNode = Explorer.addNode(scanner.nextLine(), rootNode);
                    readRecursion(newNode, scanner);
                    break;

                case "File":

                    Explorer.addLeaf(new ExplorerFile(scanner.nextLine()), rootNode);
                    break;

                case "EndFolder":

                    return;
            }
        }
    }


    //writing

    /**Is used to write complete tree to file. Uses recursion (by writeRecursion).
     * @param rootNode root node of your tree that you want to write down
     * @param location location where your file will be
     * @exception IOException happens when method cannot access to adjusted location*/
    public static void write(DefaultMutableTreeNode rootNode, String location) throws IOException {

            FileWriter fileWriter = new FileWriter(location, false);

            //start recursion
            writeRecursion(rootNode, fileWriter);
            fileWriter.close();
    }

    /**Is used by method "write" for recursion writing.
     * @param rootNode node that is root (in recursion that is parent)
     * @param writer writing tool
     * @exception IOException happens when method cannot access to adjusted location*/
    private static void writeRecursion(DefaultMutableTreeNode rootNode, FileWriter writer) throws IOException {

        Enumeration<TreeNode> nodes = rootNode.children();

        while(nodes.hasMoreElements()) {

            TreeNode node = nodes.nextElement();

            if (node.isLeaf()){

                DefaultMutableTreeNode defaultNode =(DefaultMutableTreeNode) node;

                Object object = defaultNode.getUserObject();

                if(object instanceof ExplorerFile){
                    //System.out.println("Node is instance of ExplorerFile.");

                    String str = ( (ExplorerFile) defaultNode.getUserObject() ).getFile().toString();
                    if(str != null) {
                        writer.write("\nFile");
                        writer.write("\n" + str);
                    }
                }

            } else {
                writer.write("\nFolder");
                writer.write( "\n" + node.toString());
                writeRecursion((DefaultMutableTreeNode) node, writer);
                writer.write("\nEndFolder");

            }


        }
    }

    //finding
}