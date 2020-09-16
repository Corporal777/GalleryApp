package com.example.galleryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "GalleryActivity";
    private RecyclerView photoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoRecyclerView = findViewById(R.id.photo_gallery_recycler_view);
        photoRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        new FetchItemTask().execute();
        setupAdapter();
    }

    private void setupAdapter (){
        photoRecyclerView.setAdapter(new PhotoAdapter(mItems));
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView itemImageView;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.photo_gallery_image_view);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> items) {
            mGalleryItems = items;
        }

        @NonNull
        @Override
        public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(GalleryActivity.this);
            View v = inflater.inflate(R.layout.gallery_item, parent,false);

            return new PhotoHolder(v);

        }

        @Override
        public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            Picasso.with(GalleryActivity.this).load(galleryItem.getUrl()).into(holder.itemImageView);


        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }


    private  class FetchItemTask extends AsyncTask<Void, Void, List<GalleryItem>>{

        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {
            return new FlickFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            setupAdapter();
        }
    }



}