package com.example.myapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.bin.InfoBoardBin;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<InfoBoardBin> items = new ArrayList<>();


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        InfoBoardBin item = items.get(position);

        Glide.with(viewHolder.itemView.getContext())
                .load(item.getContents_uri())
                .into(viewHolder.image_1);

        viewHolder.text1.setText(item.getTitle());
        viewHolder.text2.setText(item.getContent());
        viewHolder.num.setText(""+item.getNum());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<InfoBoardBin> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image_1;
        TextView text1, text2;
        TextView num;

        ViewHolder(View itemView) {
            super(itemView);

            image_1 = itemView.findViewById(R.id.image1);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
            num=itemView.findViewById(R.id.num);

        }
    }
}
