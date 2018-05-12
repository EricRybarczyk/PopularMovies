package com.example.ericrybarczyk.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private List<Movie> movieList;

    public MovieAdapter() {
//        try {
//            this.movieList = movieService.getMovies();
//        } catch (IOException e) {
//            e.printStackTrace(); // TODO - log and do something better for the user
//        }
    }

    public void setMovieData(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.movieTextView.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        }
        return movieList.size();
    }


    public class MovieHolder extends RecyclerView.ViewHolder {

        ImageView movieImageView;
        TextView movieTextView;

        public MovieHolder(View itemView) {
            super(itemView);
            // TODO - look into using Butterknife
//            movieImageView = itemView.findViewById(R.id.iv_movie_item);
            movieTextView = itemView.findViewById(R.id.iv_movie_item);
        }

        void bind() {
            // TODO - I think this is where I will set up the image ... or do I not really need this? Other demos just handle in constructor like above.
        }
    }

}
