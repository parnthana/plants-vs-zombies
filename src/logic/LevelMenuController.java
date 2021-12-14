package logic;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class LevelMenuController {
    @FXML
    private AnchorPane levelRoot;
    @FXML
    private ImageView level1button;
    @FXML
    private ImageView level2button;
    @FXML
    private ImageView level3button;
    @FXML
    private ImageView level4button;
    @FXML
    private ImageView level5button;
    @FXML
    private ImageView lock2;
    @FXML
    private ImageView lock3;
    @FXML
    private ImageView lock4;
    @FXML
    private ImageView lock5;
    @FXML
    private ImageView backbutton;
    @FXML
    private ImageView nightTheme;
    @FXML
    public ImageView nightMode;
    @FXML
    public ImageView dayMode;
    public static boolean status = true;

    public LevelMenuController() {
        if (status) {
            nightTheme.setVisible(false);
            nightMode.setVisible(false);
            nightMode.setDisable(true);
            dayMode.setVisible(true);
            dayMode.setDisable(false);
        } else {
            nightTheme.setVisible(true);
            nightMode.setVisible(true);
            nightMode.setDisable(false);
            dayMode.setVisible(false);
            dayMode.setDisable(true);
            GameController.theme = "night";
        }
        int level = Main.getMaxLevel();
        level2button.setDisable(true);
        level3button.setDisable(true);
        level4button.setDisable(true);
        level5button.setDisable(true);
        if (level >= 2) {
            level2button.setDisable(false);
            lock2.setVisible(false);
            lock2.setDisable(true);
        }
        if (level >= 3) {
            level3button.setDisable(false);
            lock3.setVisible(false);
            lock3.setDisable(true);
        }
        if (level >= 4) {
            level4button.setDisable(false);
            lock4.setVisible(false);
            lock4.setDisable(true);
        }
        if (level >= 5) {
            level5button.setDisable(false);
            lock5.setVisible(false);
            lock5.setDisable(true);
        }

    }

    @FXML
    void shineImage(MouseEvent event) {
        Node source = (Node) event.getSource();
        Glow shine = new Glow();
        source.setEffect(shine);
        shine.setLevel(0.3);
    }

    @FXML
    void stopShining(MouseEvent event) {
        Node source = (Node) event.getSource();
        Glow shine = (Glow) source.getEffect();
        source.setEffect(shine);
        shine.setLevel(0.0);
    }

    @FXML
    void startLevel1() throws IOException {
        FXMLLoader gamePlayfxml = new FXMLLoader(getClass().getResource("GamePlay.fxml"));
        AnchorPane Apane = gamePlayfxml.load();
        GameController controller = gamePlayfxml.getController();
        controller.initializeData(1, new GameData(1));
        levelRoot.getChildren().setAll(Apane);

    }

    @FXML
    void startLevel2() throws IOException {
        FXMLLoader gamePlayfxml = new FXMLLoader(getClass().getResource("GamePlay.fxml"));
        AnchorPane Apane = gamePlayfxml.load();
        GameController controller = gamePlayfxml.getController();
        controller.initializeData(2, new GameData(2));
        levelRoot.getChildren().setAll(Apane);

    }

    @FXML
    void startLevel3() throws IOException {
        FXMLLoader gamePlayfxml = new FXMLLoader(getClass().getResource("GamePlay.fxml"));
        AnchorPane Apane = gamePlayfxml.load();
        GameController controller = gamePlayfxml.getController();
        controller.initializeData(3, new GameData(3));
        levelRoot.getChildren().setAll(Apane);

    }

    @FXML
    void startLevel4() throws IOException {
        FXMLLoader gamePlayfxml = new FXMLLoader(getClass().getResource("GamePlay.fxml"));
        AnchorPane Apane = gamePlayfxml.load();
        GameController controller = gamePlayfxml.getController();
        controller.initializeData(4, new GameData(4));
        levelRoot.getChildren().setAll(Apane);

    }

    @FXML
    void startLevel5() throws IOException {
        FXMLLoader gamePlayfxml = new FXMLLoader(getClass().getResource("GamePlay.fxml"));
        AnchorPane Apane = gamePlayfxml.load();
        GameController controller = gamePlayfxml.getController();
        controller.initializeData(5, new GameData(5));
        levelRoot.getChildren().setAll(Apane);
    }

    @FXML
    void PrevMenuLoader() throws IOException {
        AnchorPane Apane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu.fxml")));
        levelRoot.getChildren().setAll(Apane);
    }

    @FXML
    void changeGameTheme() {
        if (!dayMode.isVisible()) {
            nightMode.setVisible(false);
            nightMode.setDisable(true);
            nightTheme.setVisible(false);
            dayMode.setVisible(true);
            dayMode.setDisable(false);
            status = true;
        } else if (!nightMode.isVisible()) {
            dayMode.setVisible(false);
            dayMode.setDisable(true);
            nightMode.setVisible(true);
            nightMode.setDisable(false);
            nightTheme.setVisible(true);
            status = false;
        }
    }

    public static boolean getDayMode() {
        return status;
    }

}
