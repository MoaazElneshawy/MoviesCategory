package com.learnmore.me.moviescategory.Provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by moaazelneshawy on 03/01/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int VERSION = 1;

    final String create_query = "CREATE TABLE " + DataContract.TableContract.TABLE_NAME + " ( "
            + DataContract.TableContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + DataContract.TableContract.MOVIE_ID + "INTEGER , "
            + DataContract.TableContract.MOVIE_TITLE + "TEXT , "
            + DataContract.TableContract.MOVIE_POSTER + "TEXT , "
            + DataContract.TableContract.MOVIE_OVERVIEW + "TEXT , "
            + DataContract.TableContract.MOVIE_VOTE + "TEXT ); ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.TableContract.TABLE_NAME);
        onCreate(db);
    }
}
