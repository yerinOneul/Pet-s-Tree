package kr.ac.gachon.sw.petstree;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//유기동물 RecylerView adapter

public class Animal_adapter extends RecyclerView.Adapter<Animal_adapter.ViewHolder>{
    ArrayList<Animal> items = new ArrayList<>();
    private OnItemClickListener listener = null;
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
        //Glide를 사용하여 image 불러오기
        Glide.with(holder.itemView).load(item.getImage()).thumbnail(0.3f).into(holder.image);
        holder.setItem(item);

    }

    public void addItem(Animal item){
        items.add(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View v,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
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

            //해당 view 클릭 시 상세 정보 창으로 이동
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(view,pos);
                    }
                }
            });

        }

        public void setItem(Animal item){
            age.setText(item.getAge());
            sex.setText(item.getSex());
            weight.setText(item.getWeight());
            kind.setText(item.getKind());
            place.setText(item.getPlace());
            shelter.setText(item.getShelter());
        }

    }
}