package Luca;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;


public class BinaryFileReaderWriter {
    private static final int BUFFER_SIZE = 784;
    private static final int IMAGE_META_BUFFER_SIZE = 16;
    private static final int IMAGE_DIMENSION = 28;
    private static final int LABEL_META_BUFFER_SIZE = 8;
    private final String imageFilename;
    private final String labelFilename;
    private final String networkFilename;

    BinaryFileReaderWriter(String imageFilename, String labelFilename, String networkFilename){
        this.imageFilename = FilenameResourceResolver.resolvePath(imageFilename);
        this.labelFilename = FilenameResourceResolver.resolvePath(labelFilename);
        this.networkFilename = "target/classes/Luca/" + networkFilename;
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
    private void writeLayer(NetworkLayer layer, DataOutputStream outFile) throws IOException {
        Array2DRowFieldMatrix<Dfp> weights = layer.getWeightMatrix();
        Array2DRowFieldMatrix<Dfp> bias = layer.getBiasMatrix();
        outFile.writeInt(weights.getRowDimension());
        outFile.writeInt(weights.getColumnDimension());
        outFile.writeInt(layer.getNodeCount());
        for (int i = 0; i < weights.getRowDimension(); i++){
            for (int j = 0; j < weights.getColumnDimension(); j++){
                outFile.writeDouble(weights.getEntry(i, j).toDouble());
            }
        }
        for (int i = 0; i < bias.getRowDimension(); i++){
            outFile.writeDouble(bias.getEntry(i, 0).toDouble());
        }
    }
    public void writeNetwork(Network network){
        List<NetworkLayer> layers = network.getNetwork();
        DataOutputStream outFile;
        try {
            outFile = new DataOutputStream(new FileOutputStream(networkFilename));
            outFile.writeInt(layers.size());
            for (NetworkLayer layer : layers)
                writeLayer(layer, outFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private NetworkLayer readLayer(DataInputStream inFile) throws IOException {
        int rowCount = inFile.readInt();
        int colCount = inFile.readInt();
        int biasCount = inFile.readInt();
        DfpField dfpField = new DfpField(Network.DECIMAL_DIGITS);
        Dfp[][] wArr = new Dfp[rowCount][colCount];
        for (int i = 0; i < rowCount; i++){
            for (int j = 0; j < colCount; j++){
                wArr[i][j] = dfpField.newDfp(inFile.readDouble());
            }
        }
        Array2DRowFieldMatrix<Dfp> weights = new Array2DRowFieldMatrix<>(wArr);
        Dfp[][] bArr = new Dfp[biasCount][1];
        for (int i = 0; i < biasCount; i++){
            bArr[i][0] = dfpField.newDfp(inFile.readDouble());
        }
        Array2DRowFieldMatrix<Dfp> bias = new Array2DRowFieldMatrix<>(bArr);
        return new NetworkLayer(biasCount, weights, bias);
    }
    public Network readNetwork(){
        DataInputStream inFile;
        try {
            inFile = new DataInputStream(new FileInputStream(networkFilename));
            int layerCount = inFile.readInt();
            Network newNetwork = new Network(layerCount, networkFilename);
            for (int i = 0; i < layerCount; i++){
                newNetwork.addLayer(readLayer(inFile));
            }
            return newNetwork;
        }
        catch (IOException e){
            return null;
        }
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
