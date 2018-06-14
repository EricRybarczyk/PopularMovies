package com.example.ericrybarczyk.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ericrybarczyk.popularmovies.model.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends ArrayAdapter<MovieTrailer> {

    @BindView(R.id.trailer_thumbnail) protected ImageView trailerThumbnail;
    @BindView(R.id.trailer_name_value) protected TextView trailerName;
    @BindView(R.id.trailer_site_value) protected TextView trailerSite;
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private Context context;

    public TrailerAdapter(@NonNull Context context, int resource, List<MovieTrailer> trailers) {
        super(context, resource, trailers);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MovieTrailer trailer = getItem(position);
        Context context = getContext();

        if (trailer == null) {
            Log.e(TAG, "Null result for MovieTrailer in data position " + position);
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item, parent, false);
        }

        convertView.setTag(trailer.getKey());
        ButterKnife.bind(this, convertView);

        convertView.setOnClickListener(v -> {
            String trailerKey = v.getTag().toString();
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.context.getString(R.string.intent_youtube_app_base_uri) + trailerKey));

            if (appIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(appIntent);
            } else {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.context.getString(R.string.intent_youtube_web_base_uri) + trailerKey));
                if (webIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(webIntent);
                } else {
                    String message = context.getResources().getString(R.string.error_no_video_player);
                    Log.e(TAG, message);
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            }
        });

        assert trailer != null;
        trailerName.setText(trailer.getName());
        trailerSite.setText(trailer.getSite());
        String thumbnailImagePath = buildThumbnailImagePath(trailer);
        Picasso.with(context)
                .load(thumbnailImagePath)
                .placeholder(R.drawable.ic_movie_placeholder) // TODO - better placeholder for this image
                .error(R.drawable.placeholder_movie_black_18dp) // TODO - better placeholder for this image
                .into(trailerThumbnail);

        return convertView;

    }

    @NonNull
    private String buildThumbnailImagePath(MovieTrailer trailer) {
        String result = null;
        try {
            String baseUrl = context.getString(R.string.youtube_trailer_thumbnail_base_url);
            String filename = context.getString(R.string.youtube_trailer_thumbnail_filename);
            result = baseUrl + trailer.getKey() + "/" + filename;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }
}
