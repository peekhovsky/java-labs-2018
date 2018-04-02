package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is program start point.
 * @author Rostislav Pekhovsky 2018
 */
public class Main extends Application {

    /**It is a main class for GUI that controls all elements*/
    private Controller controller;

    /**Initialization of Main. Something like constructor.*/
    @Override
    public void init(){
        System.out.println("Initialization of Main.");
    }

    /**Performs when program closes"*/
    @Override
    public void stop() {
        controller.stop();
        System.out.println("Stop of Main.");
    }

    /**Performs when "launch() is called"
     * Contains creation of window and parameters for that.*/
    @Override
    public void start(Stage stage) {

        stage.setTitle("Harbour");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            controller = fxmlLoader.getController();
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image("file:icon.png"));
            stage.show();

        } catch (IOException ex) {
            System.out.println("Main -> start() -> IOException");
            ex.printStackTrace();
        }
    }

    /**Start method of program*/
    public static void main(String[] args) {
        launch(args);
    }
}
