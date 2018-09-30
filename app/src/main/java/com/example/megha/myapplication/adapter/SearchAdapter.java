package com.example.megha.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.megha.myapplication.Pojo.SearchPojo;
import com.example.megha.myapplication.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemHolder> {

    private List<SearchPojo> searchlist;
    private itemListListner itemListListner;
    private Activity activity;


    public SearchAdapter(List<SearchPojo> searchlist, itemListListner itemListListner, Activity activity) {
        this.searchlist = searchlist;
        this.itemListListner = itemListListner;
        this.activity = activity;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        final SearchPojo item = searchlist.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        Glide.with(activity).load(item.getImageURL()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListListner.navigateToWebPage(item.getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchlist != null ? searchlist.size() : 0;
    }


    public interface itemListListner {

        void navigateToWebPage(String title);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public TextView title, description;
        public ImageView imageView;

        public ItemHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.titletext);
            description = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
