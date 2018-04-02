package main.java.com.explorer;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Use this class for convenient operations with file.
 * Created for using in JTree.
 * @author Rostislav Pekhovsky (2018)
*/
public class ExplorerFile {

    //Variables

    /**This object is a backbone. We use this ExplorerFile to make manipulations with files easier*/
    private File file;
    /**This is a name for file. We can change it using method setFileName, and get this by getFileName*/
    private String name;
    /**This is an extension part of file name*/
    private String extension;
    /**This is icon of file loaded from windows explorer*/
    private Icon icon;


    //Constructors

    /**Constructor uses location to load file from a disk.
     * @param location where this file will be in a disk*/
    ExplorerFile(String location){
        file = new File(location);
        icon = FileSystemView.getFileSystemView().getSystemIcon(file);
        extension = getFileExtension(file.toString());
        name = getFileName(file.getName());
    }

    /**Constructor uses File object to create Explorer file like cover.
     * @param newFile existing File object*/
    ExplorerFile(File newFile){
        file = newFile;
        icon = FileSystemView.getFileSystemView().getSystemIcon(file);
        extension = getFileExtension(file.toString());
        name = getFileName(file.getName());
    }

    /**Constructor uses File object to create Explorer file like cover.
     * Sets new adjusted name
     * @param newFile existing File object
     * @param newName name that you want to give*/
    ExplorerFile(File newFile, String newName){
        file = newFile;
        icon = FileSystemView.getFileSystemView().getSystemIcon(file);
        extension = getFileExtension(file.toString());
        name = getFileName(file.getName());
    }


    /**Opens file by default program in computer
     * @exception IOException happens if there is no file in disk
     * @exception IllegalArgumentException if wrong location string
     * @exception NullPointerException if you don't initialize File object*/
    public void open() throws IOException, IllegalArgumentException, NullPointerException {
        Desktop.getDesktop().open(file);
    }

    /**Returns extension from full name.
     * @param str full name of file with extension*/
    @Nullable
    private static String getFileExtension(String str){
        int pointIndex = str.lastIndexOf(".");

        if(pointIndex > 1){
            return str.substring(pointIndex);
        }
        else return null;
    }

    /**Returns name without extension.
     * @param str full name of file with extension*/
    public static String getFileName(String str){
        int pointIndex = str.lastIndexOf(".");

        if(pointIndex > 1){
            return str.substring(0, pointIndex);
        }
        else return str;

    }


    //get-set

    /**Returns name of ExtensionFile instead*/
    @Override
    public String toString(){
        return name;
    }

    /**Sets new File object
     * @param newFile new file that adds instead*/
    public void setFile(File newFile){
        file = newFile;
    }

    public File getFile(){
        return file;
    }

    public Icon getIcon(){
        return icon;
    }

    public String getExtension(){
        return extension;
    }

    public void setFileName(String str){
        name = str;
     }

}
