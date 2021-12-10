package entity;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class Entity {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected ImageView image;
    protected String path;

    public Entity(int x, int y, int width, int height, String path) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.path = path; //url of image
    }


    public void buildImage(Pane pane) {
        image = new ImageView();
        image.setImage(new Image(path));
        image.setX(x);
        image.setY(y);
        image.setFitHeight(height);
        image.setFitWidth(width);
        pane.getChildren().add(image);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
