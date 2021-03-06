package Luca;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private final List<NetworkLayer> network = new ArrayList<>();
    private final int layerCount;
    public static final int DECIMAL_DIGITS = 3;
    private String name;

    Network(int layerCount, String name) {
        this.layerCount = layerCount;
        this.name = name;
    }

    Network(int nodeCount, int layerCount, String name){
        this.layerCount = layerCount;
        this.name = name;
        network.add(new NetworkLayer(nodeCount, 784));
        for (int i = 1; i < layerCount; i++){
            network.add(new NetworkLayer(nodeCount, nodeCount));
        }
        network.add(new NetworkLayer(10, nodeCount));
    }

    public int computeNetwork(DigitImage image){
        Array2DRowFieldMatrix<Dfp> prevActivation = image.getMatrixAsVector();
        for (NetworkLayer layer : network){
            layer.computeCurrentActivation(prevActivation);
            prevActivation = layer.getNormalisedNodeMatrix();
        }
        return network.get(2).nodeMax();
    }
    public Array2DRowFieldMatrix<Dfp> computeExpectedVector(int label){
        Dfp[][] arr = new Dfp[10][1];
        DfpField dfpField = new DfpField(DECIMAL_DIGITS);
        for (int i = 0; i < 10; i++){
            if (i == label)
                arr[i][0] = dfpField.newDfp(1);
            else
                arr[i][0] = dfpField.newDfp(0);
        }
        return new Array2DRowFieldMatrix<>(arr);
    }
    public void trainNetworkOnBatch(List<TrainingImage> images){
        initNetworkGradMats();
        for (TrainingImage image : images) {
            computeBackpropagation(image);
        }
        computeGradientStep(images.size());
    }
    public void initNetworkGradMats(){
        for (NetworkLayer networkLayer : network) {
            networkLayer.initGradMats();
        }
    }
    public void computeGradientStep(int batchSize){
        for (NetworkLayer networkLayer : network) {
            networkLayer.stepGradientAndBias(batchSize);
        }
    }
    public void computeBackpropagation(TrainingImage image){
        computeNetwork(image);
        network.get(network.size() - 1).backpropagation(network.get(network.size() - 2).getNormalisedNodeMatrix(), computeExpectedVector(image.getDigit()));
        for (int i = network.size() - 2; i >= 1; i--){
            NetworkLayer subMat = network.get(i + 1);
            network.get(i).backpropagation(network.get(i - 1).getNormalisedNodeMatrix(), subMat.getdCostWRTActivation(), subMat.getWeightMatrix());
        }
        network.get(0).backpropagation(image.getMatrixAsVector(), network.get(1).getdCostWRTActivation(), network.get(1).getWeightMatrix());
        ConsolePrinter.printCost(network.get(network.size() - 1), image.getIndex());
    }

    public List<NetworkLayer> getNetwork() {
        return network;
    }
    public void addLayer(NetworkLayer layer){
        network.add(layer);
    }
}
