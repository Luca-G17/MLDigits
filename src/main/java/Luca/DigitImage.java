package Luca;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

public class DigitImage {
    private final Array2DRowFieldMatrix<Dfp> matrix;

    DigitImage(Dfp[][] arr){
        this.matrix = new Array2DRowFieldMatrix<>(arr);
    }
    public Array2DRowFieldMatrix<Dfp> getMatrix() {
        return matrix;
    }

    public Array2DRowFieldMatrix<Dfp> getMatrixAsVector(){
        Dfp[][] arr = new Dfp[matrix.getColumnDimension() * matrix.getRowDimension()][1];
        DfpField dfpField = new DfpField(Network.DECIMAL_DIGITS);
        for (int i = 0; i < matrix.getRowDimension(); i++){
            for (int j = 0; j < matrix.getColumnDimension(); j++){
                if (matrix.getEntry(i, j).isZero()) arr[matrix.getRowDimension() * i + j][0] = dfpField.newDfp(0);
                else arr[matrix.getRowDimension() * i + j][0] = dfpField.newDfp(1);
            }
        }
        return new Array2DRowFieldMatrix<>(arr);
    }
}
