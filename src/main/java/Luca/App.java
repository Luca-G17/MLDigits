package Luca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    private final ImageProcessor processor = new ImageProcessor();
    private final BinaryFileReader trainingReader = new BinaryFileReader(
            "train-images.idx3-ubyte",
            "train-labels.idx1-ubyte"
    );
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();

        trainingReader.ReadMats(0, 5, processor);
        // ConsoleMatrixPrinter.printList(processor.getTrainingImages());
        processor.testNetworkOnImage(2);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}