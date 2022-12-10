package com.example.phonecapabilitytesting;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Collections;


public class BluetoothBeacon extends AppCompatActivity {
    private Beacon beacon;
    private BluetoothAdapter btAdapter;
    private ImageView beaconIv;
    private static final String TAG = "BeaconSimulator";
    private BeaconTransmitter beaconTransmitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_beacon);
        beaconIv = findViewById(R.id.beaconIV);
        setupBeacon();

        configureBackButton();
    }

    private void setupBeacon() {
        beacon = new Beacon.Builder().setId1(getString(R.string.beacon_major_simulator)).setId2(getString(R.string.beacon_major_simulator)).setId3(getString(R.string.beacon_minor_simulator))
                .setManufacturer(0x004C).setTxPower(-56).setDataFields(Collections.singletonList(0L)).build();

        BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();

        beaconTransmitter = new BeaconTransmitter(this, new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
    }
    private boolean isBluetoothLEAvailable(){
        return btAdapter != null && getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
    private boolean getBlueToothOn(){
        return btAdapter != null && btAdapter.isEnabled();
    }

    @SuppressLint("MissingPermission")
    public void onBeaconClicked(View v){
        if(getBlueToothOn()){
            Log.i(TAG, "isBlueToothOn");
            transmitIBeacon();
        } else if(!isBluetoothLEAvailable()){
            System.out.println("Bluetooth not available on your device");
        } else {
            Log.i(TAG, "Bluetooth is off");
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Enable Bluetooth");
            builder.setMessage("Please enable bluetooth before transmit iBeacon");
            builder.setPositiveButton(R.string.btn_ok, (dialog, i) -> {
                if(i == DialogInterface.BUTTON_POSITIVE){
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent,1);
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }
    private void transmitIBeacon(){
        boolean isSupported;
        isSupported = btAdapter.isMultipleAdvertisementSupported();
        if(isSupported){
            Log.v(TAG, "is supported advertisement");
            if(beaconTransmitter.isStarted()){
                beaconTransmitter.stopAdvertising();
                beaconIv.setAnimation(null);
            } else {
                beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback(){
                    @Override
                    public void onStartFailure(int errorCode){
                        Log.e(TAG, "Advertisement start failed with code: " + errorCode);
                    }

                    @Override
                    public void onStartSuccess(AdvertiseSettings settingsInEffect){
                        Log.i(TAG, "Advertisement start succeeded." + settingsInEffect.toString());
                    }
                });

                Animation anim = AnimationUtils.loadAnimation(this, R.anim.shake);
                beaconIv.startAnimation(anim);
            }
        } else {
            System.out.println("Your device is not support leBluetooth.");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            transmitIBeacon();
        } else {
            Log.e(TAG, "result not ok");
        }
    }

    private void configureBackButton() {
        Button button4 = findViewById(R.id.button10);
        button4.setOnClickListener(view -> {
            startActivity(new Intent(BluetoothBeacon.this, MainActivity.class));
            finish();
        });
    }
}
