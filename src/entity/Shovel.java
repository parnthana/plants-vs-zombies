package entity;

import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import logic.SideElement;

// Singleton class
public class Shovel extends Entity {

    // Fields
    private static boolean isDisabled = true;
    private static Shovel shovel;

    // Singleton class Constructor
    private Shovel() {
        super(680, 10, 58, 58, "/images/Shovel.png");
        this.path = getClass().getResource("/images/Shovel.png").toString();
    }

    // Methods
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
            SideElement.setCardSelectedToNull();
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