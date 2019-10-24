package analyzeface.inspiringlab.com.np.analyzeface.model;

    public class Emotion {
        String type;
        int confidence;

       public Emotion(){}

        public Emotion(String type, int confidence) {
            this.type = type;
            this.confidence = confidence;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setConfidence(int confidence) {
            this.confidence = confidence;
        }

        public String getType() {
            return type;
        }

        public int getConfidence() {
            return confidence;
        }
    }


