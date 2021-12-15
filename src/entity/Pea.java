package entity;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import logic.GameController;

import java.util.Iterator;

public class Pea extends Entity {

    // Fields
    private int lane;
    private int plantPosition;
    private Timeline peaAnimation;
    private boolean bombed;

    // Constructor
    public Pea(int x, int y, int plantPosition, int lane) {
        super(x, y, 20, 20, "/images/pea.png");
        this.path = getClass().getResource("/images/pea.png").toString();
        this.plantPosition = plantPosition;
        this.lane = lane;
        bombed = false;
    }

    // Methods
    public void movePea() {
        if (x <= 1050) {
            setX(getX() + 1);
        }
        image.setVisible(plantPosition <= getX());
        checkZombieCollision();
    }

    public void shootPea() {
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(5), e -> movePea()));
        animation.setCycleCount(1050);
        animation.play();
        this.peaAnimation = animation;
        GameController.animationTimelines.add(animation);
    }

    public void checkZombieCollision() {
        synchronized (GameController.allZombies) {
            for (Zombie zombie : (Iterable<Zombie>) GameController.allZombies) {
                if (zombie.getLane() == lane && !bombed) {
                    if (Math.abs(zombie.getX() - getX()) <= 3 && !bombed) {
                        bombed = true;
                        zombie.setHealth(zombie.getHealth() - 1);
                        image.setVisible(false);
                        image.setDisable(true);
                        peaAnimation.stop();
                        Media splat = new Media(getClass().getResource("/sounds/splat3.wav").toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(splat);
                        mediaPlayer.setAutoPlay(true);
                        mediaPlayer.play();
                    }
                }
            }
        }
    }

}

