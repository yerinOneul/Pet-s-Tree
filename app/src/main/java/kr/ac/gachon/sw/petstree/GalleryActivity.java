package kr.ac.gachon.sw.petstree;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        final int numberOfColumns = 3;

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        RecyclerView.Adapter mAdapter = new GalleryAdapter(this, getImagesPath(this));
        recyclerView.setAdapter(mAdapter);
        if(ContextCompat.checkSelfPermission(GalleryActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(GalleryActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                if(ActivityCompat.shouldShowRequestPermissionRationale(GalleryActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)){
                }
                else {
                    Toast.makeText(getApplication(), "권한을 허용해주세요.", Toast.LENGTH_SHORT);
                }
        }
    }


    // 저장소 권한 요청
    public void onRequestPermissionResult(int requestCode,
                                          @NonNull String permissions[],
                                          @NonNull int[] grantResults){
        switch (requestCode){
            case 1:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else{
                    finish();
                    Toast.makeText(getApplication(), "권한을 허용해주세요.", Toast.LENGTH_SHORT);
                }
            }
        }
    }


    public ArrayList<String> getImagesPath(Activity activity) {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data;
        String PathOfImage = null;

        String[] projection;
        Intent intent = getIntent();

        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        projection = new String[] { MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };


        cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(PathOfImage);
        }
        return listOfAllImages;
    }
}

