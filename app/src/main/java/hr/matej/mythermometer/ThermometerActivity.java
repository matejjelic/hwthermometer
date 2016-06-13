package hr.matej.mythermometer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * By Matej.
 * Source: http://developer.android.com/guide/topics/sensors/sensors_environment.html
 */
public class ThermometerActivity extends AppCompatActivity implements SensorEventListener {

    public static final String CELSIUS_DEGREES = " °C";
    //    public static final String LOG_TAG = "ThermometerActivity";
    private SensorManager mSensorManager;
    private Sensor mThermometer;
    private TextView tvTemperature;
    private SharedPreferences prefs;
    private double calibration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermometer);

        initialize();
    }

    public void initialize() {
        getCalibrationFromPrefs();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mThermometer = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        tvTemperature = (TextView) findViewById(R.id.tvTemperature);
    }

    @Override
    protected void onResume() {
        getCalibrationFromPrefs();
        super.onResume();
        mSensorManager.registerListener(this, mThermometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        BigDecimal inputTemperature = new BigDecimal(event.values[0]);
        BigDecimal calibration = new BigDecimal(this.calibration);
        BigDecimal finalTemperature = inputTemperature.add(calibration).setScale(2, RoundingMode.HALF_UP);
        tvTemperature.setText(finalTemperature + CELSIUS_DEGREES);
//        Log.d(LOG_TAG, "SC -> Sensor: " + event.sensor.getName() + ", accuracy: " + event.accuracy + ", timestamp: " + event.timestamp + ", value: " + event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        Log.d(LOG_TAG, "AC -> Sensor: " + sensor.getName() + ", accuracy: " + accuracy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                this.startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.action_help:
                this.startActivity(new Intent(this, SensorLocationActivity.class));
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getCalibrationFromPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs != null) {
            calibration = prefs.getFloat("calibration", 0.0f);
        } else {
            calibration = 0.00;
        }
    }
}
