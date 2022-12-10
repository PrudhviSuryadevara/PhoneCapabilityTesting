package com.example.phonecapabilitytesting;

import static android.Manifest.permission.RECORD_AUDIO;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;

public class SoundDetection extends AppCompatActivity {
    Button buttonStart, buttonStop;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    public static final int RequestPermissionCode = 1;

    boolean recordingActive = false;
    private boolean permissionToRecordAccepted = false;
    private LineChart mChart;
    private Thread thread;
    private boolean plotData = true;

    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mHandler=new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_detection);
        buttonStart = findViewById(R.id.button3);
        buttonStop = findViewById(R.id.button4);

        buttonStop.setEnabled(false);

        requestPermission();

        mChart = findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(true);
        mChart.getDescription().setText("");
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.WHITE);
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.SQUARE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setDrawLabels(true);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDrawBorders(false);
        buttonStart.setOnClickListener(view -> {

            AudioSavePathInDevice = getExternalCacheDir().getAbsolutePath();
            AudioSavePathInDevice += "/audiorecordtest.3gp";
            MediaRecorderReady();
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                feedMultiple();

            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);

            Toast.makeText(SoundDetection.this, "started",
                    Toast.LENGTH_LONG).show();   });

        buttonStop.setOnClickListener(view -> {
            mediaRecorder.stop();
            buttonStop.setEnabled(false);
            buttonStart.setEnabled(true);
            recordingActive = false;
            Toast.makeText(SoundDetection.this, "Stopped",
                    Toast.LENGTH_LONG).show();
        });

        configureBackButton1();
    }

    private void addEntry(MediaRecorder mediaRecorder) {
        LineData data = mChart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), mediaRecorder.getMaxAmplitude()/1000), 0);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(150);
            mChart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Sound levels");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.GREEN);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    private void feedMultiple() {

        if (thread != null){
            thread.interrupt();
        }

        thread = new Thread(() -> {
            while (true){
                plotData = true;
                try {

                    Thread.sleep(100);

                    if(plotData){
                        mHandler.post(() -> addEntry(mediaRecorder));
                    }

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);



    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(SoundDetection.this,
                new String[]{RECORD_AUDIO}, RequestPermissionCode);   }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestPermissionCode) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    private void configureBackButton1() {
        Button button3 = findViewById(R.id.button6);
        button3.setOnClickListener(view -> {
            startActivity(new Intent(SoundDetection.this, MainActivity.class));
            finish();
        });
    }
}
