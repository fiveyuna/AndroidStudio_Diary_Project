package ddwu.mobile.finalproject.ma01_20190978;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.btnOpenAllRoutine:
                intent = new Intent(this, AllRoutinesActivity.class);
                break;
            case R.id.btnAddNewRoutine:
                intent = new Intent(this, InsertRoutineActivity.class);
                break;
            case R.id.btnOpenAllJournal:
                intent = new Intent(this, AllJournalsActivity.class);
                break;
            case R.id.btnAddNewJournal:
                intent = new Intent(this, InsertJournalActivity.class);
                break;
        }

        Log.d(TAG, "Intent: " + intent);
        if (intent != null) startActivity(intent);
    }

}