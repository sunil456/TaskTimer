package org.vijay.tasktimer;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.TaskViewHolder> {

    private static final String TAG = "CursorRecyclerViewAdapt";
    private Cursor mCursor;


    public CursorRecyclerViewAdapter(Cursor cursor) {
        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor Called");
        mCursor = cursor;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new View requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_items , parent , false);

        return new TaskViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position)
    {
        Log.d(TAG, "onBindViewHolder: START");

        if (mCursor == null || (mCursor.getCount()) == 0)
        {
            Log.d(TAG, "onBindViewHolder: Providing Instructions");
            holder.name.setText(R.string.instructions_heading);
            holder.description.setText(R.string.instructions);

            holder.editButotn.setVisibility(View.GONE);
            holder.editButotn.setVisibility(View.GONE);
        }
        else
        {
            if (!mCursor.moveToPosition(position))
            {
                throw new IllegalStateException("Could't move the cursor to position " + position);
            }
            holder.name.setText(mCursor.getString(mCursor.getColumnIndex(TasksContract.Columns.TASKS_NAME)));
            holder.description.setText(mCursor.getString(mCursor.getColumnIndex(TasksContract.Columns.TASKS_DESCRIPTION)));
            holder.editButotn.setVisibility(View.VISIBLE); // TODO add onClick listener
            holder.deleteButton.setVisibility(View.VISIBLE); // TODO add onClick listener
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount()
    {
        Log.d(TAG, "getItemCount: START");
        if (mCursor == null || (mCursor.getCount() == 0))
        {
            return 1; //fib , because we populate a single ViewHolder with instructions.
        }
        else
        {
           return mCursor.getCount();
        }
    }

    /**
     * Swap in a new Cursor , returning the old Cursor.
     * The returned old Cursor is <em>not</em> closed
     *
     * @param newCursor The new Cursor to be used
     * @return Returns the Previously set cursor , or NULL if there wasn't one
     * If the given new Cursor is the same instance as the previously set
     * Cursor , null is also returned.
     */
    Cursor swapCursor(Cursor newCursor)
    {
        if (newCursor == mCursor)
        {
            return null;
        }
        final Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null)
        {
            //Notify the observer about the new Cursor
            notifyDataSetChanged();
        }
        else
        {
            //notify the observer about the lack of the Data set
            notifyItemRangeRemoved(0 , getItemCount());
        }
        return oldCursor;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "TaskViewHolder";

        TextView name = null;
        TextView description = null;
        ImageButton editButotn = null;
        ImageButton deleteButton = null;

        public TaskViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "TaskViewHolder: START");

            this.name = (TextView) itemView.findViewById(R.id.tli_name);
            this.description = (TextView) itemView.findViewById(R.id.tli_description);
            this.editButotn = (ImageButton) itemView.findViewById(R.id.tli_edit);
            this.deleteButton = (ImageButton) itemView.findViewById(R.id.tli_delete);
        }
    }
}
