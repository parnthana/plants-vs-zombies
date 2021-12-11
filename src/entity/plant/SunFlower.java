package entity.plant;

import entity.Plant;
import entity.Sun;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import logic.GameController;

public class SunFlower extends Plant {

    // Field
    private Timeline sunProducer;

    // Constructor
    public SunFlower(int x, int y, int row, int col) {
        super(x, y, 100, 73, "/assets/gif/sunflower.gif", 70, row, col);
    }

    @Override
    public void attacking(Pane pane) {
        produceSun(pane);
    }

    @Override
    public void attacking() {
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
        GameController.animationTimelines.add(sunProducer);
        GameController.animationTimelines.add(startShine);
        GameController.animationTimelines.add(stopShine);
    }

    public void checkHealthPoint() {
        if (getHealthpoint() <= 0) {
            endAnimation(getSunProducer());
        }
    }

    public Timeline getSunProducer() {
        return sunProducer;
    }

}
