package kr.ac.gachon.sw.petstree;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
public class GalleryAdapter extends RecyclerView.Adapter <GalleryAdapter.GalleryViewHolder> {
    private ArrayList<String> mDataset;
    private Activity activity;

    static class GalleryViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        GalleryViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }
    public GalleryAdapter(Activity activity, ArrayList<String> myDataset){
        mDataset = myDataset;
        this.activity = activity;
    }

    @NonNull
    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        final GalleryViewHolder galleryViewHolder = new GalleryViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("imagePath", mDataset.get(galleryViewHolder.getAdapterPosition()));
                Log.d("Tag", mDataset.get(galleryViewHolder.getAdapterPosition()));
                activity.setResult(Activity.RESULT_OK, resultIntent);
                activity.finish();
            }
        });
        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryViewHolder holder, int position) {
        CardView v = holder.cardView;
        ImageView imageView = holder.cardView.findViewById(R.id.imageView);
        // override = 이미지 사이즈 조절
        Glide.with(activity).load(mDataset.get(position)).centerCrop().override(500).into(imageView);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
