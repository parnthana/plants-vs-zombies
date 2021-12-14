package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    public static MediaPlayer mediaPlayer;
    private static int maxLevel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        addMusic();
        Parent mainPage = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        Scene scene = new Scene(mainPage, 1024, 600);
        primaryStage.setTitle("Plants VS Zombies");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void addMusic() {
        Media sound = new Media(getClass().getResource("/assets/background.wav").toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setStartTime(Duration.seconds(0));
        mediaPlayer.setStopTime(Duration.seconds(50));
        mediaPlayer.play();
    }

    public static int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxlevel) {
        if (Main.maxLevel < maxlevel) {
            Main.maxLevel = maxlevel;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}