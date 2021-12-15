package entity;

import entity.base.Attackable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import logic.GameController;

import java.util.Objects;


public abstract class Zombie extends Entity implements Attackable {

    // Fields
    private int health;
    private int attackPower;
    private int lane;
    private int dx;
    private Timeline zombieAnimation;
    private Timeline eating;
    private boolean reachedPlant = false;
    private boolean isEating = false;

    // Constructor
    public Zombie(int health, int attackPower, int x, int y, int width, int height, int lane, String path) {
        super(x, y, width, height, path);
        this.health = health;
        this.attackPower = attackPower;
        this.lane = lane;
        dx = -1;
        eating = new Timeline();
    }

    // Methods
    public void setHealth(int health) {
        this.health = health;
        Platform.runLater(()-> {
            if (health <= 0) {
                ++GameController.numKilledZombies;
                image.setVisible(false);
                image.setDisable(true);
                zombieAnimation.stop();
                if (eating != null) {
                    eating.stop();
                }
                for (Object zombie : GameController.allZombies) {
                    if (this == zombie) {
                        Media yuckSound = new Media(getClass().getResource("/sounds/yuck.wav").toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(yuckSound);
                        mediaPlayer.setAutoPlay(true);
                        mediaPlayer.play();
                        GameController.allZombies.remove(zombie);
                        break;
                    }
                }
            }
        });
        if (health <= 7) {
            image.setImage( new Image(Objects.requireNonNull(getClass().getResource("/gif/defaultzombie.gif")).toString(),65,115,false,false));
            width = 65;
            height = 115;
        }
    }

    public void burntZombie() {
        image.setImage( new Image(getClass().getResource("/gif/burntZombie.gif").toString(),65,115,false,false));
        health = 0;
        dx = 0;
        eating.stop();
        ++GameController.numKilledZombies;
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
            image.setVisible(false);
            image.setDisable(true);
        }).start();

    }

    public void ReachedHouse() {
        if (image.getX() <= 220) {
            Media brainzSound = new Media(getClass().getResource("/sounds/brainz.wav").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(brainzSound);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
            GameController.wonGame = -1;
        }
    }

    public void chompPlant() {
        Media chomp = new Media(getClass().getResource("/sounds/chomp.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(chomp);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setStartTime(Duration.seconds(0));
        mediaPlayer.setStopTime(Duration.seconds(1));
        mediaPlayer.setCycleCount(1);
        mediaPlayer.play();
    }

    public void moveZombie() {
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(70), e -> zombieWalk()));
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        this.zombieAnimation = animation;
        GameController.animationTimelines.add(animation);
    }

    public void zombieWalk() {
        if (getX() > 220 && health > 0) {
            setX(getX() + dx);
            try {
                attacking();
            } catch (java.util.ConcurrentModificationException e) {
//                e.printStackTrace();
            }
            ReachedHouse();
        }
    }

    public void eatPlant() {
        boolean foundPlant = false;
        synchronized (GameController.allPlants) {
            for (Object p : GameController.allPlants) {
                Plant plant = (Plant) p;
                if (plant.getRow() == getLane()) {
                    if (Math.abs(plant.getX() - image.getX()) <= 50) {
                        foundPlant = true;
                        if (!reachedPlant) {
                            reachedPlant = true;
                            isEating = true;
                        }
                        if (isEating) {
                            Timeline eat = new Timeline(new KeyFrame(Duration.millis(1000), e -> chompPlant()));
                            eat.setCycleCount(1000);
                            eat.play();
                            this.dx = 0;
                            this.eating = eat;
                            GameController.animationTimelines.add(eat);
                            isEating = false;
                        }
                        this.dx = 0;
                        plant.setHealthpoint(plant.getHealthpoint() - attackPower);
                        if (plant.getHealthpoint() <= 0) {
                            plant.setHealthpoint(0);
                            GameController.allPlants.remove(plant);
                            plant.image.setVisible(false);
                            plant.image.setDisable(true);
                            this.dx = -1;
                            this.reachedPlant = false;
                            this.eating.stop();
                        }
                    } else {
                        dx = -1;
                        reachedPlant = false;
                        if (eating != null) {
                            eating.stop();
                        }
                    }
                } else {
                    dx = -1;
                }
            }
        }
        if (!foundPlant) {
            dx = -1;
            if (eating != null) {
                eating.stop();
            }
            reachedPlant = false;
        }
    }

    public int getHealth() {
        return health;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public Timeline getZombieAnimation() {
        return zombieAnimation;
    }
}
