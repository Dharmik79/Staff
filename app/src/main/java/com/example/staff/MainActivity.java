package com.example.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IOnAllStateLoadListner {

    RecyclerView recycler_state;

    CollectionReference allSalonCollection;

    IOnAllStateLoadListner iOnAllStateLoadListner;


    MyStateAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_state=findViewById(R.id.recycler_state);
        initView();

        init();

        loadAllStateFromFireStore();
    }
    private void loadAllStateFromFireStore() {

        allSalonCollection.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iOnAllStateLoadListner.onAllStateLoadFailed(e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    List<City> cities =new ArrayList<>();
                    for(DocumentSnapshot citySnapShot:task.getResult())
                    {
                        City city=citySnapShot.toObject(City.class);
                        cities.add(city);
                    }
                    iOnAllStateLoadListner.onAllStateLoadSuccess(cities);

                }
            }
        });

    }

    private void init() {
        allSalonCollection = FirebaseFirestore.getInstance().collection("All Saloon");
        iOnAllStateLoadListner=this;

    }

    private void initView() {
        //  recycler_state.setHasFixedSize(true);
        recycler_state.setLayoutManager(new GridLayoutManager(this,2));
        recycler_state.addItemDecoration(new SpacesItemDecoration(8));


    }

    @Override
    public void onAllStateLoadSuccess(List<City> cityList) {
        adapter=new MyStateAdapter(this,cityList);
        recycler_state.setAdapter(adapter);

    }

    @Override
    public void onAllStateLoadFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    public void addCity(View view) {
        startActivity(new Intent(MainActivity.this,msignup.class));

    }

    @Override
    public void onBackPressed() {

    }
}
