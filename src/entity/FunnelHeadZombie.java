package entity;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import logic.Sprites;

public class FunnelHeadZombie extends Zombie{

    public FunnelHeadZombie(int x, int y, int lane) {
        super(12, 2, x, y, 134, 124, lane,"/assets/coneheadzombie.gif");
        this.path = "/assets/coneheadzombie.gif";
    }

    @Override
    public int getSymbol() {
        return Sprites.FUNNELHEADZOMBIE;
    }

    @Override
    public void eatPlant() {
        super.eatPlant();
    }
}
