package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {


    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class MovieHolder extends RecyclerView.ViewHolder {

        ImageView movieImageView;

        public MovieHolder(View itemView) {
            super(itemView);
            // TODO - look into using Butterknife
            movieImageView = itemView.findViewById(R.id.iv_movie_item);
        }

        void bind() {
            // TODO - I think this is where I will set up the image
        }
    }

}
