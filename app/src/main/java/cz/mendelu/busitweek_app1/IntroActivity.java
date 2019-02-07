package cz.mendelu.busitweek_app1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public void start(View view) {
        Intent intent = new Intent(IntroActivity.this, EscapeActivity.class);
        startActivity(intent);
    }

    public void addListenerOnButton() {
        button = (Button) findViewById(R.id.continue_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(IntroActivity.this, EscapeActivity.class);
                startActivity(intent);
            }

        });
    }

}
