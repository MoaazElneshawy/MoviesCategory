package com.learnmore.me.moviescategory.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.learnmore.me.moviescategory.Activities.MovieDetailsActivity;
import com.learnmore.me.moviescategory.Models.MovieModel;
import com.learnmore.me.moviescategory.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    Context context;
    List<MovieModel> movies;

    public MovieAdapter(Context context, List<MovieModel> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_layout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        String url = movies.get(position).getPosterPath();
                Picasso.with(context)
                        .load(url)
                        .error(R.drawable.ic_launcher_background)
                        .into(holder.iv_poster1);

        holder.iv_poster1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra("movie",(Parcelable) movies.get(position));
                intent.putExtra("m","movie");

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_poster1;


        public MovieViewHolder(View view) {
            super(view);
            iv_poster1 = view.findViewById(R.id.iv_first);

        }
    }

}
