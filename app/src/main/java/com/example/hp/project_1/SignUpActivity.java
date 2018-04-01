package com.example.hp.project_1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    //private static final String TAG ="SignUpActivity" ;
    private FirebaseAuth mAuth;
    EditText uEmail,uPass;
    ProgressBar pBar;
    RadioGroup radioGroup;
    RadioButton radioSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);

        //userName=(EditText)findViewById(R.id.name_id);
        uEmail=(EditText)findViewById(R.id.email_id);
        uPass=(EditText)findViewById(R.id.pass_id);
        pBar=(ProgressBar)findViewById(R.id.progressBar);
        //findViewById(R.id.signUpButton2).setOnClickListener((View.OnClickListener) this);


    }



    public void onClick(View view){
        switch (view.getId()){
            case R.id.signUpButton2:
                registerUser();

               //startActivity(new Intent(this,StudentInfo.class));

                break;

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void registerUser(){
        String email=uEmail.getText().toString().trim();
        String password=uPass.getText().toString().trim();
        //validate the email address
        if(! android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            uEmail.setError("email is not valid");
            uEmail.requestFocus();
            return;
        }
        if(email.isEmpty()){
            uEmail.setError("email required");
        }
        if(password.isEmpty()){
            uPass.setError("please type a new password");
            uPass.requestFocus();
            return;

        }
        pBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            finish();
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(),"User registered Successfully",Toast.LENGTH_SHORT).show();

                            int radioButtonID = radioGroup.getCheckedRadioButtonId();
                            //View radioButton = radioGroup.findViewById(radioButtonID);
                            if(radioButtonID==R.id.radioStudent){
                                Intent newIntent=new Intent(SignUpActivity.this,StudentInfo.class);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(newIntent);

                            }
                            else if (radioButtonID==R.id.radioMentor){
                                Intent newIntent=new Intent(SignUpActivity.this,MentorsInfo.class);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(newIntent);

                            }
                            else if (radioButtonID==R.id.radioAdvisor){
                                Intent newIntent=new Intent(SignUpActivity.this,AdvisorInfo.class);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(newIntent);

                            }


                            return;
                        }
                        else {
                              if(task.getException()instanceof FirebaseAuthUserCollisionException){
                                  Toast.makeText(getApplicationContext(),"You are allready registered",Toast.LENGTH_SHORT).show();

                              }
                              else{
                                  Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                              }

                        }


                    }


                });



    }



}
