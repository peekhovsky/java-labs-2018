package main.java.com.explorer;

import javax.swing.*;

/**
 * This class is program start point.
 * @author Rostislav Pekhovsky
 * */
public class Main {

    /**Opens UserControl to define a type of user, than UserControl opens Explorer class
     * @see UserControl */
    public static void main(String[] args) {

            SwingUtilities.invokeLater(() -> new UserControl());
    }
}