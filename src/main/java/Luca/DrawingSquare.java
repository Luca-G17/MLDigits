package Luca;

import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DrawingSquare extends Rectangle {
    private boolean activated = false;
    private final int x;
    private final int y;
    private final DrawingPanel panel;
    DrawingSquare(int x, int y, int side, DrawingPanel panel){
        super(side, side);
        this.x = x;
        this.y = y;
        this.panel = panel;

        EventHandler<MouseEvent> draggedOver = this::onSquarePressed;
        this.setOnMouseDragEntered(draggedOver);
    }
    public void onSquarePressed(MouseEvent e){
        panel.activateSquaresInRange(x, y);
    }
    public void activate(){
        activated = true;
        this.setFill(Color.rgb(255, 255, 255));
    }
    public void deactivate(){
        activated = false;
        this.setFill(Color.rgb(0,0,0));
    }

    public boolean isActivated() {
        return activated;
    }
}
