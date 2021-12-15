package logic;

import entity.*;
import entity.plant.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class GameController {
    @FXML
    private AnchorPane GamePlayRoot;
    @FXML
    private ImageView lawnImage;
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

    public static boolean gameStatus;
    public static Timeline sunTimeline;
    public static Timeline spawnZombies1;
    public static Timeline spawnZombies2;
    private static Label sunCountDisplay;
    private double timeElapsed;
    private static int sunCount;
    public static final int LANE1 = 50;
    public static final int LANE2 = 150;
    public static final int LANE3 = 250;
    public static final int LANE4 = 350;
    public static final int LANE5 = 450;
    private GameEntity level;
    public static List allZombies;
    public static List allPlants;
    public static ArrayList<Integer> zombieList1;
    public static ArrayList<Integer> zombieList2;
    private GameData data;
    public static int wonGame;
    public static double numKilledZombies = 0;
    public static ArrayList<Timeline> animationTimelines;
    public static String theme = "day";
    private Shovel shovel;
    private int spawnedZombies = 0;

    public void initialize() {
        Media wave = new Media(getClass().getResource("/sounds/zombies_are_coming.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(wave);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setStartTime(Duration.seconds(0));
        mediaPlayer.setStopTime(Duration.seconds(5));
        mediaPlayer.play();
        sunCountDisplay = sunCountLabel;
        allZombies = Collections.synchronizedList(new ArrayList<Zombie>());
        allPlants = Collections.synchronizedList(new ArrayList<Plant>());
        gameStatus = true;
    }

    @FXML
    public void initializeData(int level, GameData gameData) {
        wonGame = 0;
        animationTimelines = new ArrayList<>();
        zombieList1 = gameData.getZombieList1();
        zombieList2 = gameData.getZombieList2();
        allPlants = gameData.getAllPlants();
        allZombies = gameData.getAllZombie();
        sunCount = gameData.getSunCount();
        timeElapsed = gameData.getTimeElapsed();
        LevelMenuController.status = gameData.getStatus();
        levelNumber = level;
        this.level = new GameEntity(level);
        Random rand = new Random();
        startAnimations();
        shovel = Shovel.getInstance();
        shovel.buildImage(GamePlayRoot);
        sunCountDisplay.setText(String.valueOf(sunCount));
        this.data = gameData;
        SideElement.getSideElements(level, GamePlayRoot);
        gameProgress();
        if (LevelMenuController.status) {
            fallingSuns(rand);
            zombieSpawner1(rand, 25);
            zombieSpawner2(rand, 40);
        } else {
            String lawnPath = getClass().getResource("/images/lawn_night.png").toString();
            Image lawn = new Image(lawnPath, 1068, 600, false, false);
            lawnImage.setImage(lawn);
            zombieSpawner1(rand, 25);
            zombieSpawner2(rand, 40);
        }
    }

    public void startAnimations() {
        synchronized (allPlants) {
            for (Plant plant : (Iterable<Plant>) allPlants) {
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
        }
        synchronized (allZombies) {
            for (Zombie zombie : (Iterable<Zombie>) allZombies) {
                zombie.buildImage(GamePlayRoot);
                zombie.moveZombie();
            }
        }
        numKilledZombies = level.getTotalZombies() * timeElapsed;
        progressBar.setProgress(timeElapsed);
    }

    public void gameProgress() {
        Timeline timelineStatus = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            try {
                timeElapsed = (numKilledZombies / level.getTotalZombies());
                progressBar.setProgress(timeElapsed);
                if (wonGame == -1) {
                    numKilledZombies = 0;
                    endAnimations();
                    gameLost();
                } else if (wonGame == 0 && allZombies.size() == 0 && level.getTotalZombies() == spawnedZombies) {
                    numKilledZombies = 0;
                    endAnimations();
                    gameWon();
                } else if (progressBar.getProgress() >= 1) {
                    spawnZombies1.stop();
                    spawnZombies2.stop();
                    endAnimations();
                    gameWon();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        timelineStatus.setCycleCount(Timeline.INDEFINITE);
        timelineStatus.play();
        animationTimelines.add(timelineStatus);
    }

    public void gameLost() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EndGame.fxml"));
        AnchorPane Apane = fxmlLoader.load();
        EndGameController endController = fxmlLoader.getController();
        endController.endGameUI(levelNumber, false);
        GamePlayRoot.getChildren().setAll(Apane);

    }

    public void gameWon() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EndGame.fxml"));
        AnchorPane Apane = fxmlLoader.load();
        EndGameController controller = fxmlLoader.getController();
        controller.endGameUI(levelNumber, true);
        GamePlayRoot.getChildren().setAll(Apane);

    }

    public static void endAnimations() {
        for (Timeline animationTimeline : animationTimelines) {
            animationTimeline.stop();
        }
    }

    public void endZombieSpawn1() {
        spawnZombies1.stop();
    }

    public void endZombieSpawn2() {
        spawnZombies2.stop();
    }

    @FXML
    void GameMenuLoader() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameMenu.fxml"));
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(fxmlLoader.load()));
        GameMenuController controller = fxmlLoader.getController();
        controller.initializeData(GamePlayRoot, levelNumber, data, sunCount, allPlants, allZombies, timeElapsed, level.getZombieList1(), level.getZombieList2());
        stage.show();
    }

    public static void updateSunCount(int val) {
        sunCount += val;
        getSunCountLabel().setText(Integer.toString(sunCount));
    }

    public static Label getSunCountLabel() {
        return sunCountDisplay;
    }

    public static void removePlant(Plant plant) {
        plant.setHealthpoint(0);
        if (plant instanceof SunFlower) {
            ((SunFlower) plant).checkHealthPoint();
        } else if (plant instanceof Wallnut) {
            ((Wallnut) plant).checkHealthPoint();
        } else {
            ((Shooter) plant).checkHealthPoint();
        }
        allPlants.remove(plant);
    }

    public static void removeZombie(Zombie zombie) {
        zombie.getImage().setVisible(false);
        allZombies.remove(zombie);
    }

    public void fallingSuns(Random ran) {
        Timeline sunDrop = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            int sunPosition = ran.nextInt(950);
            Sun sun = new Sun(sunPosition, 0, true);
            sun.buildImage(GamePlayRoot);
            sun.fallingSun();
        }));
        sunDrop.setCycleCount(Timeline.INDEFINITE);
        sunDrop.play();
        sunTimeline = sunDrop;
        animationTimelines.add(sunDrop);
    }

    public void zombieSpawner1(Random rand, double time) {
        Timeline spawnZombie1 = new Timeline(new KeyFrame(Duration.seconds(time), event -> {
            int lane;
            int laneNumber = rand.nextInt(5);
            if (laneNumber == 0)
                lane = LANE1;
            else if (laneNumber == 1)
                lane = LANE2;
            else if (laneNumber == 2)
                lane = LANE3;
            else if (laneNumber == 3)
                lane = LANE4;
            else
                lane = LANE5;
            try {
                if (zombieList1.get(0) == 1) {
                    GameEntity.spawnDefaultZombie(GamePlayRoot, lane, laneNumber);
                    zombieList1.remove(0);
                    this.spawnedZombies += 1;
                } else if (zombieList1.get(0) == 2) {
                    GameEntity.spawnFunnelHeadZombie(GamePlayRoot, lane, laneNumber);
                    zombieList1.remove(0);
                    this.spawnedZombies += 1;
                } else if (zombieList1.get(0) == 3) {
                    GameEntity.spawnBucketHeadZombie(GamePlayRoot, lane, laneNumber);
                    zombieList1.remove(0);
                    this.spawnedZombies += 1;
                }
            } catch (IndexOutOfBoundsException e) {
                endZombieSpawn1();
            }
        }));

        spawnZombie1.setCycleCount(Timeline.INDEFINITE);
        spawnZombie1.play();
        spawnZombies1 = spawnZombie1;
        animationTimelines.add(spawnZombie1);
    }

    public void zombieSpawner2(Random rand, double time) {
        Timeline spawnZombie2 = new Timeline(new KeyFrame(Duration.seconds(time), event -> {
            int lane;
            int laneNumber = rand.nextInt(5);
            if (laneNumber == 0)
                lane = LANE1;
            else if (laneNumber == 1)
                lane = LANE2;
            else if (laneNumber == 2)
                lane = LANE3;
            else if (laneNumber == 3)
                lane = LANE4;
            else
                lane = LANE5;
            try {
                if (zombieList2.get(0) == 1) {
                    GameEntity.spawnDefaultZombie(GamePlayRoot, lane, laneNumber);
                    zombieList2.remove(0);
                    this.spawnedZombies += 1;
                } else if (zombieList2.get(0) == 2) {
                    GameEntity.spawnFunnelHeadZombie(GamePlayRoot, lane, laneNumber);
                    zombieList2.remove(0);
                    this.spawnedZombies += 1;
                } else if (zombieList2.get(0) == 3) {
                    GameEntity.spawnBucketHeadZombie(GamePlayRoot, lane, laneNumber);
                    zombieList2.remove(0);
                    this.spawnedZombies += 1;
                }
            } catch (IndexOutOfBoundsException e) {
                endZombieSpawn2();
            }
        }));

        spawnZombie2.setCycleCount(Timeline.INDEFINITE);
        spawnZombie2.play();
        animationTimelines.add(spawnZombie2);
        spawnZombies2 = spawnZombie2;
    }

    @FXML
    void getGridPosition(MouseEvent event) {
        Node source = (Node) event.getSource();
        Integer rowIndex = lawn_grid.getRowIndex(source);
        Integer colIndex = lawn_grid.getColumnIndex(source);
        if (!shovel.IsDisabled()) {
            if (colIndex != null && rowIndex != null) {
                Media jostle = new Media(getClass().getResource("/sounds/plant.wav").toString());
                MediaPlayer mediaPlayer = new MediaPlayer(jostle);
                mediaPlayer.setAutoPlay(true);
                mediaPlayer.play();
                synchronized (allPlants) {
                    for (Plant plant : (Iterable<Plant>) allPlants) {
                        if (plant.getColumn() == colIndex && plant.getRow() == rowIndex) {
                            removePlant(plant);
                            break;
                        }
                    }
                }
                shovel.disable();
            }
        }
        if (SideElement.getCardSelected() != -1) {
            if (colIndex != null && rowIndex != null) {
                boolean drop = true;
                synchronized (allPlants) {
                    for (Plant plant : (Iterable<Plant>) allPlants) {
                        if (plant.getColumn() == colIndex && plant.getRow() == rowIndex) {
                            drop = false;
                            break;
                        }
                    }
                }
                if (drop) {
                    if (SideElement.getElement(SideElement.getCardSelected()).getCost() <= sunCount) {
                        dropPlant(SideElement.getCardSelected(), (int) (source.getLayoutX() + source.getParent().getLayoutX()), (int) (source.getLayoutY() + source.getParent().getLayoutY()), colIndex, rowIndex);
                        updateSunCount((-1) * SideElement.getElement(SideElement.getCardSelected()).getCost());
                        SideElement.getElement(SideElement.getCardSelected()).setDisabledOn(GamePlayRoot);
                    }
                }
            }
            SideElement.setCardSelectedToNull();
        }

    }

    public void dropPlant(int value, int x, int y, int col, int row) {
        Plant plant;
        Media plantSound = new Media(getClass().getResource("/sounds/plant.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(plantSound);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.play();
        switch (value) {
            case 1 -> {
                plant = new SunFlower(x, y, col, row);
                allPlants.add(plant);
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
            case 2 -> {
                plant = new PeaShooter(x, y, col, row);
                allPlants.add(plant);
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
            case 3 -> {
                plant = new Wallnut(x, y, col, row);
                allPlants.add(plant);
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
            case 4 -> {
                plant = new CherryBomb(x, y, col, row);
                allPlants.add(plant);
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
            case 5 -> {
                plant = new Repeater(x, y, col, row);
                allPlants.add(plant);
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
            case 6 -> {
                plant = new ChilliPepper(x, y, col, row);
                allPlants.add(plant);
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
        }

    }



}
