package kr.ac.gachon.sw.petstree;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.time.temporal.JulianFields;
import java.util.ArrayList;
import java.util.Date;

import kr.ac.gachon.sw.petstree.model.Write_Info;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;



//@Config(manifest = Config.NONE)
//@RunWith(RobolectricTestRunner.class)
//@PrepareForTest({ FirebaseFirestore.class })
@RunWith(JUnit4.class)
public class PostReadWrite {
    private Write_Info writeInfo;
    private Context context;

    String title;
    ArrayList<String> contents = new ArrayList<>();
    String publisher;
    Date date;
    int boardType;
    int num_comments;

    @Before
    public void Upload() {
        title = "test title";
        contents.add("test contents 1");
        publisher = "nickname";
        date = new Date();
        boardType = 1;
        num_comments = 2;

        writeInfo = new Write_Info(title, contents, publisher, date, boardType, num_comments);
        // context = ApplicationProvider.getApplicationContext();
    }


    @Test
    public void DownloadAndCompare() {
        System.out.println("title: " + writeInfo.getTitle());
        System.out.println("contents: " + writeInfo.getContents());
        System.out.println("publisher: " + writeInfo.getPublisher());
        System.out.println("date: " + writeInfo.getCreateAt());
        System.out.println("boardType: " + writeInfo.getBoardType());
        System.out.println("nComments: " + writeInfo.getNum_comments());

        assertEquals(title, writeInfo.getTitle());
        assertEquals(contents, writeInfo.getContents());
        assertEquals(publisher, writeInfo.getPublisher());
        assertEquals(date, writeInfo.getCreateAt());
        assertEquals(boardType, writeInfo.getBoardType());
        assertEquals(num_comments, writeInfo.getNum_comments());

        // 파이어베이스 유닛테스트 해보고 싶었는데 자료도 없고 작동도 안되네요....
//        FirebaseOptions.Builder builder = new FirebaseOptions.Builder()
//                .setApplicationId("1:48244226515:android:688ef0f08506e48c18d726")
//                .setApiKey("AIzaSyB2cnCCya8vOlmMiV2gfroLEzZ115_4Mjs")
//                .setDatabaseUrl("https://petstree-c5e85.firebaseio.com")
//                .setStorageBucket("petstree-c5e85.appspot.com");
//
//        FirebaseApp.initializeApp(context, builder.build());
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("posts").add(writeInfo)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference){
//                        System.out.println("Upload Success");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e){
//                        System.out.println("Upload Failed");
//
//                    }
//                });
//
//        db.collection("posts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                System.out.println(document.getId() + "=>" + document.getData());
//                            }
//                        } else {
//                            System.out.println("Read Failed");
//                        }
//                    }
//                });
    }

    @After
    public void DeleteTestPost() {

    }
}