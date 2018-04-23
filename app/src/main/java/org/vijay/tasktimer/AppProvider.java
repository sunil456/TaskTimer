package org.vijay.tasktimer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * provider for the TaskTimer app. This is the only knows about {@link AppDatabase}
 */
public class AppProvider extends ContentProvider {
    private static final String TAG = "AppProvider";

    private AppDatabase mOpenHelper;

    private static final UriMatcher mURI_MATCHER = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "org.vijay.tasktimer.provider";

    public static final Uri CONTENT_AYTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int TASKS = 100;
    private static final int TASKS_ID = 101;

    private static final int TIMINGS = 200;
    private static final int TIMINGS_ID = 201;

    /**
     * private static final int TASK_TIMINGS = 300;
     * private static final int TASK_TIMINGS_ID = 301;
     * */
    private static final int TASK_DURATION = 400;
    private static final int TASK_DURATION_ID = 401;


    private static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        //eg. content://org.vijay.tasktimer.provider/Tasks
        matcher.addURI(CONTENT_AUTHORITY , TasksContract.TABLE_NAME , TASKS);
        //eg. content://org.vijay.tasktimer.provider/Tasks/8
        matcher.addURI(CONTENT_AUTHORITY , TasksContract.TABLE_NAME + "/#" , TASKS_ID);

        /**matcher.addURI(CONTENT_AUTHORITY , TimingsContract.TABLE_NAME , TIMINGS);
        matcher.addURI(CONTENT_AUTHORITY , TimingsContract.TABLE_NAME + "/#" , TIMINGS_ID);

        matcher.addURI(CONTENT_AUTHORITY , DurationContract.TABLE_NAME , TASK_DURATION);
        matcher.addURI(CONTENT_AUTHORITY , DurationContract.TABLE_NAME + "/#" , TASK_DURATION_ID);*/

