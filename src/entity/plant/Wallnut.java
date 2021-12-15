package entity.plant;

import entity.Plant;
import javafx.scene.layout.Pane;
import logic.GameController;

public class Wallnut extends Plant {

    // Constructor
    public Wallnut(int x, int y, int row, int col) {
        super(x, y, 60, 75, "/gif/walnut_full_life.gif", 400, row, col);
    }

    // Methods
    public void checkHealthPoint() {
        if (getHealthpoint() <= 0) {
            setHealthpoint(0);
            GameController.allPlants.remove(this);
            getImage().setVisible(false);
            getImage().setDisable(true);
        }
    }

    @Override
    public void attacking(Pane pane) {
    }

    @Override
    public void attacking() {
    }
}

