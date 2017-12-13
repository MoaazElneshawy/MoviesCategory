package com.learnmore.me.moviescategory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    Intent comingIntent;
    String poster , title , overview , vote_average , release_date ;
    ImageView mPoster;
    TextView mTitle,mOverview , mVoteAverage , mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        init();

        comingIntent = getIntent();

        if (comingIntent != null){
            poster = comingIntent.getStringExtra("poster");
            title = comingIntent.getStringExtra("title");
            overview = comingIntent.getStringExtra("overview");
            vote_average = comingIntent.getStringExtra("vote_average");
            release_date = comingIntent.getStringExtra("release_date");

            setValuesToUI(poster,title,overview,vote_average,release_date);
        }

    }

    private void init(){

        mPoster = findViewById(R.id.movie_iv_poster);
        mTitle = findViewById(R.id.movie_tv_title);
        mOverview = findViewById(R.id.movie_tv_overview);
        mVoteAverage = findViewById(R.id.movie_tv_vote_average);
        mReleaseDate = findViewById(R.id.movie_tv_release_date);

    }

    private void setValuesToUI(String poster , String title , String overview , String vote_average , String release_date){

        Picasso.with(this)
                .load(poster)
                .into(mPoster);

        mTitle.setText(title);
        mOverview.setText(overview);
        mVoteAverage.setText(vote_average);
        mReleaseDate.setText(release_date);

    }
}
