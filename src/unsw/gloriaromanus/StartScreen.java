package unsw.gloriaromanus;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class StartScreen {

    private Stage stage;
    private String title;
    private StartController controller;
    private Scene scene;

    public StartScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Welcome to Gloria Romanus";

        controller = new StartController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("start.fxml"));
        loader.setController(controller);

        // load into a Parent node called root
        Parent root = loader.load();

        // Background video
        String startVidFile = "src/unsw/gloriaromanus/media/genshin900.mp4";
        Media startVideo = new Media(new File(startVidFile).toURI().toString());
        MediaPlayer vidPlayer = new MediaPlayer(startVideo);
        vidPlayer.setAutoPlay(true);  
        vidPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        MediaView mediaView = new MediaView(vidPlayer);

        // Title
        String titleFile = "src/unsw/gloriaromanus/media/title.png";
        Image titleImage = new Image(new File(titleFile).toURI().toString());
        ImageView imageView = new ImageView(titleImage);

        Group group = new Group();
        group.getChildren().add(mediaView);
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

    public StartController getController() {
        return controller;
    }
}

