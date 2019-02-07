package cz.mendelu.busitweek_app1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.security.Permission;
import java.util.List;

import cz.mendelu.busItWeek.library.BeaconTask;
import cz.mendelu.busItWeek.library.ChoicePuzzle;
import cz.mendelu.busItWeek.library.CodeTask;
import cz.mendelu.busItWeek.library.GPSTask;
import cz.mendelu.busItWeek.library.ImageSelectPuzzle;
import cz.mendelu.busItWeek.library.Puzzle;
import cz.mendelu.busItWeek.library.SimplePuzzle;
import cz.mendelu.busItWeek.library.StoryLine;
import cz.mendelu.busItWeek.library.Task;
import cz.mendelu.busItWeek.library.beacons.BeaconDefinition;
import cz.mendelu.busItWeek.library.beacons.BeaconUtil;
import cz.mendelu.busItWeek.library.map.MapUtil;
import cz.mendelu.busItWeek.library.qrcode.QRCodeUtil;
import cz.mendelu.busItWeek.library.timer.TimerUtil;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, LocationEngineListener {

    private MapView mapView;
    private MapboxMap mapBoxMap;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationComponent locationComponent;

    private StoryLine storyLine;
    private Task currentTask;

    private Marker currentMarker;

    private BeaconUtil beaconUtil;

    private ImageButton qrCodeButton;
    private CardView beaconScanningCard;
    private long startTime;

    private boolean beaconRun = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, "pk.eyJ1IjoianBlbGdyaW1zIiwiYSI6ImNqcnV5N2Q0ZzE4OWMzeXFzNW93MW9jbDYifQ.ifQGQUeIMxhU3gxmeZGhbg");

        setContentView(R.layout.map_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mapView = findViewById(R.id.map_view);
        qrCodeButton = findViewById(R.id.qr_button);
        beaconScanningCard = findViewById(R.id.beacon_scanning);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        beaconUtil = new BeaconUtil(this);
        storyLine = StoryLine.open(this, BusITWeekDatabaseHelper.class);
        currentTask = storyLine.currentTask();

        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeUtil.startQRScan(MapActivity.this);
            }
        });

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapBoxMap = mapboxMap;

        mapBoxMap.getUiSettings().setAllGesturesEnabled(true);
        initializeListeners();
        updateMarkers();

        LatLng pos = currentMarker.getPosition();
        zoomToLocation(pos);
    }

    private void zoomToLocation(LatLng location){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 17);

        if (mapBoxMap != null) {
            mapBoxMap.moveCamera(cameraUpdate);
        }
    }

    private void initializeListeners() {
        if (currentTask != null) {
            if (currentTask instanceof GPSTask) {
                initializeLocationComponent();
                initializeLocationEngine();
                qrCodeButton.setVisibility(View.GONE);
                beaconScanningCard.setVisibility(View.GONE);

            }

            if (currentTask instanceof CodeTask) {
                qrCodeButton.setVisibility(View.VISIBLE);
                beaconScanningCard.setVisibility(View.GONE);
            }

            if (currentTask instanceof BeaconTask) {
                BeaconDefinition definition = new BeaconDefinition((BeaconTask) currentTask) {

                    // beacon detected
                    @Override
                    public void execute() {
                        // start timer if task == 1 and don't run puzzle
                        // show activity with message and then show mapActivity again
                        beaconUtil.stopRanging();

                        if (!beaconRun && currentTask.getName().equals("1")) {
                            // start timer
                            startTime = System.currentTimeMillis();

                            // show activity
                            Intent intent = new Intent(MapActivity.this, WildCatActivity.class);
                            Bundle b = new Bundle();
                            b.putString("task", "1");
                            intent.putExtras(b);
                            startActivity(intent);


                        } else {
                            runPuzzleActivity(currentTask.getPuzzle());
                        }

                        beaconRun = true;


                    }
                };

                beaconUtil.addBeacon(definition);
                beaconUtil.startRanging();

                qrCodeButton.setVisibility(View.GONE);
                beaconScanningCard.setVisibility(View.VISIBLE);

            }
        }
    }

    private void removeListeners() {
        qrCodeButton.setVisibility(View.GONE);

        if (beaconUtil.isRanging()) {
            beaconUtil.stopRanging();
            beaconUtil.clearBeacons();
            beaconRun = false;
        }

        if (mapBoxMap != null && locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
    }

    private void updateMarkers() {
        if (currentTask != null && mapBoxMap != null) {
            if (currentMarker != null) {
                mapBoxMap.removeMarker(currentMarker);
            }

            currentMarker = mapBoxMap.addMarker(createTaskMarker(this, currentTask));
        }
    }

    private void runPuzzleActivity(Puzzle puzzle) {
        if (puzzle instanceof SimplePuzzle) {
            startActivity(new Intent(this, SimplePuzzleActivity.class));
        }

        if (puzzle instanceof ChoicePuzzle) {
            startActivity(new Intent(this, ChoicePuzzleActivity.class));
        }

        if (puzzle instanceof ImageSelectPuzzle) {
            startActivity(new Intent(this, ImagePuzzleActivity.class));
        }
    }

    private MarkerOptions createTaskMarker(Context context, Task task) {
        int color = R.color.colorGPS;

        if (task instanceof BeaconTask) {
            color = R.color.colorBeacon;
        } else if (task instanceof CodeTask) {
            color = R.color.colorQR;
        }

        return new MarkerOptions().position(new LatLng(task.getLatitude(), task.getLongitude()))
                .icon(MapUtil.createColoredCircleMarker(context, task.getName(), color));

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        currentTask = storyLine.currentTask();
        if (currentTask == null) {
            // no more tasks
            startActivity(new Intent(this, FinishActivity.class));
            finish();
        } else {
            initializeListeners();
            updateMarkers();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        removeListeners();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String qrCode = QRCodeUtil.onScanResult(this, requestCode, resultCode, data);
        if (qrCode != null) {
            /*if (qrCode.equals(((CodeTask) currentTask).getQR())) {
                runPuzzleActivity(currentTask.getPuzzle());
            }*/
            if (currentTask.getName().equals("5")) {
                Intent intent = new Intent(MapActivity.this, DominatedActivity.class);
                startActivity(intent);
            }
        }
    }

    private void initializeLocationComponent() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            if (mapBoxMap != null) {
                locationComponent = mapBoxMap.getLocationComponent();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationComponent.activateLocationComponent(this);
                locationComponent.setLocationComponentEnabled(true);
            }
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationEngine() {
        if (mapBoxMap != null && PermissionsManager.areLocationPermissionsGranted(this)) {
            locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
            locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
            locationEngine.setInterval(1000);
            locationEngine.requestLocationUpdates();
            locationEngine.addLocationEngineListener(this);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {

    }

    @Override
    public void onConnected() {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (currentTask != null && currentTask instanceof GPSTask) {
            double radius = ((GPSTask) currentTask).getRadius();
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng taskLocation = new LatLng(currentTask.getLatitude(), currentTask.getLongitude());

            if (userLocation.distanceTo(taskLocation) < radius) {
                if (currentTask.getName().equals("2")) {
                    // Calculate time passed
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;

                    if (duration/1000 < 30) {
                        Intent intent = new Intent(MapActivity.this, CaughtCatActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MapActivity.this, LostCatActivity.class);
                        startActivity(intent);
                    }
                } else {
                    runPuzzleActivity(currentTask.getPuzzle());
                }
            }
        } else if (currentTask != null && currentTask instanceof CodeTask) {
            if (currentTask.getName().equals("5")) {
                Intent intent = new Intent(MapActivity.this, PeeActivity.class);
                startActivity(intent);
            }
        }
    }
}
