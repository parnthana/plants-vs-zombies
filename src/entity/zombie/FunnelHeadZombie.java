package entity.zombie;


import entity.Zombie;
import javafx.scene.layout.Pane;

public class FunnelHeadZombie extends Zombie {

    // Constructor
    public FunnelHeadZombie(int x, int y, int lane) {
        super(12, 2, x, y, 134, 124, lane, "/assets/gif/coneheadzombie.gif");
    }

    // Methods
    @Override
    public void attacking(Pane pane) {
    }

    @Override
    public void attacking() {
        super.attacking();
    }
}
