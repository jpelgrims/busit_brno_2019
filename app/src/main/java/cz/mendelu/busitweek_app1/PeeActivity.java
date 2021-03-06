package cz.mendelu.busitweek_app1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cz.mendelu.busItWeek.library.qrcode.QRCodeUtil;

public class PeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pee);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void startScan(View view) {
        QRCodeUtil.startQRScan(PeeActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String qrCode = QRCodeUtil.onScanResult(this, requestCode, resultCode, data);
        if (qrCode != null) {
            Intent intent = new Intent(PeeActivity.this, DominatedActivity.class);
            startActivity(intent);
            finish();
        }
    }



}
