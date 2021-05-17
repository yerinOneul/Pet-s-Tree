package kr.ac.gachon.sw.petstree;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Animal_adapter extends RecyclerView.Adapter<Animal_adapter.ViewHolder>{
    ArrayList<Animal> items = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_animal, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Animal item = items.get(position);
        holder.setItem(item);

    }

    public void addItem(Animal item){
        items.add(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView age,sex,weight,kind,place,shelter;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.animal_photo);
            age = itemView.findViewById(R.id.animal_age);
            sex = itemView.findViewById(R.id.animal_sex);
            weight = itemView.findViewById(R.id.animal_weight);
            kind = itemView.findViewById(R.id.animal_kind);
            place = itemView.findViewById(R.id.animal_location);
            shelter = itemView.findViewById(R.id.animal_shelter);
        }

        public void setItem(Animal item){
            new setImage().execute(item.getImage());
            age.setText(item.getAge());
            sex.setText(item.getSex());
            weight.setText(item.getWeight());
            kind.setText(item.getKind());
            place.setText(item.getPlace());
            shelter.setText(item.getShelter());
        }

        private class setImage extends AsyncTask<String,Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bmp = null;
                try {
                    String img_url = strings[0];
                    URL url = new URL(img_url);
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bmp;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(Bitmap result) {
                image.setImageBitmap(result);
            }
        }


    }
}