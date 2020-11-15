package unsw.gloriaromanus;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class GloriaRomanusApplication extends Application {

  private static String configPath = "src/unsw/gloriaromanus/unit_config.json";

  @Override
  public void start(Stage stage) throws IOException {

    StartScreen startScreen = new StartScreen(stage);
    MainScreen mainScreen = new MainScreen(stage);
    RecruitScreen recruitScreen = new RecruitScreen(stage);
    InvadeScreen invadeScreen = new InvadeScreen(stage);
    LostScreen lostScreen = new LostScreen(stage);

    startScreen.getController().setMainScreen(mainScreen);

    mainScreen.getController().setStartScreen(startScreen);
    mainScreen.getController().setRecruitScreen(recruitScreen);
    mainScreen.getController().setInvadeScreen(invadeScreen);
    mainScreen.getController().setLostScreen(lostScreen);
    mainScreen.getController().loadConfig(configPath);
    
    recruitScreen.getController().setMainScreen(mainScreen);
    recruitScreen.getController().loadPrices(configPath);

    invadeScreen.getController().setMainScreen(mainScreen);

    lostScreen.getController().setMainScreen(mainScreen);

    Audio audio = new Audio();
    startScreen.getController().setAudio(audio);
    mainScreen.getController().setAudio(audio);

    stage.setWidth(1600);
    stage.setHeight(900);

    startScreen.start();
  }

  /**
   * Stops and releases all resources used in application.
   */
  /*@Override
  public void stop() {
    controller.terminate();
  }*/

  /**
   * Opens and runs application.
   *
   * @param args arguments passed to this application
   */
  public static void main(String[] args) {

    Application.launch(args);
  }
}