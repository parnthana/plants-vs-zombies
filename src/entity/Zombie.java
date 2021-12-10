package entity;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public abstract class Zombie extends Entity implements Attackable {
    protected int health;
    protected int attackPower;
    protected int lane;
    protected int dx;
    protected Timeline zombieAnimation;
    protected Timeline eating;
    protected boolean reachedPlant = false;
    protected boolean isEating = false;

    public Zombie(int health, int attackPower, int x, int y, int width, int height, int lane, String path) {
        super(x, y, width, height, path);
        setHealth(health);
        this.attackPower = attackPower;
        this.lane = lane;
        this.dx = -1;
        this.zombieAnimation = new Timeline();
        this.eating = new Timeline();
    }

    public abstract int getSymbol();

    public void setHealth(int health) {
        this.health = health;
        if (health <= 0) { //death
            ++GamePlayController.numZombiesKilled;
            this.image.setVisible(false);
            this.image.setDisable(true);
            this.zombieAnimation.stop();
            if (this.eating != null) {
                this.eating.stop();
            }
            for (Zombie zombie : GamePlayController.allZombies) {
                if (this == zombie) {
                    Media yuckSound = new Media(getClass().getResource("/res/sounds/yuck.wav").toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(yuckSound);
                    mediaPlayer.setAutoPlay(true);
                    mediaPlayer.play();
                    GamePlayController.allZombies.remove(zombie);
                    break;
                }
            }
        }
        if (health <= 5) {
            Image img = new Image(getClass().getResource("/res/defaultzombie.gif").toString();
            image.setImage(img);
            image.setFitHeight(115);
            image.setFitWidth(65);
            this.width = 65;
            this.height = 115;
        }
    }

    public void burntZombie() {
        Image img = new Image(getClass().getResource("/res/burntZombie.gif").toString();
        image.setImage(img);
        image.setFitHeight(115);
        image.setFitWidth(65);
        this.dx = 0;
        this.health = 0;
        this.eating.stop();
        ++GamePlayController.numZombiesKilled;
        new Thread(() -> {
            try {
                Thread.sleep(4500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            image.setVisible(false);
            image.setDisable(true);
        }).start();

    }

    public void ReachedHouse() {
        if (image.getX() <= 220) {
            Media brainzSound = new Media(getClass().getResource("/res/sounds/brainz.wav").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(brainzSound);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.play();
            GamePlayController.wonGame -= 1;
        }
    }

    public void chompPlant() {
        Media chomp = new Media(getClass().getResource("/res/sounds/chomp.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(chomp);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setStartTime(Duration.seconds(0));
        mediaPlayer.setStopTime(Duration.seconds(1));
        mediaPlayer.setCycleCount(1);
        mediaPlayer.play();
    }

    public void moveZombie() {
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(75), e -> zombieWalk()));
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
        this.zombieAnimation = animation;
        GamePlayController.animationTimelines.add(animation);
    }

    public void zombieWalk() {
        if (getX() > 220 && this.health > 0) {
            setX(getX() + this.dx);
            try {
                eatPlant();
            } catch (java.util.ConcurrentModificationException e) {
            }
            ReachedHouse();
        }
    }

    public void eatPlant() {
        boolean foundPlant = false;
        synchronized (GamePlayController.allPlants) {
            Iterator<Plant> plantItr = GamePlayController.allPlants.iterator();
            while (plantItr.hasNext()) {
                Plant plant = plantItr.next();
                if (plant.row == getLane()) {
                    if (Math.abs(plant.getX() - image.getX()) <= 50) {
                        foundPlant = true;
                        reachedPlant = true;
                        isEating = true;

                        Timeline eat = new Timeline(new KeyFrame(Duration.millis(1000), e -> chompPlant()));
                        eat.setCycleCount(1000);
                        eat.play();
                        GamePlayController.animationTimelines.add(eat);
                        this.eating = eat;
                        isEating = false;

                        this.dx = 0;
                        plant.setHealth(plant.getHealth() - this.attackPower);

                        if (plant.getHealth() <= 0) {
                            plant.setHealth(0);
                            GamePlayController.allPlants.remove(plant);
                            plant.image.Visible(false);
                            plant.image.setDisable(true);
                            this.dx = -1;
                            this.reachedPlant = false;
                            this.eating.stop();
                        }
                    } else {
                        this.dx = -1;
                        this.reachedPlant = false;
                        if (this.eating != null) {
                            this.eating.stop();
                        }
                    }
                } else {
                    this.dx = -1;
                }
            }
        }
        if (!foundPlant) {
            this.dx = -1;
            this.reachedPlant = false;
            if (this.eating != null) {
                this.eating.stop();
            }
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
