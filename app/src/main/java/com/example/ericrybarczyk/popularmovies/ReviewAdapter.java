package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ericrybarczyk.popularmovies.model.MovieReview;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends PagerAdapter {

    private Context context;
    private List<MovieReview> movieReviews;
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    @BindView(R.id.review_item_content) protected TextView reviewText;

    public ReviewAdapter(Context context, List<MovieReview> reviews) {
        this.context = context;
        this.movieReviews = reviews;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //return super.instantiateItem(container, position);
        MovieReview movieReview = movieReviews.get(position);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        ViewGroup currentLayout = (ViewGroup) inflater.inflate(R.layout.review_list_item, container, false);

        try {
            ButterKnife.bind(this, currentLayout); // ButterKnife.bind(context, currentLayout)
            reviewText.setText(movieReview.getContent());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        container.addView(currentLayout);
        return currentLayout;
    }

    @Override
    public int getCount() {
        return movieReviews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object; // TODO - figure out what to do here
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // TODO - figure out if this is actually the best way?
        container.removeView((View) object);
    }
}
