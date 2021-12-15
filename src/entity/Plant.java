package entity;

import entity.base.Attackable;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import logic.GameController;

public abstract class Plant extends Entity implements Attackable {

    // Fields
    protected int row;
    protected int column;
    protected int healthpoint;

    // Constructor
    public Plant(int x, int y, int width, int height, String path, int healthpoint, int column, int row) {
        super(x, y, width, height, path);
        this.healthpoint = healthpoint;
        this.column = column;
        this.row = row;
    }

    // Methods
    public void buildImage(GridPane lawn) {
        image = new ImageView();
        image.setImage(new Image(path, width, height, false, false));
        lawn.add(image, column, row, 1, 1);

    }

    public int getHealthpoint() {
        return healthpoint;
    }

    public void setHealthpoint(int healthpoint) {
        this.healthpoint = healthpoint;
        if (this.healthpoint <= 0) {
            GameController.allPlants.remove(this);
            image.setVisible(false);
            image.setDisable(true);
        }
    }
    public abstract void checkHealthPoint();

    public void endAnimation(Timeline t) {
        t.stop();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
