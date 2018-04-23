package org.vijay.tasktimer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditActivityFragment extends Fragment {

    private static final String TAG = "AddEditActivityFragment";

    public enum FragmentEditMode { EDIT , ADD}
    private FragmentEditMode mMode;

    private AppCompatEditText mNameTextView;
    private AppCompatEditText mDescriptionTextView;
    private AppCompatEditText mSortOrderTextView;

    private AppCompatButton mSaveButton;

    public AddEditActivityFragment() 
    {
        Log.d(TAG, "AddEditActivityFragment: Constructor Called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: STARTS");

        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);

        mNameTextView = (AppCompatEditText) view.findViewById(R.id.addedit_name);
        mDescriptionTextView = (AppCompatEditText) view.findViewById(R.id.addedit_description);
        mSortOrderTextView = (AppCompatEditText) view.findViewById(R.id.addedit_sortorder);
        mSaveButton = (AppCompatButton) view.findViewById(R.id.addedit_save);

        //The line we'll change latter
        Bundle arguments = getActivity().getIntent().getExtras();

        final Task task;

        if (arguments != null)
        {
            Log.d(TAG, "onCreateView: Retrieving Task Details.");

            task = (Task) arguments.getSerializable(Task.class.getSimpleName());

            if (task != null)
            {
                Log.d(TAG, "onCreateView: Task Details Found Editing.....");

                mNameTextView.setText(task.getName());
                mDescriptionTextView.setText(task.getDescription());
                mSortOrderTextView.setText(Integer.parseInt(String.valueOf(task.getSortOrder())));

                mMode = FragmentEditMode.EDIT;
            }
            else
            {
                //No Task , so we must be Adding NEW TASK , and not editing one
                mMode = FragmentEditMode.ADD;
            }
        }
        else
        {
            task = null;
            Log.d(TAG, "onCreateView: No Argument , Adding new Record");
            mMode = FragmentEditMode.ADD;
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update the database  if at least one field has change.
                // - There's no need to hit the database unless this has happened.

                int so; //to save repeated conversions to int
                if (mSortOrderTextView.length() > 0)
                {
                    so = Integer.parseInt(mSortOrderTextView.getText().toString());
                }
                else
                {
                    so = 0;
                }

                ContentResolver contentResolver = getActivity().getContentResolver();
                ContentValues contentValues = new ContentValues();

                switch (mMode)
                {
                    case EDIT:
                        //First Check whether Data is same or Not
                        if (!mNameTextView.getText().toString().equals(task.getName()))
                        {
                            contentValues.put(TasksContract.Columns.TASKS_NAME , mNameTextView.getText().toString());
                        }
                        if (!mDescriptionTextView.getText().toString().equals(task.getDescription()))
                        {
                            contentValues.put(TasksContract.Columns.TASKS_DESCRIPTION , mDescriptionTextView.getText().toString());
                        }
                        if (so != task.getSortOrder())
                        {
                            contentValues.put(TasksContract.Columns.TASKS_SORTORDER , so);
                        }

                        if (contentValues.size() != 0)
                        {
                            Log.d(TAG, "onClick: Updating Task");
                            contentResolver.update(TasksContract.buildTaskUri(task.getId()) , contentValues , null , null);
                        }
                        break;

                    case ADD:
                        if (mNameTextView.length()>0)
                        {
                            Log.d(TAG, "onClick: Adding new Task");
                            contentValues.put(TasksContract.Columns.TASKS_NAME , mNameTextView.getText().toString());
                            contentValues.put(TasksContract.Columns.TASKS_DESCRIPTION , mDescriptionTextView.getText().toString());
                            contentValues.put(TasksContract.Columns.TASKS_SORTORDER , so);

                            contentResolver.insert(TasksContract.CONTENT_UTI , contentValues);
                        }
                        break;
                }
                Log.d(TAG, "onClick: Done Editing");
            }
        });

        Log.d(TAG, "onCreateView: Exiting....");


        return view;
    }
}
