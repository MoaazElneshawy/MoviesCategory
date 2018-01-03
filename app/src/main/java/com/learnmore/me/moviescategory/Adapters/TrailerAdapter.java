package com.learnmore.me.moviescategory.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learnmore.me.moviescategory.R;
import com.learnmore.me.moviescategory.Models.TrailerModel;

import java.util.List;

/**
 * Created by moaazelneshawy on 01/01/18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    public TrailerAdapter(Context context, List<TrailerModel> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    Context context;
    List<TrailerModel> trailers;

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, final int position) {
        holder.trailer_name.setText(trailers.get(position).getName());
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(position).getKey());
                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                Log.i("Video", "Video Playing...." + trailers.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {

        ImageView play;
        TextView trailer_name;


        public TrailerViewHolder(View itemView) {
            super(itemView);
            play = itemView.findViewById(R.id.trailer_play);
            trailer_name = itemView.findViewById(R.id.trailer_name);
        }
    }
}
