package com.learnmore.me.moviescategory.Activities;

import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.learnmore.me.moviescategory.Adapters.CursorAdapter;
import com.learnmore.me.moviescategory.Adapters.MovieAdapter;
import com.learnmore.me.moviescategory.Models.MovieModel;
import com.learnmore.me.moviescategory.Provider.DataContract;
import com.learnmore.me.moviescategory.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks {

    final static String BASE = "http://api.themoviedb.org/3/movie/";
    final static String POPULAR_CATEGORY = "popular";
    final static String TOP_RATED_CATEGORY = "top_rated";
    final static String API_PARAM = "?api_key=";
    final static String API_KEY = "-- API KEY -- ";
    final static String CATEGORY_KEY = "category_key";
    final static String FAVORITES_CATEGORY = "favorites";
    static String CURRENT_STATE = "current";


    RequestQueue queue;
    RecyclerView mMoviesRV;
    MovieAdapter adapter;
    List<MovieModel> movies;
    CursorLoader loader;
    Cursor cursor;

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

        getSupportLoaderManager().initLoader(1, null, this);

        if (savedInstanceState != null) {
            String current = savedInstanceState.getString(CATEGORY_KEY);
            if (current != null) {
                if (current.equalsIgnoreCase(TOP_RATED_CATEGORY)) {
                    requestMovies(BASE + TOP_RATED_CATEGORY + API_PARAM + API_KEY);
                } else if (current.equalsIgnoreCase(FAVORITES_CATEGORY)) {
                    getFavoriteMovies();
                } else {
                    requestMovies(BASE + POPULAR_CATEGORY + API_PARAM + API_KEY);
                }
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
                CURRENT_STATE = POPULAR_CATEGORY;
                return true;
            case R.id.order_top_rated:
                requestMovies(BASE + TOP_RATED_CATEGORY + API_PARAM + API_KEY);
                CURRENT_STATE = TOP_RATED_CATEGORY;
                return true;
            case R.id.order_favorite:
                CURRENT_STATE = FAVORITES_CATEGORY;
                getFavoriteMovies();
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
        outState.putString(CATEGORY_KEY, CURRENT_STATE);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getString(CATEGORY_KEY);

    }

    protected void getFavoriteMovies() {
        CursorAdapter adapter = new CursorAdapter(cursor, this);
        mMoviesRV.setAdapter(adapter);
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri uri = DataContract.TableContract.CONTENT_URI_TABLE;
        loader = new CursorLoader(this, uri, null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        cursor = (Cursor) data;
        cursor.moveToFirst();

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
