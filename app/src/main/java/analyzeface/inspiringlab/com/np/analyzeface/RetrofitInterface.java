package analyzeface.inspiringlab.com.np.analyzeface;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitInterface {
    @Multipart
    @POST("/detect-faces")
 Call<ResponseModal> uploadImage(@Part MultipartBody.Part image);
}
