package unsw.gloriaromanus;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        // Wallpaper
        String titleFile = "src/unsw/gloriaromanus/media/recruit.jpg";
        Image titleImage = new Image(new File(titleFile).toURI().toString());
        ImageView imageView = new ImageView(titleImage);

        Group group = new Group();
        group.getChildren().add(imageView);
        group.getChildren().add(root);

        scene = new Scene(group, 1600, 900);
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
