package kr.ac.gachon.sw.petstree.certreq;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import kr.ac.gachon.sw.petstree.R;
import kr.ac.gachon.sw.petstree.model.CertRequest;
import kr.ac.gachon.sw.petstree.model.User;
import kr.ac.gachon.sw.petstree.util.Firestore;
import kr.ac.gachon.sw.petstree.util.Storage;
import kr.ac.gachon.sw.petstree.util.Util;

public class CertRequestAdapter extends RecyclerView.Adapter<CertRequestAdapter.CertRequestHolder> {
    private ArrayList<CertRequest> certRequestList = null;
    private OnCertRequestItemClickListener listener = null;

    public interface OnCertRequestItemClickListener {
        void onClick(View v, int pos);
    }

    public CertRequestAdapter(ArrayList<CertRequest> certRequestList, OnCertRequestItemClickListener listener) {
        this.certRequestList = certRequestList;
        this.listener = listener;
    }

    public class CertRequestHolder extends RecyclerView.ViewHolder {
        TextView tvUserNickname;
        TextView tvRequestTime;
        ImageView ivCertPhoto;

        public CertRequestHolder(@NonNull final View itemView) {
            super(itemView);
            tvUserNickname = itemView.findViewById(R.id.tv_usernickname);
            tvRequestTime = itemView.findViewById(R.id.tv_requesttime);
            ivCertPhoto = itemView.findViewById(R.id.iv_certphoto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public CertRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_certrequest, parent,false);
        return new CertRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertRequestAdapter.CertRequestHolder holder, int position) {
        CertRequest certRequest = certRequestList.get(position);
        holder.tvRequestTime.setText(Util.timeStamptoString(certRequest.getTime()));

        Firestore.getUserData(certRequest.getUserId())
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            User user = task.getResult().toObject(User.class);
                            holder.tvUserNickname.setText(user.getUserNickName());
                        }
                    }
                });

        Storage.getImageFromURL(certRequest.getImgUrl())
                .addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        if(task.isSuccessful()) {
                            holder.ivCertPhoto.setImageBitmap(Util.byteArrayToBitmap(task.getResult()));
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return certRequestList.size();
    }

    public void addItem(CertRequest certRequest) {
        certRequestList.add(certRequest);
    }

    public void addItems(ArrayList<CertRequest> certRequestList) {
        this.certRequestList = certRequestList;
        notifyDataSetChanged();
    }

    public void removeItem(CertRequest targetReq) {
        for(CertRequest certRequest : certRequestList) {
            if(certRequest.getDocId().equals(targetReq.getDocId())) {
                int idx = certRequestList.indexOf(certRequest);
                certRequestList.remove(idx);
                notifyItemRemoved(idx);
                break;
            }
        }
    }
}
