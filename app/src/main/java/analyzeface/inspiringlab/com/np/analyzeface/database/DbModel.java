package analyzeface.inspiringlab.com.np.analyzeface.database;

public class DbModel {
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGENAME = "imageName";

    private int id;
    private String imageName;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_IMAGENAME + " TEXT,"
                    + ")";

    public DbModel() {
    }

    public DbModel(int id, String imageName) {
        this.id = id;
        this.imageName = imageName;

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


}
