package analyzeface.inspiringlab.com.np.analyzeface.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class MainResponse{
    private String image;
    private ArrayList<Face> faces;

    public MainResponse() {

    }
////
    public MainResponse(String image, ArrayList<Face> faces) {
        this.image = image;
        this.faces = faces;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Face> getFaces() {
        if (!(faces == null))
            return faces;
        else
            return new ArrayList<Face>();
    }

    public void setFaces(ArrayList<Face> faces) {
        this.faces = faces;
    }


}
