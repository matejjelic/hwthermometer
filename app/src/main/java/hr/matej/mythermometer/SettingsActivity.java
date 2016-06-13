package hr.matej.mythermometer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private SeekBar seekBar;
    private TextView tvCalibration;
    private Double calibration;
    private SharedPreferences prefs;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
    }

    public void initialize() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tvCalibration = (TextView) findViewById(R.id.tvCalibration);
        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setMax(1000);

        if (prefs != null) {
            calibration = new Double(prefs.getFloat("calibration", 0.0f));
            double tempProgress = (calibration + 5) * 100;
            seekBar.setProgress((int) tempProgress);
            tvCalibration.setText(calibration.toString());
        } else {
            calibration = 0.00;
            seekBar.setProgress(500);
            tvCalibration.setText(calibration.toString());
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        calibration = ((progress - 500) / 100.00);
        tvCalibration.setText(calibration.toString());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Do nothing ...
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        storeCalibrationToSharePrefs();
    }

    public void storeCalibrationToSharePrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("calibration", (float) calibration.doubleValue());
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetButton:
                calibration = 0.00;
                seekBar.setProgress(500);
                tvCalibration.setText(calibration.toString());
                storeCalibrationToSharePrefs();
                break;

            default:
                break;
        }
    }
}
