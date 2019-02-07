package cz.mendelu.busitweek_app1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cz.mendelu.busItWeek.library.StoryLine;
import cz.mendelu.busItWeek.library.qrcode.QRCodeUtil;

public class DominatedActivity extends AppCompatActivity {

    private StoryLine storyLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dominated);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storyLine = StoryLine.open(this, BusITWeekDatabaseHelper.class);
    }

    public void stopScan() {
        storyLine.currentTask().finish(true);
        finish();
    }

}
