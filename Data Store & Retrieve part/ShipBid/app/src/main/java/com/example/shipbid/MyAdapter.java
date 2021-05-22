package com.example.shipbid;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter <MyAdapter.MyViewHolder>{

    ArrayList<Model> mList;
    Context context;


    public MyAdapter(Context context, ArrayList<Model> mList){
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Model model = mList.get(position);
        holder.pName.setText(model.getProductName());
        holder.pType.setText(model.getProductType());
        holder.sValue.setText(model.getStartingValue());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView pName, pType, sValue;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pName = itemView.findViewById(R.id.pName_text);
            pType = itemView.findViewById(R.id.pType_text);
            sValue = itemView.findViewById(R.id.startingValue_text);
        }
    }
}
