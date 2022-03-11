package Luca;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private VBox centrePanel;
    private labeledVerticalPanelElement batchCountField;
    private labeledVerticalPanelElement batchSizeField;
    private labeledVerticalPanelElement nodeCountField;
    private labeledVerticalPanelElement layerCountField;
    private labeledVerticalPanelElement networkNameField;
    private labeledVerticalPanelElement drawingPanel;
    private final Label generalOutput = new Label();
    private final Label networkLabel = new Label("Network: No Network");
    private final Label networkOutputLabel = new Label();
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
        Line horzLine = new Line(0, 0, 700, 0);
        horzLine.setStrokeWidth(4);
        Line vertLine = new Line(0, 0, 0, 550);
        vertLine.setStrokeWidth(4);

        buttonPanel = new VBox();
        centrePanel = new VBox();
        HBox leftPane = new HBox();
        leftPane.setPadding(new Insets(10, 10, 10, 10));
        leftPane.setSpacing(20);
        leftPane.getChildren().add(buttonPanel);
        leftPane.getChildren().add(vertLine);
        buttonPanel.setPadding(new Insets(10, 10, 20, 5));
        centrePanel.setPadding(new Insets(10, 10, 20, 5));
        centrePanel.setSpacing(10);
        centrePanel.setStyle("-fx-border-color: Yellow");
        centrePanel.setMaxWidth(140);
        buttonPanel.setSpacing(10);
        buttonPanel.setPrefWidth(200);
        VBox titlePanel = new VBox();
        generalOutput.prefHeight(60);
        generalOutput.setWrapText(true);
        networkLabel.setWrapText(true);
        networkOutputLabel.setWrapText(true);

        drawingPanel = new labeledVerticalPanelElement("Draw Digit", new DrawingPanel());
        batchCountField = new labeledVerticalPanelElement("Batch Count", new TextField());
        batchSizeField = new labeledVerticalPanelElement("Batch Size",  new TextField());
        nodeCountField = new labeledVerticalPanelElement("Nodes Per Layer", new TextField());
        layerCountField = new labeledVerticalPanelElement("Layer Count", new TextField());
        networkNameField = new labeledVerticalPanelElement("Network Name", new TextField());

        EventHandler<ActionEvent> clearDrawingEvent = (e -> onClearDrawingClick());
        EventHandler<ActionEvent> drawingSelectedEvent = (e -> onDrawingSelectedClick());
        EventHandler<ActionEvent> creatingEvent = (e -> onCreatingClick());
        EventHandler<ActionEvent> trainingEvent = (e -> onTrainingClick());
        EventHandler<ActionEvent> savingEvent = (e -> onSavingClick());
        EventHandler<ActionEvent> loadingEvent = (e -> onLoadingClick());
        CustomButton clearDrawingButton = new CustomButton(
                "-fx-background-color: linear-gradient(to bottom left, #fc5523, #f54b64)",
                "CLEAR",
                clearDrawingEvent
        );
        CustomButton drawingSubmissionButton = new CustomButton(
                "-fx-background-color: linear-gradient(to bottom left, #fc5523, #f54b64)",
                "SUBMIT",
                drawingSelectedEvent
        );
        CustomButton creatingButton = new CustomButton(
                "-fx-background-color: linear-gradient(to bottom left, #fc5523, #f54b64)",
                "NEW NETWORK",
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
        CustomButton loadingButton = new CustomButton(
                "-fx-background-color: linear-gradient(to bottom left, #0042b5, #4286fc)",
                "LOAD NETWORK",
                loadingEvent
        );
        buttonPanel.getChildren().add(networkLabel);
        buttonPanel.getChildren().add(layerCountField.getvBox());
        buttonPanel.getChildren().add(nodeCountField.getvBox());
        buttonPanel.getChildren().add(networkNameField.getvBox());
        buttonPanel.getChildren().add(creatingButton.getButton());
        buttonPanel.getChildren().add(batchCountField.getvBox());
        buttonPanel.getChildren().add(batchSizeField.getvBox());
        buttonPanel.getChildren().add(trainingButton.getButton());
        buttonPanel.getChildren().add(savingButton.getButton());
        buttonPanel.getChildren().add(loadingButton.getButton());
        buttonPanel.getChildren().add(generalOutput);
        centrePanel.getChildren().add(drawingPanel.getvBox());
        centrePanel.getChildren().add(clearDrawingButton.getButton());
        centrePanel.getChildren().add(drawingSubmissionButton.getButton());
        centrePanel.getChildren().add(networkOutputLabel);

        titlePanel.getChildren().add(welcomeLabel);
        titlePanel.getChildren().add(horzLine);
        borderPane.setTop(titlePanel);
        borderPane.setLeft(leftPane);
        BorderPane.setAlignment(centrePanel, Pos.TOP_LEFT);
        borderPane.setCenter(centrePanel);
    }
    public void onClearDrawingClick(){
        ((DrawingPanel)drawingPanel.getNode()).deactivateGrid();
        networkOutputLabel.setText("SUCCESS: Cleared");
    }
    public void onDrawingSelectedClick(){
        DigitImage image = ((DrawingPanel)drawingPanel.getNode()).getCurrentImage();
        if (processor != null){
            int val = processor.runNetworkOnImage(image);
            networkOutputLabel.setText(String.format("SUCCESS: The network sees a %d", val));
            processor.runNetworkOnImage(35);
        }
        else{
            networkOutputLabel.setText(String.format("ERROR: No network"));
        }
    }
    public void onLoadingClick(){
        String networkName = ((TextField) networkNameField.getNode()).getText();
        if (networkName.length() == 0)
            generalOutput.setText("ERROR: Network Name input invalid");
        else{
            processor = new ImageProcessor(networkName);
            if (processor.isNetworkFound()) {
                networkLabel.setText(String.format("Network - %s\nNODES = %d, LAYERS = %d", networkName, processor.getNodeCount(), processor.getLayerCount()));
                generalOutput.setText("SUCCESS: Network loaded");
            }
            else{
                processor = null;
                generalOutput.setText("ERROR: Network doesn't exist");
            }
        }
    }
    public void onCreatingClick(){
        String nodeCountStr = ((TextField) nodeCountField.getNode()).getText();
        String layerCountStr = ((TextField) layerCountField.getNode()).getText();
        String networkName = ((TextField) networkNameField.getNode()).getText();
        if (!CustomMath.isInteger(nodeCountStr) || !CustomMath.isInteger(layerCountStr) || networkName.length() == 0)
            generalOutput.setText("ERROR: Node/Layer Count inputs invalid");
        else{
            int nodeCount = CustomMath.strToInt(nodeCountStr);
            int layerCount = CustomMath.strToInt(layerCountStr);
            if (nodeCount > 0 && layerCount > 1){
                processor = new ImageProcessor(nodeCount, layerCount, networkName);
                generalOutput.setText("SUCCESS: Network Created");
                networkLabel.setText(String.format("Network - %s\nNODES = %d, LAYERS = %d", networkName, processor.getNodeCount(), processor.getLayerCount()));
            }
            else{
                generalOutput.setText("ERROR: Node/Layer Count inputs invalid");
            }
        }
    }
    public void onTrainingClick(){
        String batchCountStr = ((TextField) batchCountField.getNode()).getText();
        String batchSizeStr = ((TextField) batchSizeField.getNode()).getText();
        if (!CustomMath.isInteger(batchCountStr) || !CustomMath.isInteger(batchSizeStr))
            generalOutput.setText("ERROR: Batch inputs invalid");
        else {
            int batchCount = CustomMath.strToInt(batchCountStr);
            int batchSize = CustomMath.strToInt(batchSizeStr);
            if (batchCount * batchSize > 60000)
                generalOutput.setText("ERROR: Batch Count * Batch Size > 60,000");
            else if (batchCount < 1 || batchSize < 1){
                generalOutput.setText("ERROR: Invalid Batch Count or Batch Size input");
            }
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
