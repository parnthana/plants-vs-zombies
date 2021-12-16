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
    public void exitGame() {
        System.exit(0);
    }

    @FXML
    public void showLevelMenu() throws Exception {
        AnchorPane Apane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LevelMenu.fxml")));
        mainRoot.getChildren().setAll(Apane);
    }

}
