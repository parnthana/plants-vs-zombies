package entity;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import logic.GameController;

public class Pea extends Entity {
    private int lane;
    private int plantPosition;
    transient private Timeline peaAnimation;
    private static int peaID = 0;
    private boolean flag;
    private int thispea;

    public Pea(int x, int y, int plantPosition, int lane) {
        super(x, y, 20, 20, "/assets/images/pea.png");
        this.path = getClass().getResource("/assets/pea.png").toString();
        this.plantPosition = plantPosition;
        this.lane = lane;
        thispea = peaID++;
        this.flag = false;
    }

    public void movePea() {
        if (x <= 1050) {
            setX(getX() + 1);
        }
        if (this.plantPosition > getX()) {
            image.setVisible(false);
        } else {
            image.setVisible(true);
        }
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
            for (Object zombie : GameController.allZombies) {
                Zombie z = (Zombie) zombie;
                if (z.getLane() == lane && !flag) {
                    if (Math.abs(z.getX() - getX()) <= 3 && !flag) {
                        this.flag = true;
                        z.setHealth(z.getHealth() - 1);
                        image.setVisible(false);
                        image.setDisable(true);
                        peaAnimation.stop();
                        Media splat = new Media(getClass().getResource("/assets/sounds/splat3.wav").toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(splat);
                        mediaPlayer.setAutoPlay(true);
                        mediaPlayer.play();
                    }
                }
            }
        }
    }

}
