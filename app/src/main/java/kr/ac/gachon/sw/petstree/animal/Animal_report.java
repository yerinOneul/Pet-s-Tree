package kr.ac.gachon.sw.petstree.animal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kr.ac.gachon.sw.petstree.R;
import kr.ac.gachon.sw.petstree.model.Write_Info;
import kr.ac.gachon.sw.petstree.util.Gps;

public class Animal_report extends AppCompatActivity {
    private ActionBar actionBar;
    private Button save_btn, cancel_btn;
    private ImageButton myLoc_btn, image_btn;
    private RelativeLayout loaderLayout;
    private EditText reportLoc;
    private static final String TAG = "Report";
    private FirebaseUser user;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;
    private int pathCount, successCount;
    private Gps gps;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private int requestCode;
    private int resultCode;
    private Intent data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_report);
        myLoc_btn = findViewById(R.id.report_my_location);
        save_btn = findViewById(R.id.report_save_btn);
        cancel_btn = findViewById(R.id.report_cancel_btn);
        myLoc_btn = findViewById(R.id.report_my_location);
        image_btn = findViewById(R.id.report_image_btn);
        parent = findViewById(R.id.report_contentsLayout);
        loaderLayout = findViewById(R.id.loaderLayout);
        reportLoc = findViewById(R.id.report_location);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("???????????? ??????");
        }

        myLoc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //permission check
                if (!checkLocStatus()) {
                    showDialogLoc();
                } else {
                    checkRunPermission();
                    //???????????? ?????? ?????? ?????? ????????????
                    gps = new Gps(Animal_report.this);
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    String addr = getCurrentAddress(latitude, longitude);
                    reportLoc.setText(addr);
                }

            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageUpload();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToast("???????????? ?????? ????????? ??????????????????.");
                finish();
            }
        });

        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Animal_report.this)
                        .crop()
                        .galleryMimeTypes(new String[]{"image/png", "image/jpg", "image/jpeg"})
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Google ????????? ?????? MenuItem??? Switch ???????????? ?????? if??? ??????
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ?????? ?????? ??? ???????????? view??? ????????????.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ImagePicker.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    pathList.add(ImagePicker.Companion.getFilePath(data));

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    ImageView imageView = new ImageView(Animal_report.this);
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(this).load(ImagePicker.Companion.getFilePath(data)).override(1000).into(imageView);
                    parent.addView(imageView);

                    EditText editText = new EditText(Animal_report.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    parent.addView(editText);

                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Log.w(this.getClass().getSimpleName(), "ImagePicker Error : " + ImagePicker.Companion.getError(data));
                    Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
                }
                break;

            case GPS_ENABLE_REQUEST_CODE:
                if (checkLocStatus()) {
                    checkRunPermission();
                    break;
                }
                break;
        }
    }

    private void storageUpload() {
        final String title = ((EditText) findViewById(R.id.report_title)).getText().toString();
        final String sex = ((EditText) findViewById(R.id.report_sex)).getText().toString();
        final String age = ((EditText) findViewById(R.id.report_age)).getText().toString();
        final String comment = ((EditText) findViewById(R.id.report_comment)).getText().toString();
        final String time = ((EditText) findViewById(R.id.report_time)).getText().toString();
        final String location = ((EditText) findViewById(R.id.report_location)).getText().toString();
        final String name = ((EditText) findViewById(R.id.report_name)).getText().toString();
        final String phone = ((EditText) findViewById(R.id.report_phone)).getText().toString();

        // ????????? ?????? ????????? ????????? ???????????? ??????  ---> ???????????? ?????? ??? ?????? ??????
        if (pathList.size() == 0) {
            startToast("?????? ????????? ????????? ????????? ????????????.");
        } else if (title.length() > 0 && time.length() > 0 && location.length() > 0 && name.length() > 0 && phone.length() > 0) {
            loaderLayout.setVisibility(View.VISIBLE);
            ArrayList<String> contentsList = new ArrayList<>();
            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                if (view instanceof EditText) {
                    String text = ((EditText) view).getText().toString();
                    if (view.getId()==R.id.report_sex){
                        text = "?????? : "+text;
                    }
                    else if (view.getId() == R.id.report_age){
                        text = "?????? : "+text;
                    }
                    else if (view.getId() == R.id.report_comment){
                        text = "?????? : "+text;
                    }
                    else if (view.getId() == R.id.report_time){
                        text = "?????? ?????? : "+text;
                    }
                    else if (view.getId() == R.id.report_location){
                        text = "?????? ?????? : "+text;
                    }
                    else if (view.getId() == R.id.report_name){
                        text = "???????????? : "+text;
                    }
                    else if (view.getId() == R.id.report_phone){
                        text = "????????? ????????? : "+text;
                    }
                    if (text.length() > 0) {
                        contentsList.add(text);
                    }
                } else if (view instanceof ImageView) {
                    contentsList.add(pathList.get(pathCount));
                    final StorageReference imageRef = storageRef.child("posts/" + user.getUid() + "/" + pathCount + ".jpg");
                    try {
                        InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));

                        StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size() - 1)).build();
                        UploadTask uploadTask = imageRef.putStream(stream, metadata);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        contentsList.set(index, uri.toString());
                                        successCount++;
                                        if (pathList.size() == successCount) {
                                            // ????????? ??????
                                            // BoardType??? Write_Info ?????? (0 ~ 6)
                                            Write_Info write_Info = new Write_Info(title, contentsList, user.getUid(), new Date(), 5, 0);
                                            storeUpload(write_Info);

                                        }
                                    }
                                });

                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    pathCount++;
                }
            }
        } else {
            startToast("?????? ?????? ????????? ??????????????????.");
        }
    }

    private void storeUpload(Write_Info write_info) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(write_info)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        loaderLayout.setVisibility(View.GONE);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        loaderLayout.setVisibility(View.GONE);
                    }
                });

    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //return result of request permission

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == PERMISSIONS.length) {
            boolean check_result = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            //permission denied
            if (!check_result) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[1])) {
                    Toast.makeText(Animal_report.this, "?????? ?????? ?????? ????????? ??????????????????.", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public void checkRunPermission() {
        //check permission
        int fineLoc_permission = ContextCompat.checkSelfPermission(Animal_report.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLoc_permission = ContextCompat.checkSelfPermission(Animal_report.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        //permission ?????? ???
        if (fineLoc_permission != PackageManager.PERMISSION_GRANTED || coarseLoc_permission != PackageManager.PERMISSION_GRANTED)
            //?????? ??? ?????? ????????? ?????? ?????? toast??? ?????????.
            if (ActivityCompat.shouldShowRequestPermissionRationale(Animal_report.this, PERMISSIONS[0])) {
                Toast.makeText(Animal_report.this, "?????? ?????? ?????? ????????? ??????????????????.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(Animal_report.this, PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
            // ?????? ??? ?????? ????????? ?????? permission ??????
            else {
                ActivityCompat.requestPermissions(Animal_report.this, PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
    }

    public boolean checkLocStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public String getCurrentAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(Animal_report.this, Locale.getDefault());
        List<Address> addr_list = null;
        try {
            addr_list = geocoder.getFromLocation(latitude, longitude, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //?????? list??? ?????? ?????????
        if (addr_list == null || addr_list.size() == 0) {
            return "?????? ????????? ???????????? ???????????????.";
        }
        Address addr = addr_list.get(0);
        return addr.getAddressLine(0).toString().replace("????????????","");
    }

    //?????? ?????? ?????? ??????????????? ??????
    private void showDialogLoc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Animal_report.this);
        builder.setTitle("?????? ?????? ?????? ????????????");
        builder.setMessage("?????? ?????? ?????? ????????? ???????????????.");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.create().show();
    }

}
