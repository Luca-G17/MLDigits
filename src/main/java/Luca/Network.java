package Luca;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private final List<NetworkLayer> network = new ArrayList<>();

    private final int nodeCount;
    private final int layerCount;

    Network(int nodeCount, int layerCount){
        this.layerCount = layerCount;
        this.nodeCount = nodeCount;
        network.add(new NetworkLayer(nodeCount, 784));
        for (int i = 1; i < layerCount; i++){
            network.add(new NetworkLayer(nodeCount, nodeCount));
        }
        network.add(new NetworkLayer(9, nodeCount));
    }

    public int computeNetwork(TrainingImage image){
        Array2DRowFieldMatrix<Dfp> prevActivation = image.getMatrixAsVector();
        for (NetworkLayer layer : network){
            layer.computeCurrentActivation(prevActivation);
            prevActivation = layer.getNormalisedNodeMatrix();
        }
        return network.get(2).nodeMax();
    }
    public Array2DRowFieldMatrix computeExpectedVector(int label){
        Dfp[][] arr = new Dfp[9][1];
        DfpField dfpField = new DfpField(3);
        for (int i = 0; i < 9; i++){
            if (i == label)
                arr[i][0] = dfpField.newDfp(1);
            else
                arr[i][0] = dfpField.newDfp(0);
        }
        return new Array2DRowFieldMatrix<>(arr);
    }
    public void computeBackpropNetwork(TrainingImage image){
        computeNetwork(image);
        network.get(network.size() - 1).backpropagation(computeExpectedVector(image.getDigit()));
        for (int i = network.size() - 2; i >= 0; i--){
            NetworkLayer subMat = network.get(i + 1);
            network.get(i).backpropagation(subMat.getdCostWRTActivation(), subMat.getWeightMatrix());
        }
    }
}
