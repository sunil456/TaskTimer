package org.vijay.tasktimer;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    //whether ot not this activity is in 2-pane mode
    //i.e. running in landscape on a tablet
    private boolean mTwoPane = false;

    private static final String ADD_EDIT_FRAGMENT = "AddEditFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.menumain_addTask:
                taskEditRequest(null);
                break;
            case R.id.menumain_showDurations:
                break;
            case R.id.menumain_settings:
                break;
            case R.id.menumain_showAbout:
                break;
            case R.id.menumain_generate:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void taskEditRequest(Task task)
    {
        Log.d(TAG, "taskEditRequest: START");
        if (mTwoPane)
        {
            Log.d(TAG, "taskEditRequest: int two-pane mode (tablet)");
        }
        else
        {
            Log.d(TAG, "taskEditRequest: in single-pane mode (phone)");

            //in single-pane mode , start the detail activity for selected item Id.
            Intent detailIntent = new Intent(this , AddEditActivity.class);

            if (task != null)
            {
                //Editing a Task
                detailIntent.putExtra(Task.class.getSimpleName() , task);
                startActivity(detailIntent);
            }
            else
            {
                //Adding a New Task
                startActivity(detailIntent);
            }
        }
    }
}
