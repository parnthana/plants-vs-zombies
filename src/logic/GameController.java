package logic;

import entity.Plant;
import entity.Zombie;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GameController {
    @FXML
    private Label sunCountLabel;
    @FXML
    private ImageView GameMenuLoaderButton;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private int levelNumber;
    @FXML
    private GridPane lawn_grid;
    @FXML
    private ImageView peaShooterBuy;
    @FXML
    private ImageView repeaterBuy;
    @FXML
    private ImageView cherryBombBuy;
    @FXML
    private ImageView jalapenoBuy;
    @FXML
    private ImageView wallnutBuy;
    @FXML
    private ImageView sunBuy;
    @FXML
    private AnchorPane GamePlayRoot;
    @FXML
    private ImageView lawnImage;

    public static boolean gameStatus;
    public static Timeline sunTimeline;
    public static Timeline spZ1;
    public static Timeline spZ2;
    private static Label sunCountDisplay;
    private static double timeElapsed;
    private static int sunCount;
    public static final int LANE1 = 50;
    public static final int LANE2 = 150;
    public static final int LANE3 = 250;
    public static final int LANE4 = 350;
    public static final int LANE5 = 450;
    private static Level level;
    public static List allZombies;
    public static List allPlants;
    public static ArrayList<Integer> zombieList1;
    public static ArrayList<Integer> zombieList2;
    private static DataTable dataTable;
    public static int wonGame = 0;
    private volatile int spawnedZombies = 0;
    public static double numZombiesKilled = 0;
    public static ArrayList<Timeline> animationTimelines;
    public static String theme = "day";
    private Shovel shovel;

    public void initialize() {
        Media wave = new Media(getClass().getResource("/assets/sounds/zombies_are_coming.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(wave);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setStartTime(Duration.seconds(0));
        mediaPlayer.setStopTime(Duration.seconds(5));
        mediaPlayer.play();
        gameStatus = true;
        sunCountDisplay = sunCountLabel;
        allZombies = Collections.synchronizedList(new ArrayList<Zombie>());
        allPlants = Collections.synchronizedList(new ArrayList<Plant>());
    }

    @FXML
    public void initializeData(int levelNumber, DataTable dataTable) {
        wonGame = 0;
        Random rand = new Random();
        this.levelNumber = levelNumber;
        this.level = new Level(levelNumber);
        zombieList1 = dataTable.getZombieList1();
        zombieList2 = dataTable.getZombieList2();
        allPlants = dataTable.getAllPlants();
        allZombies = dataTable.getAllZombie();
        sunCount = dataTable.getSunCount();
        timeElapsed = dataTable.getTimeElapsed();
        animationTimelines = new ArrayList<Timeline>();
        LevelMenuController.status = dataTable.getStatus();
        startAnimations(rand);
        shovel = Shovel.getInstance();
        shovel.buildImage(GamePlayRoot);
        sunCountDisplay.setText(String.valueOf(sunCount));
        this.dataTable = dataTable;
        SidebarElement.getSideBarElements(levelNumber, GamePlayRoot);
        gameProgress();
        if (LevelMenuController.status) {
            fallingSuns(rand);
            spawnZombies1(rand, 15);
            spawnZombies2(rand, 30);
        } else {
            String lawnPath = getClass().getResource("/res/lawn_night.png").toString();
            Image lawn = new Image(lawnPath, 1024, 600, false, false);
            lawnImage.setImage(lawn);
            zombieSpawner1(rand, 25);
            zombieSpawner2(rand, 40);
        }
    }

    public void startAnimations(Random rand) {
        synchronized (allPlants) {
            for (Plant plant : (Iterable<Plant>) allPlants) {
                plant.buildImage(lawn_grid);
                plant.attack(GamePlayRoot);
            }
        }
//        synchronized (allMowers) {
//            Iterator<LawnMower> i = allMowers.iterator();
//            while (i.hasNext()) {
//                LawnMower l = i.next();
//                l.makeImage(GamePlayRoot);
//                l.checkZombie();
//            }
//        }
        synchronized (allZombies) {
            for (Zombie zombie : (Iterable<Zombie>) allZombies) {
                zombie.buildImage(GamePlayRoot);
                zombie.moveZombie();
            }
        }
        numZombiesKilled = level.getTotalZombies() * timeElapsed;
        progressBar.setProgress(timeElapsed);
    }

    public void gameProgress() {
        Timeline gameStatus = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            try {
                timeElapsed = (numZombiesKilled / level.getTotalZombies());
                progressBar.setProgress(timeElapsed);
                if (wonGame == (-1)) {
                    numZombiesKilled = 0;
                    endAnimations();
                    gameLost();
                } else if (wonGame == 0 && allZombies.size() == 0 && l.getTotalZombies() == spawnedZombies) {
                    numZombiesKilled = 0;
                    endAnimations();
                    gameWon();
                } else if (progressBar.getProgress() >= 1) {
                    spZ1.stop();
                    spZ2.stop();
                    endAnimations();
                    gameWon();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        gameStatus.setCycleCount(Timeline.INDEFINITE);
        gameStatus.play();
        animationTimelines.add(gameStatus);
    }

}
