package analyzeface.inspiringlab.com.np.analyzeface.model;

import java.util.ArrayList;

public class Emotion {
     String type;
     int confidence;

       public Emotion(){}

        public Emotion(String type,  int confidence) {
            this.type = type;
            this.confidence = confidence;
        }

    public int getConfidence() {
        return confidence;
    }

    public String getType() {
        return type;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public void setType(String type) {
        this.type = type;
    }
}


