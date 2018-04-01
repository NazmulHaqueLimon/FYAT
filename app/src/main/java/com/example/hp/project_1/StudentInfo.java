package com.example.hp.project_1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class StudentInfo extends AppCompatActivity {

    ImageView studentsPic;
    EditText Sname,Sid,Sdept,Semail;

    private  static final int chose_image=101;
    Uri uriProImage;
    String image;
    FirebaseAuth mAuth;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);


        FirebaseDatabase database = FirebaseDatabase.getInstance();



        studentsPic=(ImageView) findViewById(R.id.imageID);
        Sname=(EditText) findViewById(R.id.nameID);
        Sid=(EditText) findViewById(R.id.deptID);
        Sdept=(EditText) findViewById(R.id.studentID);
        Semail=(EditText) findViewById(R.id.emailID);




        studentsPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChoser();

            }
        });
        findViewById(R.id.saveID).setOnClickListener(new View.OnClickListener() {
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
            uriProImage= data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uriProImage);
                studentsPic.setImageBitmap(bitmap);

                uploadInFirebase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadInFirebase() {
        mStorageRef = FirebaseStorage.getInstance().getReference("frontPic"+System.currentTimeMillis()+".jpg");
        if(image !=null){
            mStorageRef.putFile(uriProImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image=taskSnapshot.getDownloadUrl().toString();
                }
            });

        }
    }
    //choose image from phone
    public void imageChoser(){
        Intent intent=new Intent().setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Your Image"),chose_image);
    }

    //save user information to firebase
    public void saveUserInfo(){
        String displyName=Sname.getText().toString();
        if(displyName.isEmpty()){
            Sname.setError("name required");
            Sname.requestFocus();
            return;

        }
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null && image!=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displyName)
                    .setPhotoUri(Uri.parse(image)).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(StudentInfo.this,"profile Updated",Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }


    }

}
