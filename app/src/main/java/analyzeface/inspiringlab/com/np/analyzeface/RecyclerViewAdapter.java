package analyzeface.inspiringlab.com.np.analyzeface;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import analyzeface.inspiringlab.com.np.analyzeface.model.AgeRange;
import analyzeface.inspiringlab.com.np.analyzeface.model.Emotion;
import analyzeface.inspiringlab.com.np.analyzeface.model.Face;
import analyzeface.inspiringlab.com.np.analyzeface.model.Feature;
import analyzeface.inspiringlab.com.np.analyzeface.model.MainResponse;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private LayoutInflater inflater;
    private MainResponse mainResponse;
    private ArrayList<Face> faces;
    private Context context;
    private int imageState = 1;



    public RecyclerViewAdapter(Context ctx, ArrayList<Face> mainResponse) {
        this.context = ctx;
        inflater = LayoutInflater.from(ctx);
        this.faces = mainResponse;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.faces_image_view, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.MyViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: " + faces.get(position).getImage());
        Glide.with(context).load(Config.IMAGE_URL + faces.get(position).getImage()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        })
                .into(holder.iv_faces_image);
        ((ResultActivity)context).setDefaultImageInfo(faces.get(0));
        ((ResultActivity)context).setDefaultImageEmotion(faces.get(0));

        holder.iv_faces_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(imageState % 2 == 0){
                    ((GradientDrawable)holder.iv_faces_image.getBackground()).setStroke(10,Color.BLACK);

//                }
//                else{
//                    ((GradientDrawable)holder.iv_faces_image.getBackground()).setStroke(10,Color.BLACK);
//
//
//                }
//                imageState++;
//            }




//               Toast.makeText(context, position+"", Toast.LENGTH_SHORT).show();

                    ((ResultActivity)context).setUpFacesInfromation(faces.get(position));
                ((ResultActivity)context).setUpEmotionInformation(faces.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return faces.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TableLayout tv1;
        TableLayout tv2;
        TextView high_value;
        TextView low_value;
        ImageView iv_faces_image;
        ProgressBar progressBar;


        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.result_image);
            tv1 = (TableLayout) itemView.findViewById(R.id.featurelist);
            tv2 = (TableLayout) itemView.findViewById(R.id.emotionList);
            high_value = (TextView) itemView.findViewById(R.id.high_value);
            low_value = (TextView) itemView.findViewById(R.id.low_value);
            iv_faces_image= (ImageView) itemView.findViewById(R.id.iv_faces_image);
            progressBar =(ProgressBar) itemView.findViewById(R.id.progress_bar);

        }

    }

    private void setUpFeatureList(RecyclerViewAdapter.MyViewHolder holder, int position) {
        int length = faces.get(position).getFeatureList().size();
        Log.d("FACELENGTH", "" + length);

        for (int i = 0; i < length; i++) {
            Feature feature = faces.get(position).getFeatureList().get(i);

            TableRow parent = new TableRow(context);

            parent.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.HORIZONTAL);

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    /*width*/ ViewGroup.LayoutParams.MATCH_PARENT,
                    /*height*/ ViewGroup.LayoutParams.WRAP_CONTENT,
                    /*weight*/ 1.0f
            );
            TextView featureName = new TextView(context);
            TextView featureValue = new TextView(context);
            featureName.setLayoutParams(params);
            featureValue.setLayoutParams(params);

            featureName.setText(feature.getName());
            featureValue.setText(feature.getValue());
            Log.d("FEATURENAME", "" + feature.getFeature());

            parent.addView(featureName);
            parent.addView(featureValue);

            holder.tv1.addView(parent);
        }

    }

    private void setUpEmotionList(RecyclerViewAdapter.MyViewHolder holder, int position) {
        int length = faces.get(position).getEmotion().size();
        Log.d("FACELENGTH", "" + length);
        for (int i = 0; i < length; i++) {
            Emotion emotions = faces.get(position).getEmotion().get(i);


            TableRow parent = new TableRow(context);

            parent.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    /*width*/ ViewGroup.LayoutParams.MATCH_PARENT,
                    /*height*/ ViewGroup.LayoutParams.WRAP_CONTENT,
                    /*weight*/ 1.0f
            );
            TextView emotionName = new TextView(context);
            TextView emotionValue = new TextView(context);
            emotionName.setLayoutParams(params);
            emotionValue.setLayoutParams(params);

            emotionName.setText(emotions.getType());
            emotionValue.setText("" + emotions.getConfidence()+"%");
            Log.d("EMOTIONNAME", "" + emotions.getType());
            Log.d("EMOTION LENGTH", emotions.getType());

            parent.addView(emotionName);
            parent.addView(emotionValue);

            holder.tv2.addView(parent);
        }
    }


    }

