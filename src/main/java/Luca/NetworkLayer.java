package Luca;

import org.apache.commons.math3.analysis.function.Sigmoid;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

import java.util.Random;

public class NetworkLayer {
    private final int nodeCount;
    private Array2DRowFieldMatrix<Dfp> nodeMatrix;
    private final Array2DRowFieldMatrix<Dfp> weightMatrix; // Weights Entering Layer
    private final Array2DRowFieldMatrix<Dfp> biasMatrix; // Bias on Layer

    NetworkLayer(int nodeCount, int prevNodeCount) {
        this.nodeCount = nodeCount;
        this.nodeMatrix = initMatrix(1);
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
        nodeMatrix = activation;
    }

    public Array2DRowFieldMatrix<Dfp> getBiasMatrix() {
        return biasMatrix;
    }

    public Array2DRowFieldMatrix<Dfp> getWeightMatrix() {
        return weightMatrix;
    }
    private Array2DRowFieldMatrix<Dfp> sigmoid(Array2DRowFieldMatrix<Dfp> mat){
        Dfp[][] arr = new Dfp[mat.getRowDimension()][mat.getColumnDimension()];
        Sigmoid sig = new Sigmoid();
        DfpField dfpField = new DfpField(3);
        for (int i = 0; i < mat.getRowDimension(); i++){
            for (int j = 0; j < mat.getColumnDimension(); j++){
                arr[i][j] = dfpField.newDfp(sig.value(mat.getEntry(i, j).toDouble()));
            }
        }
        return new Array2DRowFieldMatrix<>(arr);
    }
    public void computeCurrentActivation(Array2DRowFieldMatrix<Dfp> prevLayerActivation){
        nodeMatrix = sigmoid((weightMatrix.multiply(prevLayerActivation)).add(biasMatrix));
    }
    public int nodeMax(){
        int x = 0;
        for (int i = 0; i < nodeCount; i++){
            if (nodeMatrix.getEntry(i, 0).greaterThan(nodeMatrix.getEntry(x, 0)))
                x = i;
        }
        return x;
    }
    public Array2DRowFieldMatrix<Dfp> getNodeMatrix(){
        return nodeMatrix;
    }
}
