package Luca;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

import java.util.Random;

public class NetworkLayer {
    private final int nodeCount;
    private Array2DRowFieldMatrix<Dfp> normalisedNodeMatrix;
    private Array2DRowFieldMatrix<Dfp> nodeMatrix;
    private Array2DRowFieldMatrix<Dfp> weightMatrix; // Weights Entering Layer
    private Array2DRowFieldMatrix<Dfp> biasMatrix; // Bias on Layer

    private Array2DRowFieldMatrix<Dfp> dCostWRTActivation;
    private Array2DRowFieldMatrix<Dfp> dCostWRTWeight;
    private Array2DRowFieldMatrix<Dfp> dCostWRTBias;
    private final double LEARNING_RATE = 0.001;

    NetworkLayer(int nodeCount, Array2DRowFieldMatrix<Dfp> weightMatrix, Array2DRowFieldMatrix<Dfp> biasMatrix){
        this.nodeCount = nodeCount;
        this.normalisedNodeMatrix = initMatrix(1);
        this.weightMatrix = weightMatrix;
        this.biasMatrix = biasMatrix;
    }

    NetworkLayer(int nodeCount, int prevNodeCount) {
        this.nodeCount = nodeCount;
        this.normalisedNodeMatrix = initMatrix(1);
        weightMatrix = randomizeMatrix(prevNodeCount);
        biasMatrix = randomizeMatrix(1);
    }
    public void initGradMats(){
        dCostWRTWeight = initMatrix(weightMatrix.getColumnDimension());
        dCostWRTBias = initMatrix(1);
    }

    private Array2DRowFieldMatrix<Dfp> initMatrix(int x){
        DfpField dfp = new DfpField(Network.DECIMAL_DIGITS);
        Dfp[][] arr = new Dfp[nodeCount][x];
        for (int i = 0; i < nodeCount; i++){
            for (int j = 0; j < x; j++){
                arr[i][j] = dfp.newDfp(0);
            }
        }
        return new Array2DRowFieldMatrix<>(arr);
    }
    private Array2DRowFieldMatrix<Dfp> randomizeMatrix(int x){
        DfpField dfp = new DfpField(Network.DECIMAL_DIGITS);
        Random rand = new Random();
        Dfp[][] arr = new Dfp[nodeCount][x];
        for (int i = 0; i < nodeCount; i++){
            for (int j = 0; j < x; j++){
                double r = rand.nextDouble();
                arr[i][j] = dfp.newDfp(r);
            }
        }
        return new Array2DRowFieldMatrix<>(arr);
    }

    public Array2DRowFieldMatrix<Dfp> getBiasMatrix() {
        return biasMatrix;
    }

    public Array2DRowFieldMatrix<Dfp> getWeightMatrix() {
        return weightMatrix;
    }
    public void computeCurrentActivation(Array2DRowFieldMatrix<Dfp> prevLayerActivation){
        nodeMatrix = (weightMatrix.multiply(prevLayerActivation)).add(biasMatrix);
        normalisedNodeMatrix = CustomMath.reLU(nodeMatrix);
    }
    public void backpropagation(Array2DRowFieldMatrix<Dfp> prevLayerActivation, Array2DRowFieldMatrix<Dfp> expectedVector){
        dCostWRTActivation = (Array2DRowFieldMatrix<Dfp>) nodeMatrix.subtract(expectedVector.scalarMultiply(CustomMath.vectorMax(nodeMatrix)));
        dCostWRTWeight = dCostWRTWeight.add(computeDerivativeCostWRTWeight(prevLayerActivation)); // Average gradient's over batch size
        dCostWRTBias = dCostWRTBias.add(computeDerivativeCostWRTBias());
    }
    public void backpropagation(Array2DRowFieldMatrix<Dfp> prevLayerActivation, Array2DRowFieldMatrix<Dfp> subsequentCostWRTActivation, Array2DRowFieldMatrix<Dfp> weightsLeavingLayer){
        dCostWRTActivation = computeDerivativeCostWRTActivation(subsequentCostWRTActivation, weightsLeavingLayer);
        dCostWRTWeight = dCostWRTWeight.add(computeDerivativeCostWRTWeight(prevLayerActivation));
        dCostWRTBias = dCostWRTBias.add(computeDerivativeCostWRTBias());
    }
    public void stepGradientAndBias(int batchSize){
        DfpField dfpField = new DfpField(Network.DECIMAL_DIGITS);
        double normaliser = 1f / batchSize;
        Dfp learningDfp = dfpField.newDfp(LEARNING_RATE);
        weightMatrix = (Array2DRowFieldMatrix<Dfp>) weightMatrix.subtract(CustomMath.hadamardDivision(
                weightMatrix,
                (Array2DRowFieldMatrix<Dfp>) dCostWRTWeight.scalarMultiply(dfpField.newDfp(normaliser))
        ).scalarMultiply(learningDfp));
        biasMatrix = (Array2DRowFieldMatrix<Dfp>) biasMatrix.subtract(CustomMath.hadamardDivision(
                biasMatrix,
                (Array2DRowFieldMatrix<Dfp>) dCostWRTBias.scalarMultiply(dfpField.newDfp(normaliser))
        ).scalarMultiply(learningDfp));
    }
    private Array2DRowFieldMatrix<Dfp> computeDerivativeCostWRTActivation(Array2DRowFieldMatrix<Dfp> subsequentCostWRTActivation, Array2DRowFieldMatrix<Dfp> weightsLeavingLayer){
        return CustomMath.hadamardProduct((Array2DRowFieldMatrix<Dfp>) (weightsLeavingLayer.transpose()).multiply(subsequentCostWRTActivation),
                CustomMath.reLUDerivative(nodeMatrix));
        // (Transpose WeightMatrix * Next derivative of cost WRT node activation) O Sigmoid Gradient of current node activation
    }
    private Array2DRowFieldMatrix<Dfp> computeDerivativeCostWRTWeight(Array2DRowFieldMatrix<Dfp> prevLayerActivation){
        return (Array2DRowFieldMatrix<Dfp>) dCostWRTActivation.multiply(prevLayerActivation.transpose());
    }
    private Array2DRowFieldMatrix<Dfp> computeDerivativeCostWRTBias(){
        return (Array2DRowFieldMatrix<Dfp>) dCostWRTActivation.copy();
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
    public int getNodeCount() {
        return nodeCount;
    }
}
