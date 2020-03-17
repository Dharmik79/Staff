package com.example.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class newsignup extends AppCompatActivity {

    private TextInputEditText t1,t2,t3;
    private Button b;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsignup);
        t1=findViewById(R.id.edt_name);
        t2=findViewById(R.id.edt_email);
        t3=findViewById(R.id.edt_password);
        b=findViewById(R.id.edt_button);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=t2.getText().toString();
                final String password=t3.getText().toString();
                final  String name=t1.getText().toString();
                final long rate=0;

                fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //   if(task.isSuccessful()) {
                        DocumentReference doc;
                        Common.userid=fauth.getUid();
                        doc = fstore.collection("All Saloon").document(Common.state_name).collection("Branch").document(Common.selectedSalon.getSalonId()).collection("Barber").document(Common.userid);
                        Map<String, Object> map = new HashMap<>();
                        map.put("username",email);
                        map.put("Rating ",rate );
                        map.put("name",name);
                        map.put("password",password);
                        doc.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Log.d(TAG, "on sucess: user profile is created for " + userid);
                                Toast.makeText(newsignup.this,"Registered successfully ",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //    Log.d(TAG, "On failure :" + e.toString());
                            }
                        });

                        sendemail();
                     /*}
                        else
                            Toast.makeText(signup.this,"Register  not successfully",Toast.LENGTH_LONG).show();*/
                    }
                });
            }
        });
    }

    private void sendemail()
    {
        FirebaseUser firebaseUser=fauth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        //Toast.makeText(MySalonAdapterm.this,"Register successfully ,Verification email is sent",Toast.LENGTH_LONG).show();
                        fauth.signOut();
                        //context.startActivity(new Intent(MySalonAdapterm.this,loginm.class));
                    //    Intent staffsignup=new Intent(context,MainActivity.class);
                     //   context.startActivity(staffsignup);
                        startActivity(new Intent(newsignup.this,login.class));
                    }
                    else
                    {
                        // Toast.makeText(.this,"Verification emailhas not been sent",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }


    }

}
