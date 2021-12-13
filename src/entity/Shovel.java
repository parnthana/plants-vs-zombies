package entity;

import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;

//singleton class
public class Shovel extends Entity {
    private static boolean isDisabled = true;
    private static Shovel shovel;

    private Shovel() {
        super(500, 10, 60, 60, "/assets/images/Shovel.png");
        this.path = getClass().getResource("/assets/images/Shovel.png").toString();
    }

    public boolean IsDisabled() {
        return isDisabled;
    }

    public static Shovel getInstance() {
        if (shovel == null) {
            shovel = new Shovel();

        }
        return shovel;
    }

    @Override
    public void buildImage(Pane p) {
        super.buildImage(p);
        shovel.image.setOnMouseClicked(e -> {
            isDisabled = false;
            shovel.enable();
            SidebarElement.setCardSelectedToNull();
        });
    }

    public void enable() {
        Glow glow = new Glow();
        shovel.image.setEffect(glow);
        glow.setLevel(0.4);
    }

    public void disable() {
        if (!isDisabled) {
            Glow glow = (Glow) shovel.image.getEffect();
            glow.setLevel(0.0);
            isDisabled = true;
        }
    }
}