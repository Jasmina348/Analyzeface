package analyzeface.inspiringlab.com.np.analyzeface.model;

import java.util.ArrayList;

public class MainResponse {
    private String image;
    private ArrayList<Face> faces;

    public MainResponse(){

    }

    public MainResponse(String image, ArrayList<Face> details) {
        this.image = image;
        this.faces = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

    public void setFaces(ArrayList<Face> details) {
        this.faces = details;
    }
}
