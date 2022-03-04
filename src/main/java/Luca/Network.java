package Luca;

import org.apache.commons.math3.analysis.function.Sigmoid;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private List<NetworkLayer> network = new ArrayList<>();
    private int nodeCount;
    private int layerCount;

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
            prevActivation = layer.getNodeMatrix();
        }
        return network.get(2).nodeMax();
    }
}
