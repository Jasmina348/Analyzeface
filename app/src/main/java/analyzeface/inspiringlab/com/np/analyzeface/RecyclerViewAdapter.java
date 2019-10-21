package analyzeface.inspiringlab.com.np.analyzeface;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import analyzeface.inspiringlab.com.np.analyzeface.model.Face;
import analyzeface.inspiringlab.com.np.analyzeface.model.MainResponse;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private LayoutInflater inflater;
//    private MainResponse mainResponse;
    private ArrayList<Face> faces;
    private Context context;

    public RecyclerViewAdapter(Context ctx, ArrayList<Face> mainResponse) {
        this.context = ctx;
        inflater = LayoutInflater.from(ctx);
        this.faces = mainResponse;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.card_recycler_view, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + faces.get(position).getImage());
        Glide.with(context).load(Config.IMAGE_URL + faces.get(position).getImage()).into(holder.iv);
//        if (mainResponse != null) {
//            if (mainResponse.getFaces().get(position) != null) {
//                Glide.with(context).load(mainResponse.getFaces().get(position).getImage()).override(1080,600).into(holder.iv);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return faces.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

//        TextView country, name, city;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);

//            country = (TextView) itemView.findViewById(R.id.country);
//            name = (TextView) itemView.findViewById(R.id.name);
//            city = (TextView) itemView.findViewById(R.id.city);
            iv = (ImageView) itemView.findViewById(R.id.result_image);
        }

    }
}
