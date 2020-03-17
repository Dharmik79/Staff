package com.example.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SalonListActivity extends AppCompatActivity implements IOnLoadCountSalon, IBranchLoadListner {

    TextView txt_salon_count;
    RecyclerView recycler_salon;

    IOnLoadCountSalon iOnLoadCountSalon;
    IBranchLoadListner iBranchLoadListner;

    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_list);

        txt_salon_count=findViewById(R.id.txt_salon_count);
        recycler_salon=findViewById(R.id.recycler_salon);

        initView();

        init();
        loadSalonBaseOnCity(Common.state_name);
    }

    private void loadSalonBaseOnCity(String name) {

        FirebaseFirestore.getInstance().collection("All Saloon").document(name).collection("Branch").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful())
                {
                    List<Salon> salons=new ArrayList<>();
                    iOnLoadCountSalon.onLoadCountSalonSuccess(task.getResult().size());
                    for (DocumentSnapshot salonSnapshot:task.getResult())
                    {
                        Salon salon=salonSnapshot.toObject(Salon.class);
                        salon.setSalonId(salonSnapshot.getId());
                        salons.add(salon);
                    }
                    iBranchLoadListner.onAllBranchLoadSuccess(salons);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBranchLoadListner.onAllBranchLoadFailed(e.getMessage());
            }
        });
    }

    private void init() {

        iOnLoadCountSalon=this;
        iBranchLoadListner=this;
    }

    private void initView() {

        recycler_salon.setHasFixedSize(true);
        recycler_salon.setLayoutManager(new GridLayoutManager(this,2));
        recycler_salon.addItemDecoration(new SpacesItemDecoration(8));

    }


    @Override
    public void onLoadCountSalonSuccess(int count) {
        txt_salon_count.setText(new StringBuilder("All Salon (").append(count).append(")"));

    }

    @Override
    public void onAllBranchLoadSuccess(List<Salon> salonList) {
        MySalonAdapter salonAdapter=new MySalonAdapter(this,salonList);
        recycler_salon.setAdapter(salonAdapter);

    }

    @Override
    public void onAllBranchLoadFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void addSalon(View view) {
        startActivity(new Intent(SalonListActivity.this,managerNewSalon.class));

    }
}
