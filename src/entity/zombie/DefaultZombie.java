package entity.zombie;

import entity.Zombie;

public class DefaultZombie extends Zombie {

    // Constructor
    public DefaultZombie(int x, int y, int lane) {
        super(5, 2, x, y, 68, 118, lane, "/assets/gif/defaultzombie.gif");
    }

}
