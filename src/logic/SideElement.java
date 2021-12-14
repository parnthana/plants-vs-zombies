package logic;

import entity.Entity;
import entity.Shovel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class SideElement extends Entity {
    private static int cardSelected = -1;
    private int timeoutTime;
    private boolean isDisabled = false;
    private static ImageView selectedBorder;
    private static HashMap<Integer, SideElement> allElements;
    private final int cost;

    public SideElement(int x, int y, String path, int width, int height, int cost) {
        super(x, y, width, height, path);
        this.cost = cost;
    }

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
            path = "/assets/sunflowerCard.png";
            x = 24;
            y = 79;
            SideElement sunflowerCard = new SideElement(x, y, path, width, height, 50);
            sunflowerCard.buildImage(pane);
            sunflowerCard.timeoutTime = 5000;
            SideElement.allElements.put(1, sunflowerCard);
            sunflowerCard.image.setOnMouseClicked(e -> {
                if (!sunflowerCard.isDisabled) {
                    setCardSelected(1);
                    Shovel.getInstance().disable();
                }
            });
        }
        x = 22;
        if (level >= 1) {
            path = "/assets/peashooterCard.png";
            y = 147;
            SideElement peashooterCard = new SideElement(x, y, path, width, height, 100);
            peashooterCard.buildImage(pane);
            peashooterCard.timeoutTime = 6000;
            SideElement.allElements.put(2, peashooterCard);
            peashooterCard.image.setOnMouseClicked(e -> {
                if (!peashooterCard.isDisabled) {
                    setCardSelected(2);
                    Shovel.getInstance().disable();
                }
            });
        }
        if (level >= 2) {
            path = "/assets/wallnutCard.png";
            y = 217;
            SideElement wallnutCard = new SideElement(x, y, path, width, height, 50);
            wallnutCard.buildImage(pane);
            wallnutCard.timeoutTime = 7000;
            SideElement.allElements.put(3, wallnutCard);
            wallnutCard.image.setOnMouseClicked(e -> {
                if (!wallnutCard.isDisabled) {
                    setCardSelected(3);
                    Shovel.getInstance().disable();
                }
            });
        }
        if (level >= 3) {
            path = "/assets/cherrybombCard.png";
            y = 284;
            SideElement cherrybombCard = new SideElement(x, y, path, width, height, 150);
            cherrybombCard.buildImage(pane);
            cherrybombCard.timeoutTime = 15000;
            SideElement.allElements.put(4, cherrybombCard);
            cherrybombCard.image.setOnMouseClicked(e -> {
                if (!cherrybombCard.isDisabled) {
                    setCardSelected(4);
                    Shovel.getInstance().disable();
                }
            });
        }
        if (level >= 4) {
            path = "/assets/repeaterCard.png";
            x = 23;
            y = 352;
            SideElement repeaterCard = new SideElement(x, y, path, width, height, 200);
            repeaterCard.buildImage(pane);
            repeaterCard.timeoutTime = 10000;
            SideElement.allElements.put(5, repeaterCard);
            repeaterCard.image.setOnMouseClicked(e -> {
                if (!repeaterCard.isDisabled) {
                    setCardSelected(5);
                    Shovel.getInstance().disable();
                }
            });
        }
        if (level >= 5) {
            path = "/assets/jalapenoCard.png";
            x = 24;
            y = 420;
            SideElement chilliPepperCard = new SideElement(x, y, path, width, height, 125);
            chilliPepperCard.buildImage(pane);
            chilliPepperCard.timeoutTime = 12000;
            SideElement.allElements.put(6, chilliPepperCard);
            chilliPepperCard.image.setOnMouseClicked(e -> {
                if (!chilliPepperCard.isDisabled) {
                    setCardSelected(6);
                    Shovel.getInstance().disable();
                }
            });

        }
        String border_path = SideElement.class.getResource("/assets/selectedCardBorder.png").toString();
        selectedBorder = new ImageView(new Image(border_path, 110.0, 72.0, false, false));
        pane.getChildren().add(selectedBorder);
        selectedBorder.setVisible(false);
        selectedBorder.setDisable(true);
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
        if (allElements.containsKey(x)) {
            return allElements.get(x);
        } else {
            return null;
        }
    }

    public void setDisabledOn(Pane pane) {
        this.isDisabled = true;
        ImageView im = new ImageView(new Image(getClass().getResource("/assets/lock.png").toString(), 50.0, 50.0, false, false));
        im.setX(this.getX() + 20);
        im.setY(this.getY());
        pane.getChildren().add(im);
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(this.timeoutTime);
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
