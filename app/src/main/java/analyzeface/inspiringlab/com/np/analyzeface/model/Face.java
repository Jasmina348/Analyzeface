package analyzeface.inspiringlab.com.np.analyzeface.model;

import java.util.ArrayList;

/**
 * Project Name: Analyzeface
 * Author: @Weenit Maharjan
 * Date Created: 20 Oct, 2019
 */
public class Face {
    String image;
    ArrayList<Feature> featureList;
    AgeRange ageRange;
   ArrayList<Emotion> emotionList;

    public Face() {
        featureList = new ArrayList<>();
//        emotionList = new ArrayList<>();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Feature> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(ArrayList<Feature> featureList) {
        this.featureList = featureList;
    }

    public AgeRange getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(AgeRange ageRange) {
        this.ageRange = ageRange;
    }

    public ArrayList<Emotion> getEmotion() {
        return emotionList;
    }

    public void setEmotion(ArrayList<Emotion> emotionList) {
        this.emotionList = emotionList;
    }
}
