package Luca;

import javafx.scene.image.Image;
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
    public static void printList(List<TrainingImage> images){
        for (TrainingImage image : images){
            System.out.println(String.format("Label: %d, ImageNum: %d", image.getDigit(), image.getIndex()));
            print(image.getMatrix());
            System.out.println();
        }
    }
}
