package comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata.appcontracts.MovieContract;
import comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp.appdata.appcontracts.MovieContract.FavouriteMovieEntry;

/**
 * Created by abhijeet.burle on 2016/02/02.
 */
public class TestMovieProvider  extends AndroidTestCase {

    public static final String LOG_TAG = TestMovieProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                FavouriteMovieEntry.buildFavoriteListUri(),
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                FavouriteMovieEntry.buildFavoriteListUri(),
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from FavouriteMovie table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());

        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: FavouriteMovie not registered at " + mContext.getPackageName(),
                    false);
        }
    }


    public void testGetType() {
        // content://comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp/favourite/list
        String type = mContext.getContentResolver().getType(FavouriteMovieEntry.buildFavoriteListUri());
        Log.i(LOG_TAG, "["+FavouriteMovieEntry.buildFavoriteListUri()+"]["+type+"]");
        assertEquals("Error: the FavouriteMovieEntry CONTENT_URI should " +
                        "return FavouriteMovieEntry.CONTENT_TYPE",
                FavouriteMovieEntry.CONTENT_TYPE, type);

        // content://comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp/favourite/list/140607
        type = mContext.getContentResolver().getType(
                FavouriteMovieEntry.buildFavoriteMovieUri("140607"));
        Log.i(LOG_TAG, "[" + FavouriteMovieEntry.buildFavoriteMovieUri("140607") + "][" + type + "]");
        assertEquals("Error: the FavouriteMovieEntry CONTENT_URI should " +
                        "return FavouriteMovieEntry.CONTENT_ITEM_TYPE",
                FavouriteMovieEntry.CONTENT_ITEM_TYPE, type);

        // content://comabhijeetburleandriodpopularmoviesapp.github.www.popularmoviesapp/favourite/list/140607/favourite_status
        type = mContext.getContentResolver().getType(
                FavouriteMovieEntry.buildCheckIsFavourite("140607"));
        Log.i(LOG_TAG, "[" + FavouriteMovieEntry.buildCheckIsFavourite("140607") + "][" + type + "]");
        // vnd.android.cursor.item/com.example.android.sunshine.app/weather/1419120000
        assertEquals("Error: the FavouriteMovieEntry CONTENT_URI should " +
                        "return FavouriteMovieEntry.CONTENT_ITEM_TYPE",
                FavouriteMovieEntry.CONTENT_ITEM_TYPE, type);

    }

    public void testBasicQuery() {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovieStarWarsValues();
        long movieId = TestUtilities.insertMoveiRecord(mContext, testValues);
        assertTrue("Unable to Insert FavouriteMovieEntry into the Database", movieId != -1);

        db.close();

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                FavouriteMovieEntry.buildFavoriteListUri(),
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicQuery", movieCursor, testValues);
        movieCursor.close();
    }

    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createMovieStarWarsValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavouriteMovieEntry.buildFavoriteListUri(), true, tco);
        Uri movieUri = mContext.getContentResolver()
                .insert(FavouriteMovieEntry.buildFavoriteMovieUri(
                                testValues.get(FavouriteMovieEntry.COLUMN_THEMOVIEDB_ID)
                                        .toString()
                        ),
                        testValues);

        assertTrue(movieUri != null);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                FavouriteMovieEntry.buildFavoriteListUri(),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating FavouriteMovieEntry.",
                cursor, testValues);
        cursor.close();

        testValues = TestUtilities.createMovieAvengersValues();

        movieUri = mContext.getContentResolver()
                .insert(FavouriteMovieEntry.buildFavoriteMovieUri(
                                testValues.get(FavouriteMovieEntry.COLUMN_THEMOVIEDB_ID)
                                        .toString()
                        ),
                        testValues);

        assertTrue(movieUri != null);

        // Get the joined Weather data for a specific date
        cursor = mContext.getContentResolver().query(
                FavouriteMovieEntry.buildFavoriteListUri(),
                null,
                null,
                null,
                null
        );
        // CHECK SORT : LIFO
        cursor.moveToFirst();
        TestUtilities.validateCursor("testInsertReadProvider. Error validating Sort FavouriteMovieEntry.",
                cursor, testValues);
        cursor.close();

        /*
        cursor.moveToFirst();
        for ( int i = 0; i < cursor.getCount(); i++, cursor.moveToNext() ) {
            Log.w(LOG_TAG, "["+cursor.getString(0)+"]["+cursor.getString(1)+"]["+cursor.getString(2)+"]");
        }
        cursor.close();
        */


    }

    public void testSelectRecord(){
        testInsertReadProvider();
        String movieId = TestUtilities.createMovieStarWarsValues()
                .get(FavouriteMovieEntry.COLUMN_THEMOVIEDB_ID).toString();
        Cursor cursor = mContext.getContentResolver().query(
                FavouriteMovieEntry.buildCheckIsFavourite(movieId),
                null, // leaving "columns" null just returns all the columns.
                FavouriteMovieEntry.COLUMN_THEMOVIEDB_ID + "= ?", // cols for "where" clause
                new String[]{movieId}, // values for "where" clause
                null  // sort order
        );
        if(cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            Log.i(LOG_TAG, "testSelectRecord: [" + cursor.getString(0) + "][" + cursor.getString(1) + "][" + cursor.getString(2) + "]");
        }
        assertTrue(cursor!=null && cursor.getCount()>0);

    }
    public void testDeleteRecord(){
        String movieId = TestUtilities.createMovieStarWarsValues()
                .get(FavouriteMovieEntry.COLUMN_THEMOVIEDB_ID).toString();

        int count = mContext.getContentResolver()
                .delete(FavouriteMovieEntry.buildFavoriteMovieUri(movieId)
                        , FavouriteMovieEntry.COLUMN_THEMOVIEDB_ID + "= ?"
                        , new String[]{movieId});

        Log.i(LOG_TAG, "testDeleteRecord: info "+ count);

        Cursor cursor = mContext.getContentResolver().query(
                FavouriteMovieEntry.buildCheckIsFavourite(movieId),
                null, // leaving "columns" null just returns all the columns.
                FavouriteMovieEntry.COLUMN_THEMOVIEDB_ID + "= ?", // cols for "where" clause
                new String[]{movieId}, // values for "where" clause
                null  // sort order
        );
        if(cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            Log.i(LOG_TAG, "testDeleteRecord: [" + cursor.getString(0) + "][" + cursor.getString(1) + "][" + cursor.getString(2) + "]");
        }
        assertFalse(cursor!=null && cursor.getCount()>0);


    }
    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our delete.
        TestUtilities.TestContentObserver objObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavouriteMovieEntry.CONTENT_URI, true, objObserver);

        deleteAllRecordsFromProvider();
        objObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(objObserver);
    }
}
