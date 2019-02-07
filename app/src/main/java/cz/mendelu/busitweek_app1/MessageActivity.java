package cz.mendelu.busitweek_app1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cz.mendelu.busItWeek.library.ImageSelectPuzzle;
import cz.mendelu.busItWeek.library.StoryLine;

public class MessageActivity extends AppCompatActivity {

    private StoryLine storyLine;
    private TextView title;
    private TextView text;
    private ImageView image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storyLine = StoryLine.open(this, BusITWeekDatabaseHelper.class);

        title = findViewById(R.id.message_screen_title);
        text = findViewById(R.id.message_screen_text);
        image = findViewById(R.id.message_screen_image);

        chooseScreen();
    }

    private void chooseScreen() {
        Bundle b = getIntent().getExtras();
        String task = b.getString("task");

        if (task.equals("1")) {
            initScreen("You spotted a wild cat. Since cats are inferior and need to be destroyed, you have no choice but to chase it. Start running!", "", "");
        } else if (task.equals("2")) {
            long duration = b.getLong("duration");
            if (duration/1000 < 30) {
                initScreen("You caught the cat!", "", "");
            } else {
                initScreen("You lost the cat!", "", "");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        chooseScreen();
    }

    private void initScreen(String title, String text, String image) {
        this.title.setText(title);
        this.text.setText(text);

        if (!image.equals("")) {
            Picasso.get().load(image).into(this.image);
            this.image.setVisibility(View.VISIBLE);
        } else {
            this.image.setVisibility(View.GONE);
        }

    }

    public void continue_app(View view) {
        storyLine.currentTask().finish(true);
        finish();
    }
}
