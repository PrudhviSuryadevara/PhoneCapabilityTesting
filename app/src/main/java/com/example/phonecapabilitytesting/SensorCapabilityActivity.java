package com.example.phonecapabilitytesting;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SensorCapabilityActivity extends Activity {
    private int mSensorType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_capability);
        Intent intent = getIntent();
        mSensorType = intent.getIntExtra(getResources().getResourceName(R.string.sensor_type), 0);
        SensorManager mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensor = mSensorManager.getDefaultSensor(mSensorType);
        TextView mSensorNameTextView = (TextView) findViewById(R.id.sensor_name);
        TextView mSensorMaximumRangeTextView = (TextView) findViewById(R.id.sensor_range);
        TextView mSensorMinDelayTextView = (TextView) findViewById(R.id.sensor_mindelay);
        TextView mSensorPowerTextView = (TextView) findViewById(R.id.sensor_power);
        TextView mSensorResolutionTextView = (TextView) findViewById(R.id.sensor_resolution);
        TextView mSensorVendorTextView = (TextView) findViewById(R.id.sensor_vendor);
        TextView mSensorVersionTextView = (TextView) findViewById(R.id.sensor_version);
        mSensorNameTextView.setText(mSensor.getName());
        mSensorMaximumRangeTextView.setText(String.valueOf(mSensor.getMaximumRange()));
        mSensorMinDelayTextView.setText(String.valueOf(mSensor.getMinDelay()));
        mSensorPowerTextView.setText(String.valueOf(mSensor.getPower()));
        mSensorResolutionTextView.setText(String.valueOf(mSensor.getResolution()));
        mSensorVendorTextView.setText(String.valueOf(mSensor.getVendor()));
        mSensorVersionTextView.setText(String.valueOf(mSensor.getVersion()));
    }
    public void onClickSensorValues(View v)
    {
        Intent intent = new Intent(getApplicationContext(),
                SensorValuesActivity.class);
        intent.putExtra(getResources().getResourceName(R.string.sensor_type), mSensorType);
        Log.d(TAG, "Capability: " +
                mSensorType);
        startActivity(intent);
        finish();
    }

}
