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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class mprofile extends AppCompatActivity {

    private TextView t1,t2,t3;
    private Button logo,update;
    private FirebaseFirestore mstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mprofile);
        t1=findViewById(R.id.mprofileName);
        t2=findViewById(R.id.mprofileEmail);
        t3=findViewById(R.id.mprofilePhone);
        logo=findViewById(R.id.mlogo);

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
        startActivity(new Intent(mprofile.this,mbarberinfo.class));

    }

    private void load() {

        mstore=FirebaseFirestore.getInstance();
       mstore.collection("Managers").document(Common.currentUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                  //  memailclass mclass=documentSnapshot.toObject(memailclass.class);
                    String name=documentSnapshot.getString("Salon_name");
                    String phone=documentSnapshot.getString("phone_number");
                    String email=documentSnapshot.getString("email");
                     t1.setText(name);
                    t2.setText(email);
                    t3.setText(phone);
                }
           }
       });
    }

    public void logout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(mprofile.this);
        builder.setMessage("Are you sure?").setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),managerlogin.class));
                finish();
            }
        }).setNegativeButton("Cancel",null);

        AlertDialog alert=builder.create();
        alert.show();


    }
}
