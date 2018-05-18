package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ericrybarczyk.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private List<Movie> movieList;
    private Context parentContext;
    private final MovieAdapterOnClickHandler movieItemClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(int movieId);
    }

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        parentContext = context;
        movieItemClickHandler = clickHandler;
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

        // TODO - improve Picasso use - use error() and placeholder()
        Picasso.with(parentContext)
                .load(movie.getImagePath())
                .into(holder.movieImageView);
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        }
        return movieList.size();
    }


    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView movieImageView;
        //TextView movieTextView;

        public MovieHolder(View itemView) {
            super(itemView);

            // TODO - look into using Butterknife
            movieImageView = itemView.findViewById(R.id.iv_movie_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            int movieId = movieList.get(adapterPosition).getId();
            movieItemClickHandler.onClick(movieId);
        }
    }

}
