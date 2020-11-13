package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainScreen {

    private Stage stage;
    private String title;
    private GloriaRomanusController controller;
    private Scene scene;

    public MainScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Gloria Romanus";

        controller = new GloriaRomanusController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        loader.setController(controller);

        // load into a Parent node called root
        Parent root = loader.load();
        scene = new Scene(root, 1600, 900);
    }

    public void start() throws IOException {
        controller.newGame();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void load() throws IOException {
        controller.loadGame();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public GloriaRomanusController getController() {
        return controller;
    }
}

