package com.learnmore.me.moviescategory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ahmed Badr on 7/12/2017.
 */

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
        Picasso.with(context)
                .load(movies.get(position).getPoster())
                .into(holder.iv_poster1);

        holder.iv_poster1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MovieDetailsActivity.class);
                intent.putExtra("poster",movies.get(position).getPoster());
                intent.putExtra("title",movies.get(position).getTitle());
                intent.putExtra("overview",movies.get(position).getOverview());
                intent.putExtra("vote_average",movies.get(position).getVote_average());
                intent.putExtra("release_date",movies.get(position).getRelease_date());
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
