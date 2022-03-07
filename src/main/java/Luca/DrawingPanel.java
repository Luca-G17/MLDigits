package Luca;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class DrawingPanel {
    private GridPane pane = new GridPane();
    private boolean[][] activationGrid = new boolean[28][28];
    private final int radius = 5;

    public DrawingPanel(){
        EventHandler<MouseEvent> pressedEvent = (this::onSquarePressed);
        for (int i = 0; i < 28; i++){
            for (int j = 0; j < 28; j++){
                Rectangle rect = new Rectangle(5, 5);
                rect.setOnMousePressed(pressedEvent);
                pane.add(rect, i, j, 1, 1);
            }
        }
    }
    public void onSquarePressed(MouseEvent e){
        int x = (int) Math.round(e.getSceneX() - pane.getLayoutX() / 5);
        int y = (int) Math.round(e.getSceneY() - pane.getLayoutY() / 5);

    }
    public GridPane getPane() {
        return pane;
    }
    public void activateSquaresInRange(){

    }
}
