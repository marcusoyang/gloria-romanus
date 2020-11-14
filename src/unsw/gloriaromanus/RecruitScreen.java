package unsw.gloriaromanus;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RecruitScreen {

    private Stage stage;
    private String title;
    private RecruitController controller;
    private Scene scene;

    public RecruitScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Unit Recruitment";

        controller = new RecruitController();        
        FXMLLoader recruitLoader = new FXMLLoader(getClass().getResource("recruitment.fxml"));
        recruitLoader.setController(controller);

        // load into a Parent node
        Parent root = recruitLoader.load();

        scene = new Scene(root, 1600, 900);
    }

    public void start() {
        controller.clearTextFields();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public RecruitController getController() {
        return controller;
    }
}
