package Luca;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;


public class BinaryFileReader {
    private static final int BUFFER_SIZE = 784;
    private static final int META_BUFFER_SIZE = 16;
    private static final int IMAGE_DIMENSION = 28;
    private URL filename;
    private List<Array2DRowFieldMatrix<Dfp>> matrices = new ArrayList<>();

    BinaryFileReader(URL filename){
        this.filename = filename;
        Read();
    }
    private void Read(){
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(String.valueOf(filename)))){
            byte[] metaBuffer = new byte[META_BUFFER_SIZE];
            int bytesRead = inputStream.read(metaBuffer);
            Dfp arr[][] = new Dfp[IMAGE_DIMENSION][IMAGE_DIMENSION];
            byte[] buffer = new byte[BUFFER_SIZE];
            while((bytesRead = inputStream.read(buffer)) != -1){
                int b = 0;
                for (int i = 0; i < IMAGE_DIMENSION; i++){
                    for (int j = 0; j < IMAGE_DIMENSION; j++){
                        DfpField dfpField = new DfpField(0);
                        arr[i][j] = dfpField.newDfp(buffer[b++]);
                    }
                }
                matrices.add(new Array2DRowFieldMatrix<Dfp>(arr));
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public List<Array2DRowFieldMatrix<Dfp>> getMatrices() {
        return matrices;
    }
}
