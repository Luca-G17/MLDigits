package Luca;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

public class TrainingImage {

    private Array2DRowFieldMatrix<Dfp> matrix;
    private final int index;
    private final int digit;
    TrainingImage(Dfp arr[][], int index, int digit){
        this.matrix = new Array2DRowFieldMatrix<Dfp>(arr);
        this.index = index;
        this.digit = digit;
    }
    public Array2DRowFieldMatrix<Dfp> getMatrix() {
        return matrix;
    }
    public int getDigit() {
        return digit;
    }
    public int getIndex() {
        return index;
    }
}
