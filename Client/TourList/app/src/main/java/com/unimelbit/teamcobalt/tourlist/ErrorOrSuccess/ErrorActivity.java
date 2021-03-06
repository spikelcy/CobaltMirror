package com.unimelbit.teamcobalt.tourlist.ErrorOrSuccess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.unimelbit.teamcobalt.tourlist.BaseActivity;
import com.unimelbit.teamcobalt.tourlist.R;

/**
 * Activity used to handle errors, proper use should prevent app crashes and give the user feedback
 * concerning the cause of the error
 */
public class ErrorActivity extends AppCompatActivity {

    private static String TITLE = "Error";
    private static String INTENT_MSG = "com.unimelbit.teamcobalt.tourlist.Error.ErrorActivity.INTENT_MSG";
    private static String INTENT_STACKTRACE = "com.unimelbit.teamcobalt.tourlist.Error.ErrorActivity.INTENT_STACKTRACE";
    private static String INTENT_EMPTY_FIELD_FLAG = "com.unimelbit.teamcobalt.tourlist.Error.ErrorActivity.INTENT_EMPTY_FIELD";

    private String msg;
    private String stackTrace;

    /**
     * Simple public static polymorphic method to start to the error activity
     */
    public static void newError(Activity from, Exception e, String msg) {

        e.printStackTrace();
        Intent i = new Intent(from, ErrorActivity.class);
        i.putExtra(INTENT_MSG, msg);
        i.putExtra(INTENT_STACKTRACE, e.toString());
        from.startActivity(i);
        from.finish();
    }

    /**
     * Simple public static polymorphic method to start to the error activity
     */
    public static void newError(Activity from, Exception e) {
        e.printStackTrace();
        Intent i = new Intent(from, ErrorActivity.class);
        i.putExtra(INTENT_MSG, INTENT_EMPTY_FIELD_FLAG);
        i.putExtra(INTENT_STACKTRACE, e.toString());
        from.startActivity(i);
        from.finish();
    }

    /**
     * Simple public static polymorphic method to start to the error activity
     */
    public static void newError(Activity from, String msg) {
        Intent i = new Intent(from, ErrorActivity.class);
        i.putExtra(INTENT_MSG, msg);
        i.putExtra(INTENT_STACKTRACE, INTENT_EMPTY_FIELD_FLAG);
        from.startActivity(i);
        from.finish();
    }

    /**
     * Simple public static polymorphic method to start to the error activity
     */
    public static void newError(Activity from) {
        Intent i = new Intent(from, ErrorActivity.class);
        i.putExtra(INTENT_MSG, INTENT_EMPTY_FIELD_FLAG);
        i.putExtra(INTENT_STACKTRACE, INTENT_EMPTY_FIELD_FLAG);
        from.startActivity(i);
        from.finish();
    }

    /**
     * Initialise the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        setTitle(TITLE);

        msg = getIntent().getStringExtra(INTENT_MSG);
        stackTrace = getIntent().getStringExtra(INTENT_STACKTRACE);
        initUI();
    }

    /**
     * Initialise the UI
     */
    private void initUI() {

        // hide action bar, prevent keybaord from showing
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (msg.equals(INTENT_EMPTY_FIELD_FLAG)) {
            findViewById(R.id.error_message_container).setVisibility(View.GONE);
        } else {
            ((EditText) findViewById(R.id.error_message_out)).setText(msg);
        }

        if (stackTrace.equals(INTENT_EMPTY_FIELD_FLAG)) {
            findViewById(R.id.error_stacktrace_container).setVisibility(View.GONE);
        } else {
            ((EditText) findViewById(R.id.error_stacktrace_out)).setText(stackTrace);
        }
    }

    /**
     * Go back to the baseactivity on back button press
     */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, BaseActivity.class);
        startActivity(i);
        finish();
    }
}
