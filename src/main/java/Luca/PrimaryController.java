package Luca;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

public class PrimaryController {
    @FXML
    BorderPane borderPane;
    @FXML
    Label welcomeLabel;

    private final ImageProcessor processor = new ImageProcessor();

    public void initialize(){
        borderPane.setBackground(new Background(
            new BackgroundFill(
                new RadialGradient(0, 0, 0, 0, 1, true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#81c483")),
                    new Stop(1, Color.web("#fcc200"))),
                CornerRadii.EMPTY,
                Insets.EMPTY
            )));
        //displayImage(processor.getTrainingImages().get(1));
        Font font = Font.loadFont("file:src/main/resources/Luca/Roboto-Light.ttf", 30);
        welcomeLabel.setFont(font);
        // welcomeLabel.setStyle("-fx-border-color:yellow");
    }
    public void displayImage(TrainingImage image){
        // create canvas 140x140
        // Fill with 5x5 rects
        Canvas canvas = new Canvas();
        canvas.setHeight(140);
        canvas.setWidth(140);
        GraphicsContext context = canvas.getGraphicsContext2D();
        Array2DRowFieldMatrix<Dfp> mat = image.getMatrix();
        for (int i = 0; i < mat.getRowDimension(); i++){
            for (int j = 0; j < mat.getColumnDimension(); j++){
                if (mat.getEntry(i, j).isZero())
                    context.setFill(Color.rgb(255, 255, 255));
                else
                    context.setFill(Color.rgb(0, 0, 0));
                context.fillRect(j * 5, i * 5, j * 5 + 5, i * 5 + 5);
            }
        }
        borderPane.setCenter(canvas);
    }
}
