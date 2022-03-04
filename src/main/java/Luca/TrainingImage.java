package Luca;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
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
    public Array2DRowFieldMatrix<Dfp> getMatrixAsVector(){
        Dfp arr[][] = new Dfp[matrix.getColumnDimension() * matrix.getRowDimension()][1];
        DfpField dfpField = new DfpField(0);
        for (int i = 0; i < matrix.getRowDimension(); i++){
            for (int j = 0; j < matrix.getColumnDimension(); j++){
                if (matrix.getEntry(i, j).isZero()) arr[matrix.getRowDimension() * i + j][0] = dfpField.newDfp(0);
                else arr[matrix.getRowDimension() * i + j][0] = dfpField.newDfp(1);
            }
        }
        return new Array2DRowFieldMatrix<Dfp>(arr);
    }
    public int getDigit() {
        return digit;
    }
    public int getIndex() {
        return index;
    }
}
