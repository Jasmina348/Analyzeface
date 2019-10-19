package analyzeface.inspiringlab.com.np.analyzeface;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideHelper {

    public static void loadSingleImageFromUrl(Context context, String filename, ImageView imageView) {
        String url = MyApiHelper.VIEW_UPLOAD_IMAGE + filename;
        Glide.with(context).load(url).into(imageView);
    }


}
