package com.example.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profile extends AppCompatActivity {

    private TextView t1,t2,t3;
    private Button logo,update;
    private FirebaseFirestore mstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        t1=findViewById(R.id.profileName);
        t2=findViewById(R.id.profileEmail);
        t3=findViewById(R.id.profilePhone);
        logo=findViewById(R.id.logo);

        load();
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(profile.this,StaffHomeActivity.class));

        super.onBackPressed();
    }

    private void load() {

        mstore=FirebaseFirestore.getInstance();
        mstore.collection("users").document(Common.currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if (documentSnapshot.exists() && documentSnapshot!=null)
                    {
                        String name=documentSnapshot.getString("Fname");
                        String phone=documentSnapshot.getString("phone number");
                        String email=documentSnapshot.getString("email");

                        t1.setText(name);
                        t2.setText(email);
                        t3.setText(phone);

                    }


                }
                else
                {
                    Toast.makeText(profile.this,"Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void logout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(profile.this);
        builder.setMessage("Are you sure?").setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        }).setNegativeButton("Cancel",null);

        AlertDialog alert=builder.create();
        alert.show();


    }
}
