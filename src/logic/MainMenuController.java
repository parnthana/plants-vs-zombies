package logic;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MainMenuController {
    @FXML
    private AnchorPane mainRoot;


    @FXML
    public void exitGame() {
        System.exit(0);
    }

    @FXML
    public void showLevelMenu() throws Exception {
        AnchorPane Apane = FXMLLoader.load(getClass().getResource("LevelMenu.fxml"));
        mainRoot.getChildren().setAll(Apane);
    }

    @FXML
    public void startGame() throws Exception {
        FXMLLoader gamePlayfxml = new FXMLLoader(getClass().getResource("GamePlay.fxml"));
        AnchorPane Apane = gamePlayfxml.load();
        GameController controller = gamePlayfxml.getController();
        controller.initializeData(Main.getMaxLevel(), new GameData(Main.getMaxLevel()));
        mainRoot.getChildren().setAll(Apane);
    }

}

