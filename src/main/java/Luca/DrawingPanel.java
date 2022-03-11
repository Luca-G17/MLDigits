package Luca;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

public class DrawingPanel extends Node {
    private final GridPane pane = new GridPane();
    private final int PANEL_LENGTH = 28;
    private final DrawingSquare[][] activationGrid = new DrawingSquare[PANEL_LENGTH][PANEL_LENGTH];
    private final int RADIUS = 2;

    public DrawingPanel(){
        for (int i = 0; i < PANEL_LENGTH; i++){
            for (int j = 0; j < PANEL_LENGTH; j++){
                DrawingSquare square = new DrawingSquare(j, i, 5, this);
                activationGrid[i][j] = square;
                pane.add(square, i, j, 1, 1);
            }
        }
        pane.toFront();
    }

    public GridPane getPane() {
        return pane;
    }
    public void activateSquaresInRange(int x, int y){
        int minY = Math.max(y - RADIUS, 0);
        int maxY = Math.min(y + RADIUS, PANEL_LENGTH);
        int minX = Math.max(x - RADIUS, 0);
        int maxX = Math.min(x + RADIUS, PANEL_LENGTH);
        for (int i = minY; i < maxY; i++){
            for (int j = minX; j < maxX; j++){
                if (((i - y) * (i - y)) + ((j - x) * (j - x)) < RADIUS * RADIUS) {
                    activationGrid[i][j].activate();
                }
            }
        }
    }
    public void deactivateGrid(){
        for (int i = 0; i < PANEL_LENGTH; i++){
            for (int j = 0; j < PANEL_LENGTH; j++){
                activationGrid[i][j].deactivate();
            }
        }
    }
    public DigitImage getCurrentImage(){
        DfpField dfpField = new DfpField(0);
        Dfp[][] arr = new Dfp[PANEL_LENGTH][PANEL_LENGTH];
        for (int i = 0; i < PANEL_LENGTH; i++){
            for (int j = 0; j < PANEL_LENGTH; j++){
                if (activationGrid[i][j].isActivated())
                    arr[i][j] = dfpField.newDfp(0);
                else
                    arr[i][j] = dfpField.newDfp(1);
            }
        }
        return new DigitImage(arr);
    }
}
