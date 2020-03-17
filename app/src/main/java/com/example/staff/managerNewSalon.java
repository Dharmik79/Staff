package com.example.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class managerNewSalon extends AppCompatActivity {


    private EditText name,email,ph,pass,address;
    String city;
    private Button bt;
    private FirebaseAuth fauth;
    private FirebaseFirestore fstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_new_salon);

        name=findViewById(R.id.ManagerNamesalon);
        email=findViewById(R.id.Manageremailsalon);
        pass=findViewById(R.id.ManagerPasssalon);
        ph=findViewById(R.id.ManagerPhonesalon);
        bt=findViewById(R.id.managersignupsalon);
        address=findViewById(R.id.ManagerAddresssalon);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        city=Common.state_name;

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String memail=email.getText().toString().trim();
                final String mpass=pass.getText().toString().trim();
                final String mph=ph.getText().toString().trim();
                final String mname=name.getText().toString().trim();
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
                        doc = fstore.collection("Managers").document(memail);
                        Map<String, Object> map = new HashMap<>();
                        map.put("Salon Name", mname);
                        map.put("phone number", mph);
                        map.put("email", memail);
                        map.put("Password", mpass);
                        map.put("City",city);
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

                        doc1 = fstore.collection("All Saloon").document(city).collection("Branch").document();
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("name", mname);
                        map1.put("address",maddress);
                        map1.put("userid",userid);
                        doc1.set(map1);
                        DocumentReference doc2;

                        doc2 = fstore.collection("All Saloon").document(city);
                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("name",city);

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
