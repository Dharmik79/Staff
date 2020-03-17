package com.example.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class msignup extends AppCompatActivity {

    private EditText name,email,ph,pass,city,address;
    private Button bt;
    private TextView txt;

    private FirebaseAuth fauth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msignup);
        name=findViewById(R.id.ManagerName);
        email=findViewById(R.id.Manageremail);
        pass=findViewById(R.id.ManagerPass);
        ph=findViewById(R.id.ManagerPhone);
        city=findViewById(R.id.ManagerCity);
        bt=findViewById(R.id.managersignup);
        address=findViewById(R.id.ManagerAddress);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String memail=email.getText().toString().trim();
                final String mpass=pass.getText().toString().trim();
                final String mph=ph.getText().toString().trim();
                final String mname=name.getText().toString().trim();
                final String mcity=city.getText().toString().trim();
                final String maddress=address.getText().toString().trim();
                if(TextUtils.isEmpty(memail))
                {
                    email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(mpass))
                {
                    pass.setError("password is required");
                    return;
                }
                if(mpass.length()<6)
                {
                    pass.setError("Password should be grater than 6");
                    return;
                }
                if (TextUtils.isEmpty(mcity))
                {
                    city.setError("City required");
                    return;
                }
                if (TextUtils.isEmpty(maddress))
                {
                    address.setError("Address required");
                    return;
                }

                fauth.createUserWithEmailAndPassword(memail,mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //   if(task.isSuccessful()) {
                        DocumentReference doc;
                        String userid=fauth.getUid();
                        doc = fstore.collection("Managers").document(userid);
                        Map<String, Object> map = new HashMap<>();
                        map.put("Salon Name", mname);
                        map.put("phone number", mph);
                        map.put("email", memail);
                        map.put("Password", mpass);
                        map.put("City",mcity);
                        map.put("userid",userid);
                        doc.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                        DocumentReference doc1;

                        doc1 = fstore.collection("All Saloon").document(mcity).collection("Branch").document();
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("name", mname);
                       map1.put("address",maddress);
                       map1.put("userid",userid);
                        doc1.set(map1);
                        DocumentReference doc2;

                        doc2 = fstore.collection("All Saloon").document(mcity);
                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("name", mcity);

                        doc2.set(map2);


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
        /*FirebaseUser firebaseUser=fauth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(msignup.this,"Register successfully ,Verification email is sent",Toast.LENGTH_LONG).show();
                        fauth.signOut();
                        finish();*/
                        startActivity(new Intent(getApplicationContext(),login.class));
                   /* }
                    else
                    {
                        Toast.makeText(msignup.this,"Verification emailhas not been sent",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }*/


    }

}
