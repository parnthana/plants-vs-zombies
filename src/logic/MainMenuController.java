package logic;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainMenuController {
    @FXML
    private AnchorPane mainRoot;


    @FXML
    void exitGame() {
        System.exit(0);
    }

    @FXML
    void showLevelMenu() throws Exception {
        AnchorPane Apane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LevelMenu.fxml")));
        mainRoot.getChildren().setAll(Apane);
    }

    @FXML
    void startGame() throws Exception {
        FXMLLoader gamePlayfxml = new FXMLLoader(getClass().getResource("GamePlay.fxml"));
        AnchorPane Apane = gamePlayfxml.load();
        GameController controller = gamePlayfxml.getController();
        controller.initializeData(Main.getMaxLevel(),new GameData(Main.getMaxLevel()));
        mainRoot.getChildren().setAll(Apane);
    }

}

