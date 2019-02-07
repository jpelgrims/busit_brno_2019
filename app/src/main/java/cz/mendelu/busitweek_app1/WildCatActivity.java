package cz.mendelu.busitweek_app1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cz.mendelu.busItWeek.library.StoryLine;

public class WildCatActivity extends AppCompatActivity {

    private StoryLine storyLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wild_cat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storyLine = StoryLine.open(this, BusITWeekDatabaseHelper.class);
    }

    public void continue_app(View view) {
        storyLine.currentTask().finish(true);
        finish();
    }
}
