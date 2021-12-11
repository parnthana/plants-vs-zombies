package entity;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class SunFlower extends Plant {
    private Timeline sunProducer;

    public SunFlower(int x, int y, int row, int col) {
        super(x, y, 100, 73, "/res/sunflower.gif", 70, row, col);
        this.path = "/res/sunflower.gif";
    }

    @Override
    public void attacking(Pane pane) {
        produceSun(pane);
    }

    @Override
    public void attack(Pane pane) {
    }

    public void produceSun(Pane pane) {
        Timeline stopShine = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            Glow shine = new Glow();
            image.setEffect(shine);
            shine.setLevel(0.0);
        }));

        Timeline sunProducer = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            checkHealthPoint();
            if (getHealthpoint() > 0) {
                Sun sun = new Sun(getX() + 20, getY() + 38, false);
                sun.buildImage(pane);
            }
            stopShine.play();
        }));
        Timeline startShine = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            Glow shines = new Glow();
            image.setEffect(shines);
            shines.setLevel(0.5);
            sunProducer.play();
        }));
        startShine.setCycleCount(Timeline.INDEFINITE);
        startShine.play();
        this.sunProducer = sunProducer;
        GamePlayController.animationTimelines.add(sunProducer);
        GamePlayController.animationTimelines.add(startShine);
        GamePlayController.animationTimelines.add(stopShine);
    }

    public void checkHealthPoint() {
        if (getHealthpoint() <= 0) {
            endAnimation(getSunProducer());
        }
    }

    public Timeline getSunProducer() {
        return sunProducer;
    }

    @Override
    public void attacking() {
    }
}
