package com.learnmore.me.moviescategory.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.learnmore.me.moviescategory.Activities.MovieDetailsActivity;
import com.learnmore.me.moviescategory.Provider.DataContract;
import com.learnmore.me.moviescategory.R;
import com.squareup.picasso.Picasso;

/**
 * Created by moaazelneshawy on 09/01/18.
 */

public class CursorAdapter extends RecyclerView.Adapter<CursorAdapter.CursorHolder> {

    Cursor cursor;
    Context context;

    public CursorAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
    }

    @Override
    public CursorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.movie_layout, parent, false);

        return new CursorHolder(view);
    }

    @Override
    public void onBindViewHolder(CursorHolder holder, final int position) {
        if (cursor != null || cursor.getCount() != 0) {
            cursor.moveToFirst();
            Picasso.with(context)
                    .load(posterUrl(cursor,position))
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.iv_poster1);
        }
        holder.iv_poster1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToPosition(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra("intent", "cursor");
                intent.putExtra("title", cursor.getString(cursor.getColumnIndex(DataContract.TableContract.MOVIE_TITLE)));
                intent.putExtra("overview", cursor.getString(cursor.getColumnIndex(DataContract.TableContract.MOVIE_OVERVIEW)));
                intent.putExtra("vote", cursor.getString(cursor.getColumnIndex(DataContract.TableContract.MOVIE_VOTE)));
                intent.putExtra("poster", cursor.getString(cursor.getColumnIndex(DataContract.TableContract.MOVIE_POSTER)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    class CursorHolder extends RecyclerView.ViewHolder {

        ImageView iv_poster1;

        public CursorHolder(View view) {
            super(view);
            iv_poster1 = view.findViewById(R.id.iv_first);

        }
    }

    String posterUrl(Cursor cursor, int position) {
        String poster = "";
        cursor.moveToPosition(position);
        poster = cursor.getString(cursor.getColumnIndex(DataContract.TableContract.MOVIE_POSTER));
        return poster;
    }
}