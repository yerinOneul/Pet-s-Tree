package kr.ac.gachon.sw.petstree;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.ac.gachon.sw.petstree.model.Write_Info;

public class Write extends AppCompatActivity {
    private Button save_btn;
    private Button cancel_btn;
    private static final String TAG = "Write";
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        save_btn = findViewById(R.id.save_btn);
        cancel_btn = findViewById(R.id.cancel_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

    }

    private void addPost(){
        final String title = ((EditText)findViewById(R.id.title)).getText().toString();
        final String contents = ((EditText)findViewById(R.id.contents)).getText().toString();

        if(title.length()>0 && contents.length()>0){
            user = FirebaseAuth.getInstance().getCurrentUser();
            Write_Info write_Info = new Write_Info(title, contents, user.getUid());
            uploader(write_Info);
        }else {
            startToast("내용을 입력해주세요.");
        }
    }

    private void uploader(Write_Info write_info){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(write_info)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference){
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e){
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
