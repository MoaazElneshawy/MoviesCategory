package com.learnmore.me.moviescategory.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.learnmore.me.moviescategory.Adapters.MovieAdapter;
import com.learnmore.me.moviescategory.Models.MovieModel;
import com.learnmore.me.moviescategory.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final static String BASE = "http://api.themoviedb.org/3/movie/";
    final static String POPULAR_CATEGORY = "popular";
    final static String TOP_RATED_CATEGORY = "top_rated";
    final static String API_PARAM = "?api_key=";
    final static String API_KEY = "-- API KEY --";
    final static String CATEGORY_KEY = "category_key";
    final static String FAVORITES_CATEGORY = "favorites";

    RequestQueue queue;
    RecyclerView mMoviesRV;
    MovieAdapter adapter;
    List<MovieModel> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // define the request of Volley
        queue = Volley.newRequestQueue(this);
        movies = new ArrayList<>();
        //Define the RecyclerView and Its properties
        mMoviesRV = (RecyclerView) findViewById(R.id.rv_movies);
        mMoviesRV.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MovieAdapter(this, movies);

        if (savedInstanceState != null) {
            if (savedInstanceState.get(CATEGORY_KEY).equals(TOP_RATED_CATEGORY)) {
                requestMovies(BASE + TOP_RATED_CATEGORY + API_PARAM + API_KEY);
            }
        } else {
            requestMovies(BASE + POPULAR_CATEGORY + API_PARAM + API_KEY);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order_popular:
                requestMovies(BASE + POPULAR_CATEGORY + API_PARAM + API_KEY);
                return true;
            case R.id.order_top_rated:
                requestMovies(BASE + TOP_RATED_CATEGORY + API_PARAM + API_KEY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void requestMovies(String order) {

        JsonObjectRequest request = new JsonObjectRequest(order, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    if (movies.size() > 0) {
                        movies.clear();
                        adapter.notifyDataSetChanged();
                    }
                    jsonFormat(response);
                } else {
                    Toast.makeText(MainActivity.this, "No thing !", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Connection Failure !", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);

    }

    private void jsonFormat(JSONObject response) {
        if (movies.size() > 0) movies.clear();
        try {
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieObject = results.getJSONObject(i);
                MovieModel model = new MovieModel();
                model.setId(movieObject.getInt("id"));
                model.setOverview(movieObject.getString("overview"));
                model.setTitle(movieObject.getString("title"));
                double vote = movieObject.getDouble("vote_average");
                model.setVoteAverage(vote);
                model.setReleaseDate(movieObject.getString("release_date"));
                model.setPosterPath("http://image.tmdb.org/t/p/w500/" + movieObject.getString("poster_path"));
                movies.add(model);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mMoviesRV.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.getString(CATEGORY_KEY, TOP_RATED_CATEGORY);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getString(CATEGORY_KEY);
    }
}
