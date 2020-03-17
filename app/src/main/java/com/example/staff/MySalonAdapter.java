package com.example.staff;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySalonAdapter extends RecyclerView.Adapter<MySalonAdapter.MyViewHolder> {

    Context context;
    List<Salon> salonList;
    List<CardView> cardViewList;


    public MySalonAdapter(Context context, List<Salon> salonList) {
        this.context = context;
        this.salonList = salonList;
        cardViewList=new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(context).inflate(R.layout.layout_salon,parent,false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.txt_salon_name.setText(salonList.get(position).getName());
        holder.txt_salon_address.setText(salonList.get(position).getAddress());

        if(!cardViewList.contains(holder.card_salon))
        {
            cardViewList.add(holder.card_salon);
        }
        holder.setiRecyclerItemSelectedListner(new IRecyclerItemSelectedListner() {
            @Override
            public void onItemSelected(View view, int pos) {
                Common.selectedSalon=salonList.get(pos);
                Intent staffsignup=new Intent(context,loginandsignup.class);
                context.startActivity(staffsignup);

            }
        });

    }

    @Override
    public int getItemCount() {
        return salonList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_salon_name,txt_salon_address;
        CardView card_salon;

        IRecyclerItemSelectedListner iRecyclerItemSelectedListner;

        public void setiRecyclerItemSelectedListner(IRecyclerItemSelectedListner iRecyclerItemSelectedListner) {
            this.iRecyclerItemSelectedListner = iRecyclerItemSelectedListner;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_salon=(CardView)itemView.findViewById(R.id.card_salon);
            txt_salon_address=(TextView)itemView.findViewById(R.id.txt_salon_address);
            txt_salon_name=(TextView)itemView.findViewById(R.id.txt_salon_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListner.onItemSelected(v,getAdapterPosition());
        }
    }

}
