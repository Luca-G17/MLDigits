package Luca;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

public class PrimaryController {
    @FXML
    BorderPane borderPane;
    private VBox buttonPanel;
    private VerticalPanelTextField batchCountField;
    private VerticalPanelTextField batchSizeField;
    private VerticalPanelTextField nodeCountField;
    private VerticalPanelTextField layerCountField;
    private VerticalPanelTextField networkNameField;
    private final Label generalOutput = new Label();
    private ImageProcessor processor;

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
        Label welcomeLabel = new Label("Multilayer Perceptron using the MNIST Dataset");
        welcomeLabel.setPadding(new Insets(0, 0, 10, 0));
        Font font = Font.loadFont("file:src/main/resources/Luca/Roboto-Light.ttf", 30);
        welcomeLabel.setFont(font);
        Line line = new Line(0, 0, 700, 0);
        line.setStrokeWidth(4);
        buttonPanel = new VBox();
        buttonPanel.setPadding(new Insets(10, 10, 20, 5));
        buttonPanel.setSpacing(10);
        buttonPanel.setPrefWidth(200);
        VBox titlePanel = new VBox();
        generalOutput.prefHeight(60);
        generalOutput.setWrapText(true);

        batchCountField = new VerticalPanelTextField("Batch Count", "");
        batchSizeField = new VerticalPanelTextField("Batch Size", "");
        nodeCountField = new VerticalPanelTextField("Nodes Per Layer", "");
        layerCountField = new VerticalPanelTextField("Layer Count", "");
        networkNameField = new VerticalPanelTextField("Network Name", "");
        EventHandler<ActionEvent> creatingEvent = (e -> onCreatingClick());
        EventHandler<ActionEvent> trainingEvent = (e -> onTrainingClick());
        EventHandler<ActionEvent> savingEvent = (e -> onSavingClick());
        CustomButton creatingButton = new CustomButton(
                "-fx-background-color: linear-gradient(to bottom left, #fc5523, #f54b64)",
                "CREATE NETWORK",
                creatingEvent
        );
        CustomButton trainingButton = new CustomButton(
                "-fx-background-color: linear-gradient(to bottom left, #fc5523, #f54b64)",
                "TRAIN NETWORK",
                trainingEvent
        );
        CustomButton savingButton = new CustomButton(
                "-fx-background-color: linear-gradient(to bottom left, #0042b5, #4286fc)",
                "SAVE NETWORK",
                savingEvent
        );
        buttonPanel.getChildren().add(layerCountField.getvBox());
        buttonPanel.getChildren().add(nodeCountField.getvBox());
        buttonPanel.getChildren().add(batchCountField.getvBox());
        buttonPanel.getChildren().add(batchSizeField.getvBox());
        buttonPanel.getChildren().add(networkNameField.getvBox());
        buttonPanel.getChildren().add(creatingButton.getButton());
        buttonPanel.getChildren().add(trainingButton.getButton());
        buttonPanel.getChildren().add(savingButton.getButton());
        buttonPanel.getChildren().add(generalOutput);

        titlePanel.getChildren().add(welcomeLabel);
        titlePanel.getChildren().add(line);
        borderPane.setTop(titlePanel);
        borderPane.setLeft(buttonPanel);
    }

    public void onCreatingClick(){
        String nodeCountStr = nodeCountField.getText();
        String layerCountStr = layerCountField.getText();
        String networkName = networkNameField.getText();
        if (!CustomMath.isInteger(nodeCountStr) || !CustomMath.isInteger(layerCountStr) || networkName.length() == 0)
            generalOutput.setText("ERROR: Node/Layer Count inputs invalid");
        else{
            int nodeCount = CustomMath.strToInt(nodeCountStr);
            int layerCount = CustomMath.strToInt(layerCountStr);
            processor = new ImageProcessor(nodeCount, layerCount, networkName);
            generalOutput.setText(String.format("SUCCESS: Network Created, NODES = %d, LAYERS = %d", nodeCount, layerCount));
        }
    }
    public void onTrainingClick(){
        String batchCountStr = batchCountField.getText();
        String batchSizeStr = batchSizeField.getText();
        if (!CustomMath.isInteger(batchCountStr) || !CustomMath.isInteger(batchSizeStr))
            generalOutput.setText("ERROR: Batch inputs invalid");
        else {
            int batchCount = CustomMath.strToInt(batchCountField.getText());
            int batchSize = CustomMath.strToInt(batchSizeField.getText());
            if (batchCount * batchSize > 60000)
                generalOutput.setText("ERROR: Batch Count * Batch Size > 60,000");
            else if (processor == null)
                generalOutput.setText("ERROR: No network to train");
            else {
                generalOutput.setText("PENDING: Network training");
                processor.trainNetwork(batchSize, batchCount);
                generalOutput.setText("SUCCESS: Network training finished");
            }
        }
    }
    public void onSavingClick(){
        if (processor == null)
            generalOutput.setText("ERROR: No network to save");
        else {
            processor.saveNetwork();
            generalOutput.setText("SUCCESS: Network saved");
        }
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
