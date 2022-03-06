package Luca;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

import java.io.Console;
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
public class ConsolePrinter {
    public static void printImage(TrainingImage image){
        Array2DRowFieldMatrix<Dfp> mat = image.getMatrix();
        System.out.printf("Label: %d, ImageNum: %d%n", image.getDigit(), image.getIndex());
        for (int i = 0; i < mat.getRowDimension(); i++){
            for (int j = 0; j < mat.getColumnDimension(); j++){
                if (mat.getEntry(i, j).isZero())
                    System.out.print("#");
                else
                    System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public static void printMatrixValues(Array2DRowFieldMatrix<Dfp> mat){
        for (int i = 0; i < mat.getRowDimension(); i++){
            for (int j = 0; j < mat.getColumnDimension(); j++){
                System.out.printf("%f ", (mat.getEntry(i, j).toDouble()));
            }
            System.out.println();
        }
        System.out.println();
    }
    public static void printList(List<TrainingImage> images){
        for (TrainingImage image : images){
            printImage(image);
        }
    }
    public static void printCost(NetworkLayer layer, int i){
        Array2DRowFieldMatrix<Dfp> costs = layer.getdCostWRTActivation();
        double total = 0;
        for (Dfp entry : costs.getColumn(0)){
            total += entry.toDouble();
        }
        System.out.printf("Image Num: %d\nCost: %f%n", i, total);
    }
}
