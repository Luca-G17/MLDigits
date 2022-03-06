package Luca;

import java.util.ArrayList;
import java.util.List;

public class ImageProcessor {

    private final List<TrainingImage> trainingImages = new ArrayList<>();

    public List<TrainingImage> getTrainingImages() {
        return trainingImages;
    }
    public void addTrainingImage(TrainingImage t){
       trainingImages.add(t);
    }
    private final Network network = new Network(16, 2);


    public void testNetworkOnImage(int i){
        if (i < trainingImages.size()){
            int x = network.computeNetwork(trainingImages.get(i));
            System.out.printf("The network sees a %d", x);
            System.out.println();
            ConsoleMatrixPrinter.printImage(trainingImages.get(i));
        }
    }

}
