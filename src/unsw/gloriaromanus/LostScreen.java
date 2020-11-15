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

public class LostScreen {
    
    private Stage stage;
    private String title;
    private LostController controller;
    private Scene scene;

    public LostScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Player Lost";

        controller = new LostController();        
        FXMLLoader lostLoader = new FXMLLoader(getClass().getResource("lost.fxml"));
        lostLoader.setController(controller);

        // load into a Parent node
        Parent root = lostLoader.load();

        // Wallpaper
        String titleFile = "src/unsw/gloriaromanus/media/lost.jpg";
        Image titleImage = new Image(new File(titleFile).toURI().toString());
        ImageView imageView = new ImageView(titleImage);

        Group group = new Group();
        group.getChildren().add(imageView);
        group.getChildren().add(root);

        scene = new Scene(group, 1600, 900);
    }

    public void start(Player p) {
        controller.displayPlayer(p);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public LostController getController() {
        return controller;
    }
}
