package unsw.gloriaromanus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class InvadeScreen {

    private Stage stage;
    private String title;
    private InvadeController controller;
    private Scene scene;

    public InvadeScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Select Units to Invade";

        controller = new InvadeController();        
        FXMLLoader invadeLoader = new FXMLLoader(getClass().getResource("invade.fxml"));
        invadeLoader.setController(controller);

        Parent root = invadeLoader.load();

        // Wallpaper
        String titleFile = "src/unsw/gloriaromanus/media/recruit.jpg";
        Image titleImage = new Image(new File(titleFile).toURI().toString());
        ImageView imageView = new ImageView(titleImage);

        Group group = new Group();
        group.getChildren().add(imageView);
        group.getChildren().add(root);

        scene = new Scene(group, 1600, 900);
    }

    public void start(ArrayList<Unit> units) {
        controller.loadUnits(units);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public InvadeController getController() {
        return controller;
    }
}
