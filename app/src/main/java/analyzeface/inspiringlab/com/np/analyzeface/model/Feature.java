package analyzeface.inspiringlab.com.np.analyzeface.model;

/**
 * Project Name: Analyzeface
 * Author: @Weenit Maharjan
 * Date Created: 20 Oct, 2019
 */
public class Feature {
    String feature;
    String value;
    double confidence;

    public Feature(){}

    public Feature(String feature, String value, double confidence) {
        this.feature = feature;
        this.value = value;
        this.confidence = confidence;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
