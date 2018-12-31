package com.example.krishna.mp3alarm.adaptersmms;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.krishna.mp3alarm.Constantsmms;

import java.util.List;

/**
 * Created by Shoukhin on 7/7/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ItemViewHolder> {
    private List<Integer> imageList;
    private Activity context;

    public ImageAdapter(List<Integer> imageList, Activity context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(com.example.krishna.mp3alarm.R.layout.image_list_viewmms, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        Glide.with(context).load(imageList.get(position))
                .into(holder.itemImage);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constantsmms.IMAGE_RESOURCE_CODE, imageList.get(position));
                context.setResult(Activity.RESULT_OK, returnIntent);
                context.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;

        public ItemViewHolder(View view) {
            super(view);
            itemImage = view.findViewById(com.example.krishna.mp3alarm.R.id.select_image_view);
        }
    }
}

