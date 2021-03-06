package com.example.staff;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyStateAdapter extends RecyclerView.Adapter<MyStateAdapter.MyViewHolder>{

    Context context;
    List<City> cityList;
    int lastPosition=-1;

    public MyStateAdapter(Context context, List<City> cityList) {
        this.context = context;
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.layout_state,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, final int position) {
        holder.txt_state_name.setText(cityList.get(position).getName());

        setAnimation(holder.itemView,position);

        holder.setiRecyclerItemSelectedListner(new IRecyclerItemSelectedListner() {
            @Override
            public void onItemSelected(View view, int pos) {
                Common.state_name=cityList.get(position).getName();
                context.startActivity(new Intent(context,SalonListActivity.class));
            }
        });

    }

    private void setAnimation(View itemView,int position) {

        if (position>lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left);
            itemView.startAnimation(animation);
            lastPosition=position;

        }

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_state_name;
        IRecyclerItemSelectedListner iRecyclerItemSelectedListner;

        public void setiRecyclerItemSelectedListner(IRecyclerItemSelectedListner iRecyclerItemSelectedListner) {
            this.iRecyclerItemSelectedListner = iRecyclerItemSelectedListner;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_state_name=itemView.findViewById(R.id.txt_state_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListner.onItemSelected(v,getAdapterPosition());
        }
    }

}
