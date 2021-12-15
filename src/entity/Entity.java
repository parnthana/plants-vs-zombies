package entity;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Entity {

    // Fields
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected ImageView image;
    protected String path;

    // Constructor
    public Entity(int x, int y, int width, int height, String path) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.path = getClass().getResource(path).toString(); // Url of image
    }

    // Methods
    public void buildImage(Pane pane) {
        image = new ImageView();
        image.setImage(new Image(path,width,height,false,false));
        image.setX(x);
        image.setY(y);
        pane.getChildren().add(image);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        image.setX(x);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        image.setY(y);
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getPath() {
        return path;
    }

}
