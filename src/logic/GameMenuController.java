package logic;

import entity.Plant;
import entity.Zombie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameMenuController {
    @FXML
    private AnchorPane GamePlayRoot;
    @FXML
    private ImageView restartGameButton;

    @FXML
    private int levelNumber;
    private GameData data;
    public static List<Plant> allPlants;
    private static int sunCount;
    private static List<Zombie> allZombies;
    private static double time;
    private static ArrayList<Integer> zombieList1;
    private static ArrayList<Integer> zombieList2;

    @FXML
    public void initializeData(AnchorPane gamePlayRoot, int levelNumber, GameData d, int sCount, List<Plant> allPlant, List<Zombie> allZombie, double timeElapsed, ArrayList<Integer> zL1, ArrayList<Integer> zL2) {
        this.GamePlayRoot = gamePlayRoot;
        this.levelNumber = levelNumber;
        this.data = d;
        sunCount = sCount;
        allPlants = allPlant;
        allZombies = allZombie;
        time = timeElapsed;
        zombieList1 = zL1;
        zombieList2 = zL2;
    }

    @FXML
    void restartGame(MouseEvent event) throws IOException {
        GameController.gameStatus = false;
        GameController.endAnimations();
        Stage stage = (Stage) restartGameButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GamePlay.fxml"));
        AnchorPane game = fxmlLoader.load();
        GameController controller = fxmlLoader.getController();
        data = new GameData(levelNumber);
        controller.initializeData(levelNumber, data);

        GamePlayRoot.getChildren().setAll(game);
    }

    @FXML
    void showMainMenu(MouseEvent event) throws IOException {
        GameController.gameStatus = false;
        GameController.endAnimations();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainPage.fxml")));
        GamePlayRoot.getChildren().setAll(pane);
        Stage stage = (Stage) restartGameButton.getScene().getWindow();
        stage.close();

    }
}

