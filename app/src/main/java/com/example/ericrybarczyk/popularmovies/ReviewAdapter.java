package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ericrybarczyk.popularmovies.model.MovieReview;
import com.example.ericrybarczyk.popularmovies.utils.MovieAppConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends PagerAdapter {

    private final List<MovieReview> movieReviews;
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    @BindView(R.id.review_item_content) protected TextView reviewText;
    @BindView(R.id.review_author_name_label) protected TextView authorNameLabel;
    @BindView(R.id.review_author_name_content) protected TextView authorNameContent;

    ReviewAdapter(List<MovieReview> reviews) {
        this.movieReviews = reviews;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        MovieReview movieReview = movieReviews.get(position);
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ViewGroup currentLayout = (ViewGroup) inflater.inflate(R.layout.review_list_item, container, false);

        try {
            ButterKnife.bind(this, currentLayout); // ButterKnife.bind(context, currentLayout)
            reviewText.setText(movieReview.getContent());

            if (movieReview.getId() == MovieAppConstants.NO_REVIEW_ID) {
                reviewText.setTextColor(ContextCompat.getColor(container.getContext(), R.color.colorAlert));
                authorNameLabel.setVisibility(View.GONE);
                authorNameContent.setVisibility(View.GONE);
            } else {
                authorNameContent.setText(movieReview.getAuthor());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
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
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
