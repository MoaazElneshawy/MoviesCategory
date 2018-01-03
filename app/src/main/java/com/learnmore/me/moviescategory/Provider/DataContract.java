package com.learnmore.me.moviescategory.Provider;

import android.provider.BaseColumns;

/**
 * Created by moaazelneshawy on 02/01/18.
 */

public final class DataContract {
    private DataContract() {
    }

    public final class TableContract implements BaseColumns{
        public static final String TABLE_NAME="MOVIE_TABLE";
        public static final String MOVIE_ID="MovieId";
        public static final String MOVIE_TITLE="MovieTitle";
        public static final String MOVIE_POSTER="MoviePoster";
        public static final String MOVIE_VOTE="MovieVote";
        public static final String MOVIE_OVERVIEW="MovieOverview";
    }
}