        return matcher;
    }
    /**
     * Implement this to initialize your content provider on startup.
     * This method is called for all registered content providers on the
     * application main thread at application launch time.  It must not perform
     * lengthy operations, or application startup will be delayed.
     * <p>
     * <p>You should defer nontrivial initialization (such as opening,
     * upgrading, and scanning databases) until the content provider is used
     * (via {@link #query}, {@link #insert}, etc).  Deferred initialization
     * keeps application startup fast, avoids unnecessary work if the provider
     * turns out not to be needed, and stops database errors (such as a full
     * disk) from halting application launch.
     * <p>
     * <p>If you use SQLite, {@link SQLiteOpenHelper}
     * is a helpful utility class that makes it easy to manage databases,
     * and will automatically defer opening until first use.  If you do use
     * SQLiteOpenHelper, make sure to avoid calling
     * {@link SQLiteOpenHelper#getReadableDatabase} or
     * {@link SQLiteOpenHelper#getWritableDatabase}
     * from this method.  (Instead, override
     * {@link SQLiteOpenHelper#onOpen} to initialize the
     * database when it is first opened.)
     *
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    /**
     * Implement this to handle query requests from clients.
     * <p>
     * <p>Apps targeting {@link Build.VERSION_CODES#O} or higher should override
     *
     * implementation of this method.
     * <p>
     * <p>This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p>
     * Example client call:<p>
     * <pre>// Request a specific record.
     * Cursor managedCursor = managedQuery(
     * ContentUris.withAppendedId(Contacts.People.CONTENT_URI, 2),
     * projection,    // Which columns to return.
     * null,          // WHERE clause.
     * null,          // WHERE clause value substitution
     * People.NAME + " ASC");   // Sort order.</pre>
     * Example implementation:<p>
     * <pre>// SQLiteQueryBuilder is a helper class that creates the
     * // proper SQL syntax for us.
     * SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
     *
     * // Set the table we're querying.
     * qBuilder.setTables(DATABASE_TABLE_NAME);
     *
     * // If the query ends in a specific record number, we're
     * // being asked for a specific record, so set the
     * // WHERE clause in our query.
     * if((URI_MATCHER.match(uri)) == SPECIFIC_MESSAGE){
     * qBuilder.appendWhere("_id=" + uri.getPathLeafId());
     * }
     *
     * // Make the query.
     * Cursor c = qBuilder.query(mDb,
     * projection,
     * selection,
     * selectionArgs,
     * groupBy,
     * having,
     * sortOrder);
     * c.setNotificationUri(getContext().getContentResolver(), uri);
     * return c;</pre>
     *
     * @param uri           The URI to query. This will be the full URI sent by the client;
     *                      if the client is requesting a specific record, the URI will end in a record number
     *                      that the implementation should parse and add to a WHERE or HAVING clause, specifying
     *                      that _id value.
     * @param projection    The list of columns to put into the cursor. If
     *                      {@code null} all columns are included.
     * @param selection     A selection criteria to apply when filtering rows.
     *                      If {@code null} then all rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder     How the rows in the cursor should be sorted.
     *                      If {@code null} then the provider is free to define the sort order.
     * @return a Cursor or {@code null}.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        Log.d(TAG, "query: Called with Uri: " + uri);
        final int match = mURI_MATCHER.match(uri);
        Log.d(TAG, "query: Match is: " + match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match)
        {
            case TASKS:
                queryBuilder.setTables(TasksContract.TABLE_NAME);
                break;
            case TASKS_ID:
                queryBuilder.setTables(TasksContract.TABLE_NAME);
                long taskId = TasksContract.getTaskId(uri);
                queryBuilder.appendWhere(TasksContract.Columns._ID + " = " + taskId);
                break;

            /**case TIMINGS:
                queryBuilder.setTables(TimingsContract.TABLE_NAME);
                break;
            case TIMINGS_ID:
                queryBuilder.setTables(TimingsContract.TABLE_NAME);
                long timingId = TasksContract.getTimingId(uri);
                queryBuilder.appendWhere(TimingsContract.Columns._ID + " = " + timingId);
                break;

            case TASK_DURATION:
                queryBuilder.setTables(DurationsContract.TABLE_NAME);
                break;
            case TASK_DURATION_ID:
                queryBuilder.setTables(DurationsContract.TABLE_NAME);
                long durationId = TasksContract.getDurationId(uri);
                queryBuilder.appendWhere(DurationsContract.Columns._ID + " = " + durationId);
                break;*/

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        //return queryBuilder.query(db , projection , selection , selectionArgs , null , null , sortOrder);
        Cursor cursor = queryBuilder.query(db , projection , selection , selectionArgs , null , null , sortOrder);
        Log.d(TAG, "query: rows in returned cursor = " + cursor.getCount()); //TODO remove this line

        cursor.setNotificationUri(getContext().getContentResolver() , uri);
        return cursor;
    }

    /**
     * Implement this to handle requests for the MIME type of the data at the
     * given URI.  The returned MIME type should start with
     * <code>vnd.android.cursor.item</code> for a single record,
     * or <code>vnd.android.cursor.dir/</code> for multiple items.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p>
     * <p>Note that there are no permissions needed for an application to
     * access this information; if your content provider requires read and/or
     * write permissions, or is not exported, all applications can still call
     * this method regardless of their access permissions.  This allows them
     * to retrieve the MIME type for a URI when dispatching intents.
     *
     * @param uri the URI to query.
     * @return a MIME type string, or {@code null} if there is no type.
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mURI_MATCHER.match(uri);
        switch (match)
        {
            case TASKS:
                return TasksContract.CONTENT_TYPE;

            case TASKS_ID:
                return TasksContract.CONTENT_ITEM_TYPE;

            /**case TIMINGS:
                return TasksContract.CONTENT_TYPE;

             case TIMINGS_ID:
                return TasksContract.CONTENT_ITEM_TYPE;

             case TASK_DURATION:
                return TasksContract.CONTENT_TYPE;

             case TASK_DURATION_ID:
                return TasksContract.CONTENT_ITEM_TYPE;*/

            default:
                throw new IllegalArgumentException("Unknown Uri :" + uri);

        }
    }

    /**
     * Implement this to handle requests to insert a new row.

     * after inserting.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     *
     * @param uri    The content:// URI of the insertion request. This must not be {@code null}.
     * @param values A set of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     * @return The URI for the newly inserted item.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "Entering insert, called with uri = " + uri);
        final int match = mURI_MATCHER.match(uri);
        Log.d(TAG, "match is : " + match);

        final SQLiteDatabase db;

        Uri returnUri = null;
        long recordId;

        switch (match)
        {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(TasksContract.TABLE_NAME , null , values);
                if (recordId >=0)
                {
                    returnUri = TasksContract.buildTaskUri(recordId);
                }
                else
                {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;

            case TIMINGS:
                /*db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(TimingsContract.Timings.buildTimingUri(recordId));
                if (recordId > 0)
                {
                    returnUri = TimingsContract.Timiings.buildTimingUri(recordId);
                }
                else
                {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;*/

            default:
                throw new IllegalArgumentException("Unknown Uri : " + uri);
        }
        if (recordId >= 0)
        {
            //Something was inserted
            Log.d(TAG, "insert: Setting notifyChange with " + uri);
            getContext().getContentResolver().notifyChange(uri , null);
        }
        else 
        {
            Log.d(TAG, "insert: Nothing inserted");
        }
        Log.d(TAG, "Exiting insert, returning" + returnUri);
        return returnUri;
    }

    /**
     * Implement this to handle requests to delete one or more rows.
     * The implementation should apply the selection clause when performing
     * deletion, allowing the operation to affect multiple rows in a directory.
     *
     * after deleting.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p>
     * <p>The implementation is responsible for parsing out a row ID at the end
     * of the URI, if a specific row is being deleted. That is, the client would
     * pass in <code>content://contacts/people/22</code> and the implementation is
     * responsible for parsing the record number (22) when creating a SQL statement.
     *
     * @param uri           The full URI to query, including a row ID (if a specific record is requested).
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs
     * @return The number of rows affected.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        Log.d(TAG, "delete: ");
        final int match = mURI_MATCHER.match(uri);
        Log.d(TAG, "match is : " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch (match)
        {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(TasksContract.TABLE_NAME , selection , selectionArgs);
                break;

            case TASKS_ID:
                db = mOpenHelper.getWritableDatabase();
                long taskId = TasksContract.getTaskId(uri);
                selectionCriteria = TasksContract.Columns._ID + " = " + taskId;

                if ((selection != null) && (selection.length()>0))
                {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.delete(TasksContract.TABLE_NAME , selectionCriteria , selectionArgs);
                break;

            /*case TIMINGS:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(TimingsContract.TABLE_NAME , selection , selectionArgs);
                break;

            case TIMINGS_ID:
                db = mOpenHelper.getWritableDatabase();
                long timingsId = TimingsContract.getTaskId(uri);
                selectionCriteria = TimingsContract.Columns._ID + " = " + timingsId;

                if ((selection != null) && (selection.length()>0))
                {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.delete(TimingsContract.TABLE_NAME , selectionCriteria , selectionArgs);
                break;*/

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        Log.d(TAG, "Exiting update , Returning : " + count);
        return count;
    }

    /**
     * Implement this to handle requests to update one or more rows.
     * The implementation should update all rows matching the selection
     * to set the columns according to the provided values map.
     *
     * after updating.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     *
     * @param uri           The URI to query. This can potentially have a record ID if this
     *                      is an update request for a specific record.
     * @param values        A set of column_name/value pairs to update in the database.
     *                      This must not be {@code null}.
     * @param selection     An optional filter to match rows to update.
     * @param selectionArgs
     * @return the number of rows affected.
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        Log.d(TAG, "update Called with Uri : " + uri);
        final int match = mURI_MATCHER.match(uri);
        Log.d(TAG, "match is : " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch (match)
        {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(TasksContract.TABLE_NAME , values , selection , selectionArgs);
                break;

            case TASKS_ID:
                db = mOpenHelper.getWritableDatabase();
                long taskId = TasksContract.getTaskId(uri);
                selectionCriteria = TasksContract.Columns._ID + " = " + taskId;

                if ((selection != null) && (selection.length()>0))
                {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.update(TasksContract.TABLE_NAME , values , selectionCriteria , selectionArgs);
                break;

            /*case TIMINGS:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(TimingsContract.TABLE_NAME , values , selection , selectionArgs);
                break;

            case TIMINGS_ID:
                db = mOpenHelper.getWritableDatabase();
                long timingsId = TimingsContract.getTaskId(uri);
                selectionCriteria = TimingsContract.Columns._ID + " = " + timingsId;

                if ((selection != null) && (selection.length()>0))
                {
                    selectionCriteria += " AND (" + selection + ")";
                }

                count = db.update(TimingsContract.TABLE_NAME , values , selectionCriteria , selectionArgs);
                break;*/

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        Log.d(TAG, "Exiting update , Returning : " + count);
        return count;
    }
}
