package entity.plant;

import entity.Pea;
import entity.Plant;

import entity.Zombie;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import logic.GameController;

public class Shooter extends Plant {

    // Fields
    protected Timeline shooterTimeline;
    protected int lane;

    // Constructor
    public Shooter(int x, int y, String path, int healthpoint, int width, int height, int column, int row) {
        super(x, y, width, height, path, healthpoint, column, row);
        this.lane = row;
    }

    // Methods
    @Override
    public void attacking(Pane pane) {
        Timeline peaShooter = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                synchronized (GameController.allZombies) {
                    for (Zombie z : (Iterable<Zombie>)GameController.allZombies) {
                        if (z.getLane() == getShooterLane() && getX() <= z.getX()) {
                            int peaStartX = getX() + 50;
                            int peaStartY = getY() + 25;
                            Pea p = new Pea(peaStartX, peaStartY, getX() + 50, row);
                            p.buildImage(pane);
                            p.shootPea();
                            checkHealthPoint();
                        }
                    }
                }
            }
        }));
        peaShooter.setCycleCount(Timeline.INDEFINITE);
        peaShooter.play();
        this.shooterTimeline = peaShooter;
        GameController.animationTimelines.add(peaShooter);
    }

    @Override
    public void attacking() {
    }

    public Timeline getShooterTimeline() {
        return shooterTimeline;
    }

    public int getShooterLane() {
        return lane;
    }

    public void checkHealthPoint() {
        if (getHealthpoint() <= 0) {
            endAnimation(shooterTimeline);
        }
    }

}

