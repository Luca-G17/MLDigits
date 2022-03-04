package Luca;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

import java.util.Random;

public class NetworkLayer {
    private Array2DRowFieldMatrix<Dfp> weightMatrix; // Weights Leaving Layer
    private Array2DRowFieldMatrix<Dfp> biasMatrix; // Bias on Layer
    private Array2DRowFieldMatrix<Dfp> nodeMatrix; // Nodes on Layer

    private final int NODES_COUNTS = 16;
    NetworkLayer(){
        weightMatrix = randomizeMatrix(16);
        biasMatrix = randomizeMatrix(1);
        nodeMatrix = initMatrix(1);
    }
    private Array2DRowFieldMatrix<Dfp> initMatrix(int x){
        DfpField dfp = new DfpField(3);
        Dfp arr[][] = new Dfp[NODES_COUNTS][x];
        for (int i = 0; i < NODES_COUNTS; i++){
            for (int j = 0; j < x; j++){
                arr[i][j] = dfp.newDfp(0);
            }
        }
        return new Array2DRowFieldMatrix<>(arr);
    }
    private Array2DRowFieldMatrix<Dfp> randomizeMatrix(int x){
        DfpField dfp = new DfpField(3);
        Random rand = new Random();
        Dfp arr[][] = new Dfp[NODES_COUNTS][x];
        for (int i = 0; i < NODES_COUNTS; i++){
            for (int j = 0; j < x; j++){
                float r = rand.nextFloat();
                arr[i][j] = dfp.newDfp(r);
            }
        }
        return new Array2DRowFieldMatrix<>(arr);
    }
}
