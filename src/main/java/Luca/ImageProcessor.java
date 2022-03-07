package Luca;

import java.util.ArrayList;
import java.util.List;

public class ImageProcessor {

    private final List<TrainingImage> trainingImages = new ArrayList<>();

    public void addTrainingImage(TrainingImage t){
       trainingImages.add(t);
    }
    private final Network network;
    private BinaryFileReaderWriter fileReaderWriter;
    private boolean networkFound;

    public ImageProcessor(int nodes, int layers, String networkName){
        initFileReaderWriter(networkName);
        network = new Network(nodes, layers, networkName);
    }
    public ImageProcessor(String networkName){
        initFileReaderWriter(networkName);
        network = fileReaderWriter.readNetwork();
        networkFound = network == null;
    }

    public boolean isNetworkFound() {
        return networkFound;
    }

    private void initFileReaderWriter(String networkName){
        fileReaderWriter = new BinaryFileReaderWriter(
                "train-images.idx3-ubyte",
                "train-labels.idx1-ubyte",
                networkName
        );
    }
    public void testNetworkOnImage(int i){
        if (i < trainingImages.size()){
            int x = network.computeNetwork(trainingImages.get(i));
            System.out.printf("The network sees a %d", x);
            System.out.println();
            ConsolePrinter.printImage(trainingImages.get(i));
        }
    }
    public void trainNetwork(int batchSize, int totalBatches){
        for (int i = 0; i < totalBatches; i++){
            fileReaderWriter.ReadMats(i * batchSize, batchSize, this);
            network.trainNetworkOnBatch(trainingImages.subList(i * batchSize, i * batchSize + batchSize));
        }
    }
    public int getLayerCount(){
        return network.getNetwork().size() - 1;
    }
    public int getNodeCount(){
        return network.getNetwork().get(0).getNodeCount();
    }
    public void saveNetwork(){
        fileReaderWriter.writeNetwork(network);
    }
    public List<TrainingImage> getTrainingImages() {
        return trainingImages;
    }
}
