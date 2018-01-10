package com.learnmore.me.moviescategory.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.learnmore.me.moviescategory.Adapters.ReviewsAdapter;
import com.learnmore.me.moviescategory.Adapters.TrailerAdapter;
import com.learnmore.me.moviescategory.Models.MovieModel;
import com.learnmore.me.moviescategory.Models.ReviewModel;
import com.learnmore.me.moviescategory.Models.TrailerModel;
import com.learnmore.me.moviescategory.Provider.DataContract;
import com.learnmore.me.moviescategory.Provider.DatabaseHelper;
import com.learnmore.me.moviescategory.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {

    Intent comingIntent;
    LinearLayout mReviewL;
    ImageView mPoster;
    TextView mTitleTV, mOverviewTV, mVoteAverageTV, mYearTV, mTrailerTV, mReviewsTV, mReviewAuthorTV, mReviewContentTV;
    RecyclerView mTrailersRV, mReviewsRV;
    Button mAddToFavBTN;
    RequestQueue queue;
    MovieModel model;
    List<TrailerModel> trailers;
    List<ReviewModel> reviews;
    public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String VIDEOS = "/videos";
    public static final String REVIEWS = "/reviews";
    public static final String API_KEY = "?api_key= -- API KEY --";
    DatabaseHelper dbHelper;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        init();
        dbHelper = new DatabaseHelper(this);

        comingIntent = getIntent();
        if (comingIntent != null) {
            String extra = comingIntent.getStringExtra("intent");
            if (extra.equalsIgnoreCase("movie")) {
                model = comingIntent.getParcelableExtra("movie");
                setValuesToUI(model);
            } else if (extra.equalsIgnoreCase("cursor")) {
                String title = comingIntent.getStringExtra("title");
                String vote = comingIntent.getStringExtra("vote");
                String overview = comingIntent.getStringExtra("overview");
                String poster = comingIntent.getStringExtra("poster");
                cursorData(title, vote, overview, poster);
            }
        }


    }

    private void init() {
        queue = Volley.newRequestQueue(this);
        trailers = new ArrayList<>();
        reviews = new ArrayList<>();
        mReviewL = findViewById(R.id.linear_reviews);
        mReviewAuthorTV = findViewById(R.id.author);
        mReviewContentTV = findViewById(R.id.content);
        mTrailerTV = findViewById(R.id.trailers_tv);
        mReviewsTV = findViewById(R.id.reviews_tv);
        mPoster = findViewById(R.id.movie_iv_poster);
        mTitleTV = findViewById(R.id.movie_tv_title);
        mOverviewTV = findViewById(R.id.movie_tv_overview);
        mVoteAverageTV = findViewById(R.id.movie_tv_vote_average);
        mYearTV = findViewById(R.id.movie_tv_year);
        mAddToFavBTN = findViewById(R.id.movie_add_to_fav_btn);
        mTrailersRV = findViewById(R.id.trailers_rv);
        mTrailersRV.setLayoutManager(new LinearLayoutManager(this));
        mReviewsRV = findViewById(R.id.reviews_rv);
        mReviewsRV.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setValuesToUI(MovieModel movie) {
        Picasso.with(this)
                .load(movie.getPosterPath())
                .into(mPoster);
        mTitleTV.setText(movie.getTitle());
        mOverviewTV.setText(movie.getOverview());
        mVoteAverageTV.setText(String.valueOf(movie.getVoteAverage()));
        mYearTV.setText(movie.getReleaseDate().substring(0, 4));

        getTrailers(String.valueOf(model.getId()));
        getReviews(String.valueOf(model.getId()));


    }

    private void getTrailers(String movieId) {
        String url = BASE_URL + movieId + VIDEOS + API_KEY;
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                jsonFormaterForTrailers(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }


    private void jsonFormaterForTrailers(JSONObject response) {
        if (trailers.size() > 0) trailers.clear();
        try {
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                TrailerModel model = new TrailerModel();
                JSONObject trailerObject = results.getJSONObject(i);
                model.setKey(trailerObject.getString("key"));
                model.setName(trailerObject.getString("name"));
                trailers.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (trailers.size() == 0) {
            mTrailerTV.setText(getString(R.string.no_trailers));
            mTrailersRV.setVisibility(View.GONE);
        }
        TrailerAdapter adapter = new TrailerAdapter(this, trailers);
        mTrailersRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    private void getReviews(String movieId) {
        String url = BASE_URL + movieId + REVIEWS + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                jsonFormaterForReviews(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    private void jsonFormaterForReviews(JSONObject response) {

        if (reviews.size() > 0) reviews.clear();
        try {
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject reviewJson = results.getJSONObject(i);
                ReviewModel model = new ReviewModel();
                model.setAuthor(reviewJson.getString("author"));
                model.setContent(reviewJson.getString("content"));
                reviews.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (reviews.size() == 0) {
            mReviewsTV.setText(getString(R.string.no_reviews));
            mReviewL.setVisibility(View.GONE);
        }

        ReviewsAdapter adapter = new ReviewsAdapter(this, reviews);
        mReviewsRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public void addMovieToFavorite(View view) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DataContract.TableContract.MOVIE_ID, model.getId());
        contentValues.put(DataContract.TableContract.MOVIE_TITLE, model.getTitle());
        contentValues.put(DataContract.TableContract.MOVIE_POSTER, model.getPosterPath());
        contentValues.put(DataContract.TableContract.MOVIE_OVERVIEW, model.getOverview());
        contentValues.put(DataContract.TableContract.MOVIE_VOTE, model.getVoteAverage());
        this.getContentResolver().insert(DataContract.TableContract.CONTENT_URI_TABLE, contentValues);
        Toast.makeText(this, getString(R.string.movie_added), Toast.LENGTH_SHORT).show();
    }

    public void cursorData(String title, String vote, String overview, String poster) {

        mTrailersRV.setVisibility(View.GONE);
        mTrailerTV.setVisibility(View.GONE);
        mReviewsRV.setVisibility(View.GONE);
        mReviewsTV.setVisibility(View.GONE);
        mReviewContentTV.setVisibility(View.GONE);
        mReviewAuthorTV.setVisibility(View.GONE);
        try {
            mTitleTV.setText(title);
            mOverviewTV.setText(overview);
            mVoteAverageTV.setText(vote);
            Picasso.with(this)
                    .load(poster)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(mPoster);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MovieDetailsActivity.this,MainActivity.class));
    }
}
