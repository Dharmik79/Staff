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

public class managerlogin extends AppCompatActivity {

    private EditText email,pass,name,number;
    private Button bt;
    private TextView tx,t,txt1,forget;
    FirebaseAuth fauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managerlogin);
        email=findViewById(R.id.edt_memail);
        pass=findViewById(R.id.edt_mpass);
        bt=findViewById(R.id.edt_mLogin);
        tx=findViewById(R.id.edt_msignup);
        forget=findViewById(R.id.forget);

        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(managerlogin.this,MainActivity.class));
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(managerlogin.this,forget.class));
            }
        });
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

                        }
                        else
                            Toast.makeText(managerlogin.this,"login  is not  successful",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


    }
}
