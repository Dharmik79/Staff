package com.example.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import io.paperdb.Paper;

public class login extends AppCompatActivity  {

    EditText email,pass;
    Button bt;
    private TextView tx,t,txt1;
    ProgressBar pr;
    FirebaseAuth fauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=(EditText)findViewById(R.id.e2);
        pass=(EditText)findViewById(R.id.e3);
        bt=(Button)findViewById(R.id.b);
        tx=(TextView)findViewById(R.id.tef2);
        t=findViewById(R.id.e5);
        txt1=findViewById(R.id.loginManager);
        fauth=FirebaseAuth.getInstance();



        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String memail=email.getText().toString().trim();
                final String mpass=pass.getText().toString().trim();
                if(TextUtils.isEmpty(memail))
                {
                    email.setError("Email should not be empty");
                    email.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(mpass))
                {
                    pass.setError("Email should not be empty");
                    pass.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(memail).matches())
                {
                    email.setError("Enter valid Email address");
                    email.requestFocus();
                    return;
                }

                fauth.signInWithEmailAndPassword(memail,mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Common.currentUser=memail;
                            Common.currentPass=mpass;
                            Common.userid=fauth.getUid();
                            checkemail();

                        }
                        else
                            Toast.makeText(login.this,"login  is not  successful",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),newsignup.class));
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),forget.class));
            }
        });
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,managerlogin.class));
            }
        });


    }
    private void checkemail()
    {
        FirebaseUser firebaseUser = fauth.getInstance().getCurrentUser();
        //Boolean emailflag=firebaseUser.isEmailVerified();
      //  if(emailflag)
      //  {
            startActivity(new Intent(getApplicationContext(),StaffHomeActivity.class));
      //  }
      //  else
      //  {
          //  Toast.makeText(getApplicationContext(),"Verify your email",Toast.LENGTH_LONG).show();
          //  fauth.signOut();
       // }
    }



}
