package com.learnmore.me.moviescategory.Provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by moaazelneshawy on 03/01/18.
 */

public final class DataContract {

    public static final String AUTHORITY = "com.learnmore.me.moviescategory.Provider";
    public static final Uri BASE_CONTENT = Uri.parse("content://" + AUTHORITY);

    public static final class TableContract implements BaseColumns {
        public static final String TABLE_NAME = "MOVIE_TABLE";
        public static final String MOVIE_ID = "MovieId";
        public static final String MOVIE_TITLE = "MovieTitle";
        public static final String MOVIE_POSTER = "MoviePoster";
        public static final String MOVIE_VOTE = "MovieVote";
        public static final String MOVIE_OVERVIEW = "MovieOverview";

        public static final Uri CONTENT_URI_TABLE = BASE_CONTENT.buildUpon()
                .appendPath(TABLE_NAME).build();

    }
}
