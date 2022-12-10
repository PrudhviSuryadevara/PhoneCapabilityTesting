package com.example.phonecapabilitytesting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_main);

        configureButton ();
        configureButton1 ();
        configureButton2 ();
        configureButton3 ();

    }

    private void configureButton() {
        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SensorListActivity.class));
            finish();
        });
    }

    private void configureButton1() {
        Button button1 = findViewById(R.id.button5);
        button1.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SoundDetection.class));
            finish();
        });
    }

    private void configureButton2() {
        Button button2 = findViewById(R.id.button7);
        button2.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ShakeMusicPlayer.class));
            finish();
        });
    }

    private void configureButton3() {
        Button button3 = findViewById(R.id.button9);
        button3.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, BluetoothBeacon.class));
            finish();
        });
    }

}