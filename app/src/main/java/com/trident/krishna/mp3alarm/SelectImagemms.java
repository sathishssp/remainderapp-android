package com.trident.krishna.mp3alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.trident.krishna.mp3alarm.adaptersmms.ImageAdapter;
import com.trident.krishna.mp3alarm.resourceManagermms.ResourceManager;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectImagemms extends AppCompatActivity {

    private ArrayList<Integer> imageData = new ArrayList<>(Arrays.asList(
            ResourceManager.imageResources));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trident.krishna.mp3alarm.R.layout.activity_select_imagemms);

        RecyclerView recyclerView = findViewById(com.trident.krishna.mp3alarm.R.id.image_recycler_view);
        ImageAdapter imageAdapter = new ImageAdapter(imageData, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(imageAdapter);
    }

}
