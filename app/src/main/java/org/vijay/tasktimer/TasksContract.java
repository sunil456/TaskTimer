package org.vijay.tasktimer;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static org.vijay.tasktimer.AppProvider.CONTENT_AUTHORITY;
import static org.vijay.tasktimer.AppProvider.CONTENT_AYTHORITY_URI;

public class TasksContract
{
    /** Table Name */
    static final String TABLE_NAME = "Tasks";

    /** Tasks Field */
    public static class Columns
    {
        public static final String _ID = BaseColumns._ID;
        public static final String TASKS_NAME = "Name";
        public static final String TASKS_DESCRIPTION = "Description";
        public static final String TASKS_SORTORDER = "SortOrder";

        /** Private Constructor for preventing to create Instantiation*/
        private Columns()
        {

        }
    }

    /**
     * The URI to access the Tasks Table
     */
    public static final Uri CONTENT_UTI = Uri.withAppendedPath(CONTENT_AYTHORITY_URI , TABLE_NAME);
    static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;

    static Uri buildTaskUri(long taskId)
    {
        return ContentUris.withAppendedId(CONTENT_UTI , taskId);
    }
    static long getTaskId(Uri uri)
    {
        return ContentUris.parseId(uri);
    }
}
