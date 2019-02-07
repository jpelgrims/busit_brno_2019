package cz.mendelu.busitweek_app1;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MeasureActivity extends AppCompatActivity {
    ArrayList<Integer> list = new ArrayList<>();
    //nodig voor te meten
    TextView mStatusView;
    MediaRecorder mRecorder;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    final Runnable updater = new Runnable(){

        public void run(){
            updateTv();
        };
    };
    final Handler mHandler = new Handler();


    //poging timer
    TextView timerView ;
    int i = 10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        timerView = (TextView) findViewById(R.id.timer);
        onCreateMeasurements();
        timer();
    }

    public void timer() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000); // every second
                timerView.setText(Integer.toString(i));
                i -= 1;
                if (i<0) {
                    System.out.println("stop");
                    handler.removeCallbacksAndMessages(null);
                    int max = list.get(0);
                    for (Integer x : list) {
                        if (x>max){
                            max = x;
                        }
                    }
                    if (max > 83) {
                        Intent intent = new Intent(MeasureActivity.this, ReunionActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MeasureActivity.this, BarkFailActivity.class);
                        startActivity(intent);
                    }

                }
            }
        }, 0);
    }

    public void onCreateMeasurements() {
        mStatusView = (TextView) findViewById(R.id.status);

        if (runner == null)
        {
            runner = new Thread(){
                public void run()
                {
                    while (runner != null)
                    {
                        try
                        {
                            Thread.sleep(500);
                            Log.i("Noise", "Tock");
                        } catch (InterruptedException e) { };
                        mHandler.post(updater);
                    }
                }
            };
            runner.start();
            Log.d("Noise", "start runner()");
        }
    }

    // voor te meten
    public void onResume() {
        super.onResume();
        startRecorder();
    }

    public void onPause() {
        super.onPause();
        stopRecorder();
    }

    public void startRecorder(){
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            }catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " + android.util.Log.getStackTraceString(ioe));

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }
            try {
                mRecorder.start();
            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }

            //mEMA = 0.0;
        }

    }
    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void updateTv(){
        int result = (int) soundDb(1);
        if (result < 100)
            list.add(result);
        mStatusView.setText(Integer.toString(result - 5 )+ " dB");
    }
    public double soundDb(double ampl){
        return 20 * Math.log10(getAmplitudeEMA() / ampl);
    }
    public double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
        else
            return 0;

    }
    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }


}