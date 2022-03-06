package Luca;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

import java.util.Random;
import java.util.WeakHashMap;

public class NetworkLayer {
    private final int nodeCount;
    private Array2DRowFieldMatrix<Dfp> normalisedNodeMatrix;
    private Array2DRowFieldMatrix<Dfp> nodeMatrix;
    private final Array2DRowFieldMatrix<Dfp> weightMatrix; // Weights Entering Layer
    private final Array2DRowFieldMatrix<Dfp> biasMatrix; // Bias on Layer

    private Array2DRowFieldMatrix<Dfp> dCostWRTActivation;
    private Array2DRowFieldMatrix<Dfp> dCostWRTWeight;
    private Array2DRowFieldMatrix<Dfp> dCostWRTBias;

    NetworkLayer(int nodeCount, int prevNodeCount) {
        this.nodeCount = nodeCount;
        this.normalisedNodeMatrix = initMatrix(1);
        weightMatrix = randomizeMatrix(prevNodeCount);
        biasMatrix = randomizeMatrix(1);

    }
    private Array2DRowFieldMatrix<Dfp> initMatrix(int x){
        DfpField dfp = new DfpField(3);
        Dfp[][] arr = new Dfp[nodeCount][x];
        for (int i = 0; i < nodeCount; i++){
            for (int j = 0; j < x; j++){
                arr[i][j] = dfp.newDfp(0);
            }
        }
        return new Array2DRowFieldMatrix<>(arr);
    }
    private Array2DRowFieldMatrix<Dfp> randomizeMatrix(int x){
        DfpField dfp = new DfpField(3);
        Random rand = new Random();
        Dfp[][] arr = new Dfp[nodeCount][x];
        for (int i = 0; i < nodeCount; i++){
            for (int j = 0; j < x; j++){
                int r = rand.nextInt(10) - 5;
                arr[i][j] = dfp.newDfp(r);
            }
        }
        return new Array2DRowFieldMatrix<>(arr);
    }
    public void SetLayerActivation(Array2DRowFieldMatrix<Dfp> activation){
        normalisedNodeMatrix = activation;
    }

    public Array2DRowFieldMatrix<Dfp> getBiasMatrix() {
        return biasMatrix;
    }

    public Array2DRowFieldMatrix<Dfp> getWeightMatrix() {
        return weightMatrix;
    }
    public void computeCurrentActivation(Array2DRowFieldMatrix<Dfp> prevLayerActivation){
        nodeMatrix = (weightMatrix.multiply(prevLayerActivation)).add(biasMatrix);
        normalisedNodeMatrix = CustomMath.sigmoid(nodeMatrix);
    }
    public void backpropagation(Array2DRowFieldMatrix<Dfp> expectedVector){
        dCostWRTActivation = normalisedNodeMatrix.subtract(expectedVector);
        computeDerivativeCostWRTWeight();
        computeDerivativeCostWRTBias();
    }
    public void backpropagation(Array2DRowFieldMatrix<Dfp> subsequentCostWRTActivation, Array2DRowFieldMatrix<Dfp> weightsLeavingLayer){
        computeDerivativeCostWRTActivation(subsequentCostWRTActivation, weightsLeavingLayer);
        computeDerivativeCostWRTWeight();
        computeDerivativeCostWRTBias();
    }
    private void computeDerivativeCostWRTActivation(Array2DRowFieldMatrix<Dfp> subsequentCostWRTActivation, Array2DRowFieldMatrix<Dfp> weightsLeavingLayer){
        dCostWRTActivation = CustomMath.hadamardProduct((Array2DRowFieldMatrix<Dfp>) (weightsLeavingLayer.transpose()).multiply(subsequentCostWRTActivation),
                CustomMath.sigmoidDerivative(nodeMatrix));
        // (Transpose WeightMatrix * Next derivative of cost WRT node activation) O Sigmoid Gradient of current node activation
    }
    private void computeDerivativeCostWRTWeight(){
        dCostWRTWeight = (Array2DRowFieldMatrix<Dfp>) dCostWRTActivation.multiply(normalisedNodeMatrix.transpose());
    }
    private void computeDerivativeCostWRTBias(){
        dCostWRTBias = (Array2DRowFieldMatrix<Dfp>) dCostWRTActivation.copy();
    }
    public int nodeMax(){
        int x = 0;
        for (int i = 0; i < nodeCount; i++){
            if (normalisedNodeMatrix.getEntry(i, 0).greaterThan(normalisedNodeMatrix.getEntry(x, 0)))
                x = i;
        }
        return x;
    }
    public Array2DRowFieldMatrix<Dfp> getNormalisedNodeMatrix(){
        return normalisedNodeMatrix;
    }
    public Array2DRowFieldMatrix<Dfp> getdCostWRTActivation(){
        return dCostWRTActivation;
    }
}
