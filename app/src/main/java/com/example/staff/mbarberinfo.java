package com.example.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class mbarberinfo extends AppCompatActivity {

    private RecyclerView recycler_barber;
    CollectionReference barberRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbarberinfo);

        recycler_barber=(RecyclerView)findViewById(R.id.recycler_barber);
        initView();
        init();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.manager_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.id1)
        {  logout();
        }
        if(item.getItemId()==R.id.id2)
        {
         startActivity(new Intent(mbarberinfo.this,mprofile.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public  void logout()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(mbarberinfo.this);
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

    @Override
    public void onBackPressed() {

    }

    private void init() {
    barberRef= FirebaseFirestore.getInstance().collection("Managers").document(Common.currentUser).collection("Barber");
        barberRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<mBarber> barbers=new ArrayList<>();

                for (QueryDocumentSnapshot barberSnapShot:task.getResult())
                {
                    mBarber barber=barberSnapShot.toObject(mBarber.class);

                    barbers.add(barber);
                }
                moveto(barbers);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void moveto(ArrayList<mBarber> barbers) {
        MyBarberAdapter adapter=new MyBarberAdapter(this,barbers);
        recycler_barber.setAdapter(adapter);

    }

    private void initView() {
        recycler_barber.setHasFixedSize(true);
        recycler_barber.setLayoutManager(new GridLayoutManager(this,2));
        recycler_barber.addItemDecoration(new SpacesItemDecoration(4));
    }
}
