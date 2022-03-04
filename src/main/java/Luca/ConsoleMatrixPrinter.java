package Luca;

import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

import java.util.List;


/*
Do with generics later
public class ConsoleMatrixPrinter<T extends FieldElement<T>> {
    public void print(Array2DRowFieldMatrix<T> mat){
        for (int i = 0; i < mat.getRowDimension(); i++){
            for (int j = 0; j < mat.getColumnDimension(); j++){
                if (mat.getEntry(i, j).equals())
                    System.out.print();
            }
            System.out.println();
        }
    }
}
*/
public class ConsoleMatrixPrinter {
    public static void print(Array2DRowFieldMatrix<Dfp> mat){
        for (int i = 0; i < mat.getRowDimension(); i++){
            for (int j = 0; j < mat.getColumnDimension(); j++){
                if (mat.getEntry(i, j).isZero())
                    System.out.print("#");
                else
                    System.out.print(" ");
            }
            System.out.println();
        }
    }
    public static void printList(List<Array2DRowFieldMatrix<Dfp>> mats){
        for (int i = 0; i < mats.size(); i++){
            print(mats.get(i));
            System.out.println();
        }
    }
}
