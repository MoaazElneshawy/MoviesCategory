package com.learnmore.me.moviescategory.Provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by moaazelneshawy on 03/01/18.
 */

public class DatabaseProvider extends ContentProvider {

    public static final int ALL_MOVIES = 100;
    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int iMatcher = sUriMatcher.match(uri);
        Cursor cursor;
        switch (iMatcher) {
            case ALL_MOVIES:
                cursor = db.query(DataContract.TableContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
                default:
                    throw new UnsupportedOperationException("Unsupported operation");
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int iMatcher = sUriMatcher.match(uri);
        Uri returnUri;
        switch (iMatcher) {
            case ALL_MOVIES:
                long id = db.insert(DataContract.TableContract.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DataContract.TableContract.CONTENT_URI_TABLE, id);
                } else {
                    throw new android.database.SQLException("Failed to insert Movie in the database");
                }
                System.out.println("id , " + id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(DataContract.AUTHORITY, DataContract.TableContract.TABLE_NAME, ALL_MOVIES);
        return matcher;
    }

}
