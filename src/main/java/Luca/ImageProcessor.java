package Luca;

import java.util.ArrayList;
import java.util.List;

public class ImageProcessor {

    private final List<TrainingImage> trainingImages = new ArrayList<>();
    private static final int TOTAL_BATCHES = 30;
    private static final int BATCH_SIZE = 10;
    public List<TrainingImage> getTrainingImages() {
        return trainingImages;
    }
    public void addTrainingImage(TrainingImage t){
       trainingImages.add(t);
    }
    private final Network network;
    private final BinaryFileReaderWriter fileReaderWriter = new BinaryFileReaderWriter(
            "train-images.idx3-ubyte",
            "train-labels.idx1-ubyte",
            "/home/lucag/Documents/HomeStuff/JavaProjects/MLDigits/target/classes/Luca/Network1"
    );
    public ImageProcessor(){
        network = new Network(16, 2);
        trainNetwork();
        //fileReaderWriter.ReadMats(0, 36, this);
        //network = fileReaderWriter.readNetwork();
        testNetworkOnImage(1);
    }
    public void testNetworkOnImage(int i){
        if (i < trainingImages.size()){
            int x = network.computeNetwork(trainingImages.get(i));
            System.out.printf("The network sees a %d", x);
            System.out.println();
            ConsolePrinter.printImage(trainingImages.get(i));
        }
    }
    public void trainNetwork(){
        for (int i = 0; i < TOTAL_BATCHES; i++){
            fileReaderWriter.ReadMats(i * BATCH_SIZE, BATCH_SIZE, this);
            network.trainNetworkOnBatch(trainingImages.subList(i * BATCH_SIZE, i * BATCH_SIZE + BATCH_SIZE));
        }
        fileReaderWriter.writeNetwork(network);
    }
}
