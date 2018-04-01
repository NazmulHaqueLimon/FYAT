package com.example.hp.project_1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class StudentInfo extends AppCompatActivity {

    ImageView studentsPic;
    TextView Sname,Sid,Sdept,Semail;

    private  static final int chose_image=101;
    Uri proImage;
    String imageUrl;
    FirebaseAuth mAuth;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);


        FirebaseDatabase database = FirebaseDatabase.getInstance();



        studentsPic=(ImageView)findViewById(R.id.imageID);
        Sname=(TextView) findViewById(R.id.nameID);
        Sid=(TextView) findViewById(R.id.deptID);
        Sdept=(TextView) findViewById(R.id.studentID);
        Semail=(TextView) findViewById(R.id.emailID);


        studentsPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChoser();

            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();

            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==chose_image && requestCode==RESULT_OK && data!=null && data.getData()!=null){
             proImage= data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),proImage);
                studentsPic.setImageBitmap(bitmap);

                uploadInFirebase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadInFirebase() {
        mStorageRef = FirebaseStorage.getInstance().getReference("frontPic"+System.currentTimeMillis()+".jpg");
        if(proImage !=null){
            mStorageRef.putFile(proImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   imageUrl=taskSnapshot.getDownloadUrl().toString();
                }
            })

        }
    }

    public void imageChoser(){
        Intent intent=new Intent().setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Your Image"),chose_image);
    }
    public void saveUserInfo(){

    }

}
