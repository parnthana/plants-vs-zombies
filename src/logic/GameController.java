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
    public static Timeline spawnZombies1;
    public static Timeline spawnZombies2;
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
        level = new Level(levelNumber);
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
        GameController.dataTable = dataTable;
        SidebarElement.getSideBarElements(levelNumber, GamePlayRoot);
        gameProgress();
        if (LevelMenuController.status) {
            fallingSuns(rand);
            zombieSpawner1(rand, 15);
            zombieSpawner2(rand, 30);
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
                    spawnZombies1.stop();
                    spawnZombies2.stop();
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

    public void gameLost() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EndGame.fxml"));
        AnchorPane Apane = fxmlLoader.load();
        EndGameController controller = fxmlLoader.<EndGameController>getController();
        controller.initializeData(levelNumber, false, dataTable);
        GamePlayRoot.getChildren().setAll(Apane);

    }

    public void gameWon() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EndGame.fxml"));
        AnchorPane Apane = fxmlLoader.load();
        EndGameController controller = fxmlLoader.<EndGameController>getController();
        controller.initializeData(levelNumber, true, dataTable);
        GamePlayRoot.getChildren().setAll(Apane);

    }

    public static void endAnimations() {
        for (Timeline animationTimeline : animationTimelines) {
            animationTimeline.stop();
        }
    }

    public synchronized void updateSpawnedZombies() {
        this.spawnedZombies += 1;
    }

    public void endZombieSpawn1() {
        spawnZombies1.stop();
    }

    public void endZombieSpawn2() {
        spawnZombies2.stop();
    }

    @FXML
    void GameMenuLoader(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameMenu.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        GameMenuController controller = fxmlLoader.<GameMenuController>getController();
        controller.initializeData(GamePlayRoot, levelNumber, d, sunCount, allPlants, allZombies, timeElapsed, level.getZombieList1(), level.getZombieList2());
        stage.show();
    }

    public static void updateSunCount(int val) {
        sunCount += val;
        getSunCountLabel().setText(Integer.toString(sunCount));
    }

    public static Label getSunCountLabel() {
        return (sunCountDisplay);
    }

    public static void removePlant(Plant plant) {
        plant.getImage().setVisible(false);
        allPlants.remove(plant);
    }

    public static void removeZombie(Zombie zombie) {
        zombie.getImage().setVisible(false);
        allZombies.remove(zombie);
    }

    public void fallingSuns(Random rand) {
        Timeline sunDrop = new Timeline(new KeyFrame(Duration.seconds(12), event -> {
            int sunPosition = rand.nextInt(850) + 100;
            Sun sun = new Sun(sunPosition, 0, true);
            sun.buildImage(GamePlayRoot);
            sun.fallingSun();
        }));
        sunDrop.setCycleCount(Timeline.INDEFINITE);
        sunDrop.play();
        animationTimelines.add(sunDrop);
        sunTimeline = sunDrop;
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
                if (zombieList1.get(0) == 0) {
                    Level.spawnNormalZombie(GamePlayRoot, lane, laneNumber);
                    zombieList1.remove(0);
                    updateSpawnedZombies();
                } else if (zombieList1.get(0) == 1) {
                    Level.spawnConeZombie(GamePlayRoot, lane, laneNumber);
                    zombieList1.remove(0);
                    updateSpawnedZombies();
                } else if (zombieList1.get(0) == 2) {
                    Level.spawnBucketZombie(GamePlayRoot, lane, laneNumber);
                    zombieList1.remove(0);
                    updateSpawnedZombies();
                }
            } catch (IndexOutOfBoundsException e) {
                endZombieSpawn1();
            }
        }));

        spawnZombie1.setCycleCount(Timeline.INDEFINITE);
        spawnZombie1.play();
        animationTimelines.add(spawnZombie1);
        spawnZombies1 = spawnZombie1;
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
                if (zombieList2.get(0) == 0) {
                    Level.spawnNormalZombie(GamePlayRoot, lane, laneNumber);
                    zombieList2.remove(0);
                    updateSpawnedZombies();
                } else if (zombieList2.get(0) == 1) {
                    Level.spawnConeZombie(GamePlayRoot, lane, laneNumber);
                    zombieList2.remove(0);
                    updateSpawnedZombies();
                } else if (zombieList2.get(0) == 2) {
                    Level.spawnBucketZombie(GamePlayRoot, lane, laneNumber);
                    zombieList2.remove(0);
                    updateSpawnedZombies();
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
        Integer colIndex = lawn_grid.getColumnIndex(source);
        Integer rowIndex = lawn_grid.getRowIndex(source);
        if (!shovel.isIsDisabled()) {
            shovel.disable();
            if (colIndex != null && rowIndex != null) {
                Media jostle = new Media(getClass().getResource("/assets/sounds/plant.wav").toString());
                MediaPlayer mediaPlayer = new MediaPlayer(jostle);
                mediaPlayer.setAutoPlay(true);
                mediaPlayer.play();
                synchronized (allPlants) {
                    for (Object p : allPlants) {
                        Plant plant = (Plant) p;
                        if (plant.getColumn() == colIndex && plant.getRow() == rowIndex) {
                            plant.getImage().setVisible(false);
                            plant.getImage().setDisable(true);
                            plant.setHealthpoint(0);
                            allPlants.remove(plant);
                            ((SunFlower) plant).checkHealthPoint();
                            ((Shooter) plant).checkHealthPoint();
                            ((Wallnut) plant).checkHealthPoint();
                            break;
                        }
                    }
                }
            }
        }
        if (SidebarElement.getCardSelected() != -1) {
            if (colIndex != null && rowIndex != null) {
                boolean flag = true;
                synchronized (allPlants) {
                    for (Object plant : allPlants) {
                        Plant p = (Plant) plant;
                        if (p.getColumn() == colIndex && p.getRow() == rowIndex) {
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag) {
                    if (SidebarElement.getElement(SidebarElement.getCardSelected()).getCost() <= sunCount) {
                        dropPlant(SidebarElement.getCardSelected(), (int) (source.getLayoutX() + source.getParent().getLayoutX()), (int) (source.getLayoutY() + source.getParent().getLayoutY()), colIndex, rowIndex);
                        updateSunCount((-1) * SidebarElement.getElement(SidebarElement.getCardSelected()).getCost());
                        SidebarElement.getElement(SidebarElement.getCardSelected()).setDisabledOn(GamePlayRoot);
                    }
                }
            }
            SidebarElement.setCardSelectedToNull();
        }

    }

    public void dropPlant(int value, int x, int y, int row, int col) {
        Plant plant;
        Media plantSound = new Media(getClass().getResource("/assets/sounds/plant.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(plantSound);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.play();
        switch (value) {
            case 1 -> {
                plant = new SunFlower(x, y, row, col);
                allPlants.add(plant);
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
            case 2 -> {
                plant = new PeaShooter(x, y, row, col);
                allPlants.add(plant);
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
            case 3 -> {
                plant = new Wallnut(x, y, row, col);
                allPlants.add(plant);
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
            case 4 -> {
                plant = new CherryBomb(x, y, row, col);
                allPlants.add(plant);
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
            case 5 -> {
                plant = new Repeater(x, y, row, col);
                allPlants.add(plant);
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
            case 6 -> {
                plant = new ChilliPepper(x, y, row, col);
                allPlants.add(plant);
                plant.buildImage(lawn_grid);
                plant.attacking(GamePlayRoot);
            }
        }
    }

}
