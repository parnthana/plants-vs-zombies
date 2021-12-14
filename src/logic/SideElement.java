package logic;

import entity.Entity;
import entity.Shovel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class SideElement extends Entity {

    // Fields
    private static int cardSelected = -1;
    private int cooldownTime;
    private boolean isDisabled = false;
    private static ImageView selectedBorder;
    private static HashMap<Integer, SideElement> allElements;
    private final int cost;

    // Constructor
    public SideElement(int x, int y, String path, int width, int height, int cost) {
        super(x, y, width, height, path);
        this.cost = cost;
    }

    // Methods
    public int getCost() {
        return this.cost;
    }

    public static void getSideElements(int level, Pane pane) {
        String path;
        int x;
        int y;
        int width = 97;
        int height = 58;
        SideElement.allElements = new HashMap<>();
        if (level >= 1) {
            path = "/images/sunflowerCard.png";
            x = 24;
            y = 79;
            SideElement sunflowerCard = new SideElement(x, y, path, width, height, 50);
            sunflowerCard.buildImage(pane);
            sunflowerCard.cooldownTime = 5000;
            SideElement.allElements.put(1, sunflowerCard);
            sunflowerCard.image.setOnMouseClicked(e -> {
                handler(sunflowerCard, 1);
            });
        }
        x = 22;
        if (level >= 1) {
            path = "/images/peashooterCard.png";
            y = 147;
            SideElement peashooterCard = new SideElement(x, y, path, width, height, 100);
            peashooterCard.buildImage(pane);
            peashooterCard.cooldownTime = 6000;
            SideElement.allElements.put(2, peashooterCard);
            peashooterCard.image.setOnMouseClicked(e -> {
                handler(peashooterCard, 2);
            });
        }
        if (level >= 2) {
            path = "/images/wallnutCard.png";
            y = 217;
            SideElement wallnutCard = new SideElement(x, y, path, width, height, 50);
            wallnutCard.buildImage(pane);
            wallnutCard.cooldownTime = 7000;
            SideElement.allElements.put(3, wallnutCard);
            wallnutCard.image.setOnMouseClicked(e -> {
                handler(wallnutCard, 3);
            });
        }
        if (level >= 3) {
            path = "/images/cherrybombCard.png";
            y = 284;
            SideElement cherrybombCard = new SideElement(x, y, path, width, height, 150);
            cherrybombCard.buildImage(pane);
            cherrybombCard.cooldownTime = 15000;
            SideElement.allElements.put(4, cherrybombCard);
            cherrybombCard.image.setOnMouseClicked(e -> {
                handler(cherrybombCard, 4);
            });
        }
        if (level >= 4) {
            path = "/images/repeaterCard.png";
            x = 23;
            y = 352;
            SideElement repeaterCard = new SideElement(x, y, path, width, height, 200);
            repeaterCard.buildImage(pane);
            repeaterCard.cooldownTime = 10000;
            SideElement.allElements.put(5, repeaterCard);
            repeaterCard.image.setOnMouseClicked(e -> {
                handler(repeaterCard, 5);
            });
        }
        if (level >= 5) {
            path = "/images/chilliPepperCard.png";
            x = 24;
            y = 420;
            SideElement chilliPepperCard = new SideElement(x, y, path, width, height, 125);
            chilliPepperCard.buildImage(pane);
            chilliPepperCard.cooldownTime = 12000;
            SideElement.allElements.put(6, chilliPepperCard);
            chilliPepperCard.image.setOnMouseClicked(e -> {
                handler(chilliPepperCard, 6);
            });

        }
        String border_path = SideElement.class.getResource("/images/selectedCardBorder.png").toString();
        selectedBorder = new ImageView(new Image(border_path, 110.0, 72.0, false, false));
        pane.getChildren().add(selectedBorder);
        selectedBorder.setVisible(false);
        selectedBorder.setDisable(true);
    }

    public static void handler(SideElement sideElement, int cardSelected) {
        if (!sideElement.isDisabled) {
            setCardSelected(cardSelected);
            Shovel.getInstance().disable();
        }
    }

    public static int getCardSelected() {
        return cardSelected;
    }

    private static void setCardSelected(int i) {
        cardSelected = i;
        selectedBorder.setVisible(true);
        selectedBorder.setX(allElements.get(cardSelected).getX() - 5);
        selectedBorder.setY(allElements.get(cardSelected).getY() - 5);
    }

    public static void setCardSelectedToNull() {
        cardSelected = -1;
        selectedBorder.setVisible(false);
    }

    public static SideElement getElement(int x) {
        return allElements.getOrDefault(x, null);
    }

    public void setDisabledOn(Pane pane) {
        this.isDisabled = true;
        ImageView im = new ImageView(new Image(getClass().getResource("/images/lock.png").toString(), 50.0, 50.0, false, false));
        im.setX(this.getX() + 20);
        im.setY(this.getY());
        pane.getChildren().add(im);
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(this.cooldownTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.isDisabled = false;
            im.setVisible(false);
            im.setDisable(true);
        });
        t.start();

    }
}
