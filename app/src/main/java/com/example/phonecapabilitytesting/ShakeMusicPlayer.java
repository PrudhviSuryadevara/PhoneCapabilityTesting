package com.example.phonecapabilitytesting;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ShakeMusicPlayer extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float last_x;
    private float last_y;
    private float last_z;
    private boolean isFirstValue;
    private MediaPlayer mMediaPlayer;
    int counter = 0;
    ArrayList<Object> musicFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_music_player);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        musicFiles = new ArrayList<>();
        int m1 = R.raw.m1;
        mMediaPlayer =  MediaPlayer.create(getApplicationContext(), m1);
        int m2 = R.raw.m2;
        int m3 = R.raw.m3;
        musicFiles.add(m1);
        musicFiles.add(m2);
        musicFiles.add(m3);

        configureBackButton1();

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if(isFirstValue) {
            float deltaX = Math.abs(last_x - x);
            float deltaY = Math.abs(last_y - y);
            float deltaZ = Math.abs(last_z - z);
            float shakeThreshold = 3f;
            if((deltaX > shakeThreshold && deltaY > shakeThreshold)
                    || (deltaX > shakeThreshold && deltaZ > shakeThreshold)
                    || (deltaY > shakeThreshold && deltaZ > shakeThreshold)) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    Toast.makeText(this,"Motion Detected, Changing Song" ,
                            Toast.LENGTH_SHORT).show();
                    counter = counter +1;
                    if (counter > 2) {counter = 0; }
                    mMediaPlayer =  MediaPlayer.create(getApplicationContext(),
                            (Integer) musicFiles.get(counter));

                }

                mMediaPlayer.start();

            }
        }
        last_x = x;
        last_y = y;
        last_z = z;
        isFirstValue = true;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void configureBackButton1() {
        Button button4 = findViewById(R.id.button8);
        button4.setOnClickListener(view -> {
            startActivity(new Intent(ShakeMusicPlayer.this, MainActivity.class));
            mMediaPlayer.stop();
            finish();

        });
    }

}
