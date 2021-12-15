package logic;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class MainMenuController {
    @FXML
    private AnchorPane mainRoot;


    @FXML
    void exitGame() {
        System.exit(0);
    }

    @FXML
    void showLevelMenu() throws Exception {
        AnchorPane Apane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("logic/LevelMenu.fxml")));
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

