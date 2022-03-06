package Luca;

import org.apache.commons.math3.analysis.function.Sigmoid;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

public abstract class CustomMath {
    public static Array2DRowFieldMatrix<Dfp> hadarmardDivision(Array2DRowFieldMatrix<Dfp> m1, Array2DRowFieldMatrix<Dfp> m2){
        if (m1.getColumnDimension() != m2.getColumnDimension() || m1.getRowDimension() != m2.getRowDimension()){
            throw new RuntimeException(String.format("Dimensions don't match m1: (%d, %d) m2: (%d, %d)",
                    m1.getRowDimension(),
                    m1.getColumnDimension(),
                    m2.getRowDimension(),
                    m2.getColumnDimension()));
        }
        else{
            Dfp[][] arr = new Dfp[m1.getRowDimension()][m1.getColumnDimension()];
            for (int i = 0; i < m1.getRowDimension(); i++){
                for (int j = 0; j < m1.getColumnDimension(); j++){
                    arr[i][j] = m1.getEntry(i, j).divide(m2.getEntry(i, j));
                }
            }
            return new Array2DRowFieldMatrix<>(arr);
        }
    }
    public static Array2DRowFieldMatrix<Dfp> hadamardProduct(Array2DRowFieldMatrix<Dfp> m1, Array2DRowFieldMatrix<Dfp> m2){
        if (m1.getColumnDimension() != m2.getColumnDimension() || m1.getRowDimension() != m2.getRowDimension()){
            throw new RuntimeException(String.format("Dimensions don't match m1: (%d, %d) m2: (%d, %d)",
                    m1.getRowDimension(),
                    m1.getColumnDimension(),
                    m2.getRowDimension(),
                    m2.getColumnDimension()));
        }
        else{
            Dfp[][] arr = new Dfp[m1.getRowDimension()][m1.getColumnDimension()];
            for (int i = 0; i < m1.getRowDimension(); i++){
                for (int j = 0; j < m1.getColumnDimension(); j++){
                    arr[i][j] = m1.getEntry(i, j).multiply(m2.getEntry(i, j));
                }
            }
            return new Array2DRowFieldMatrix<>(arr);
        }
    }
    public static Array2DRowFieldMatrix<Dfp> sigmoid(Array2DRowFieldMatrix<Dfp> mat){
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
    public static Array2DRowFieldMatrix<Dfp> sigmoidDerivative(Array2DRowFieldMatrix<Dfp> mat){
        Dfp[][] arr = new Dfp[mat.getRowDimension()][mat.getColumnDimension()];
        Sigmoid.Parametric sig = new Sigmoid.Parametric();
        DfpField dfpField = new DfpField(3);
        for (int i = 0; i < mat.getRowDimension(); i++){
            for (int j = 0; j < mat.getColumnDimension(); j++){
                double[] sigGrad = sig.gradient(mat.getEntry(i, j).toDouble()); // Sigmoid Gradient = o(x)(1 - o(x))
                arr[i][j] = dfpField.newDfp(sigGrad[0] * sigGrad[1]);
            }
        }
        return new Array2DRowFieldMatrix<>(arr);
    }
}
