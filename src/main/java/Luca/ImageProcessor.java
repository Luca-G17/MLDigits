package Luca;

import java.util.ArrayList;
import java.util.List;

public class ImageProcessor {

    private List<TrainingImage> trainingImages = new ArrayList<>();

    public List<TrainingImage> getTrainingImages() {
        return trainingImages;
    }
    public void addTrainingImage(TrainingImage t){
       trainingImages.add(t);
    }
}
