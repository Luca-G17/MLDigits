package Luca;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;


public class BinaryFileReader {
    private static final int BUFFER_SIZE = 784;
    private static final int IMAGE_META_BUFFER_SIZE = 16;
    private static final int IMAGE_DIMENSION = 28;
    private static final int LABEL_META_BUFFER_SIZE = 8;
    private final String imageFilename;
    private final String labelFilename;

    BinaryFileReader(String imageFilename, String labelFilename){
        this.imageFilename = FilenameResourceResolver.resolvePath(imageFilename);
        this.labelFilename = FilenameResourceResolver.resolvePath(labelFilename);
    }
    private byte[] readLabels(int s, int n){
        byte[] buffer = new byte[n];
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(labelFilename))){
            byte[] metaBuffer = new byte[LABEL_META_BUFFER_SIZE + s];
            inputStream.read(metaBuffer);
            inputStream.read(buffer);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return buffer;
    }
    public void ReadMats(int s, int n, ImageProcessor processor){
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(imageFilename))){
            // Skip metadata and images already read.
            byte[] metaBuffer = new byte[IMAGE_META_BUFFER_SIZE + s * IMAGE_DIMENSION * IMAGE_DIMENSION];
            int bytesRead = inputStream.read(metaBuffer);

            Dfp[][] arr = new Dfp[IMAGE_DIMENSION][IMAGE_DIMENSION];
            byte[] buffer = new byte[BUFFER_SIZE];
            List<Array2DRowFieldMatrix<Dfp>> matrices = new ArrayList<>();

            byte[] labels = readLabels(s, n);
            int matCount = -1;
            DfpField dfpField = new DfpField(0);
            while(inputStream.read(buffer) != -1 && matrices.size() < n){
                int b = 0;
                matCount++;
                for (int i = 0; i < IMAGE_DIMENSION; i++){
                    for (int j = 0; j < IMAGE_DIMENSION; j++){
                        arr[i][j] = dfpField.newDfp(buffer[b++]);
                    }
                }
                matrices.add(new Array2DRowFieldMatrix<>(arr));
                processor.addTrainingImage(new TrainingImage(arr, s + matCount, labels[matCount]));

            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
