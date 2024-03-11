package com.example.gimnasioforce;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView stepsTextView;
    private TextView distanceTextView;
    private float previousX = 0f;

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private int stepsCount = 0;
    private boolean running = false;
    final float ACCELEROMETER_THRESHOLD = 10f;
    private float distanceCovered = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepsTextView = findViewById(R.id.stepsTextView);
        distanceTextView = findViewById(R.id.distanceTextView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepsCount = 0;
                running = true;
            }
        });

        Button stopButton = findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorListener);
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (running) {
                float x = event.values[0];
                float deltaX = Math.abs(x - previousX);
                if (deltaX > ACCELEROMETER_THRESHOLD) {
                    stepsCount++;
                    stepsTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            stepsTextView.setText("Pasos: " + stepsCount);
                        }
                    });
                }
                distanceCovered = stepsCount * 0.7f;
                distanceTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        distanceTextView.setText("Distancia recorrida: " + distanceCovered + " metros");
                    }
                });
                previousX = x;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Ignorar para este ejemplo
        }
    };
}
