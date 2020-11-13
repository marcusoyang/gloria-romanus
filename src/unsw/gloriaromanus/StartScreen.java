package unsw.gloriaromanus;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        /*String startVidFile = "src/unsw/gloriaromanus/media/genshin1080.mp4";
        Media startVideo = new Media(new File(startVidFile).toURI().toString());
        MediaPlayer vidPlayer = new MediaPlayer(startVideo);
        vidPlayer.setAutoPlay(true);  
        vidPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        MediaView mediaView = new MediaView(vidPlayer);
        Group group = new Group();
        group.getChildren().add(mediaView);*/

        scene = new Scene(root, 500, 300);
    }

    public void start() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public StartController getController() {
        return controller;
    }
}

