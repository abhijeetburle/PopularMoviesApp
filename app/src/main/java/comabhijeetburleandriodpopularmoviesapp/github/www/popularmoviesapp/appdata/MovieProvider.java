package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata.appcontracts.MovieContract;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata.appcontracts.MovieContract.FavouriteMovieEntry;

/**
 * Created by abhijeet.burle on 2016/02/01.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int MOVIE_FAV_LIST = 100;
    static final int MOVIE_FAV_DETAILS = 101;
    static final int MOVIE_FAV_CHECK = 102;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_FAV_LIST:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavouriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        FavouriteMovieEntry.COLUMN_REG_DATE + " DESC"
                );
                break;
            }
            case MOVIE_FAV_CHECK:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavouriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return  retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_FAV_LIST:
                return FavouriteMovieEntry.CONTENT_TYPE;
            case MOVIE_FAV_DETAILS:
                return FavouriteMovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE_FAV_CHECK:
                return FavouriteMovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE_FAV_DETAILS: {
                long _id = db.insert(FavouriteMovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FavouriteMovieEntry.buildFavoriteMovieUri(
                            values.get(FavouriteMovieEntry.COLUMN_THEMOVIEDB_ID).toString());
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIE_FAV_LIST:
            case MOVIE_FAV_DETAILS: {
                rowsDeleted = db.delete(
                        FavouriteMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE_FAV_DETAILS:
                rowsUpdated = db.update(FavouriteMovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_FAVOURITE + "/list", MOVIE_FAV_LIST);
        matcher.addURI(authority, MovieContract.PATH_FAVOURITE + "/list/#", MOVIE_FAV_DETAILS);
        matcher.addURI(authority, MovieContract.PATH_FAVOURITE + "/list/#/favourite_status", MOVIE_FAV_CHECK);
        return matcher;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
