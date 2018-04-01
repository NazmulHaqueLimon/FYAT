package com.example.hp.project_1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView newsList;
    private ArrayList<String> ListArray;
    private ArrayAdapter<String> ListAdapter;
    EditText Uemail,Upass;

    ProgressBar pBar2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        //homepage list view for news and events
        newsList=(ListView)findViewById(R.id.listView_id);
        ListArray=new ArrayList<String>();
        ListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ListArray);
        newsList.setAdapter(ListAdapter);
        ListArray.add("Seminar on Carear Boost");
        ListArray.add("RS Campus visit");
        ListArray.add("Seminar on Carear Boost2");
        //setupListViewListener();


        findViewById(R.id.FirstButton).setOnClickListener((View.OnClickListener) this);
        findViewById(R.id.signIn).setOnClickListener(this);
        findViewById(R.id.signUpButton1).setOnClickListener(this);

        Uemail=(EditText) findViewById(R.id.emailID);
        Upass=(EditText)findViewById(R.id.pass);


        pBar2=(ProgressBar)findViewById(R.id.progressBar);

    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.signUpButton1:
                finish();
                startActivity(new Intent(this,SignUpActivity.class));

                break;
            case R.id.signIn:
                userLogin();
                break;
        }

    }



    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() !=null){
              //finish();
           // Intent newIntent=new Intent(MainActivity.this,HomeActivity.class);
        }

    }
    public void userLogin(){
        String email=Uemail.getText().toString().trim();
        String password=Upass.getText().toString().trim();
        //validate the email address
        if(! android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            Uemail.setError("email is not valid");
            Upass.requestFocus();
            return;
        }
        if(email.isEmpty()){
            Uemail.setError("email required");
        }
        if(password.isEmpty()){
            Upass.setError("please type a new password");
            Upass.requestFocus();
            return;

        }
        //pBar2.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //pBar2.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(getApplicationContext(),"SignIn Successfull",Toast.LENGTH_SHORT).show();

                    Intent newIntent=new Intent(MainActivity.this,HomeActivity.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(newIntent);

                    return;
                } else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();

                }

            }
        });

    }


}
