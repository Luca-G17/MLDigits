package Luca;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private final List<NetworkLayer> network = new ArrayList<>();
    private final int layerCount;

    Network(int layerCount) {
        this.layerCount = layerCount;
    }

    Network(int nodeCount, int layerCount){
        this.layerCount = layerCount;
        network.add(new NetworkLayer(nodeCount, 784));
        for (int i = 1; i < layerCount; i++){
            network.add(new NetworkLayer(nodeCount, nodeCount));
        }
        network.add(new NetworkLayer(10, nodeCount));
    }

    public int computeNetwork(TrainingImage image){
        Array2DRowFieldMatrix<Dfp> prevActivation = image.getMatrixAsVector();
        for (NetworkLayer layer : network){
            layer.computeCurrentActivation(prevActivation);
            prevActivation = layer.getNormalisedNodeMatrix();
        }
        return network.get(2).nodeMax();
    }
    public Array2DRowFieldMatrix<Dfp> computeExpectedVector(int label){
        Dfp[][] arr = new Dfp[10][1];
        DfpField dfpField = new DfpField(3);
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
        for (int i = 0; i < images.size(); i++){
            computeBackpropagation(images.get(i));
            computeGradientStep(images.size());
        }
    }
    public void initNetworkGradMats(){
        for (int i = 0; i < network.size(); i++){
            network.get(i).initGradMats();
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
        // ConsolePrinter.printCost(network.get(network.size() - 1), image.getIndex());
    }

    public List<NetworkLayer> getNetwork() {
        return network;
    }
    public void addLayer(NetworkLayer layer){
        network.add(layer);
    }
    public int getLayerCount() {
        return layerCount;
    }
}
