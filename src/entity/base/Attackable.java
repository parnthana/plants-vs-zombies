package entity.base;

import entity.Entity;
import javafx.scene.layout.Pane;

public interface Attackable {
    void attacking(Pane pane);
    void attacking();
}
