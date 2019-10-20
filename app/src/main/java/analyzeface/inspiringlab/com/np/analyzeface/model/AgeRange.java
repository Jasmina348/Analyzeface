package analyzeface.inspiringlab.com.np.analyzeface.model;

/**
 * Project Name: Analyzeface
 * Author: @Weenit Maharjan
 * Date Created: 20 Oct, 2019
 */
public class AgeRange {
    int Low;
    int High;

    public AgeRange(int low, int high) {
        Low = low;
        High = high;
    }

    public int getLow() {
        return Low;
    }

    public void setLow(int low) {
        Low = low;
    }

    public int getHigh() {
        return High;
    }

    public void setHigh(int high) {
        High = high;
    }
}
