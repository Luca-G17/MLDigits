package Luca;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;

public class TrainingImage extends DigitImage {

    private final int index;
    private final int digit;
    TrainingImage(Dfp[][] arr, int index, int digit){
        super(arr);
        this.index = index;
        this.digit = digit;
    }
    public int getDigit() {
        return digit;
    }
    public int getIndex() {
        return index;
    }
}
