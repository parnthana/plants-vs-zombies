package entity;

import logic.Sprites;

public class BucketHeadZombie extends Zombie {
    public BucketHeadZombie(int x, int y, int lane) {
        super(22, 4, x, y, 65, 120, lane, "/assets/bucketheadzombie.gif");
        this.path = "/assets/bucketheadzombie.gif";
    }

    @Override
    public int getSymbol() {
        return Sprites.BUCKETHEADZOMBIE;
    }
}
