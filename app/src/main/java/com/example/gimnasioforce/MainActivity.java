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
    float previousX = 0f;

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private int stepsCount = 0;
    boolean running = false;
    final float ACCELEROMETER_THREHOLD = 10f;
    private float distanceCovered = 0;
    private Handler handler;
    private Runnable updateStepsRunnable;

    Button star;
    Button stop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepsTextView = findViewById(R.id.stepsTextView);
        distanceTextView = findViewById(R.id.distanceTextView);
        star=findViewById(R.id.startButton);
        stop=findViewById(R.id.stopButton);
        stepsTextView.setText("Pasos: " + stepsCount);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepsCount = 0;
                running = true;
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
            }
        });



        //@Override
    /*public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (event.values.length > 0) {
                stepsCount = (int) event.values[0];
            } else {
                // No se recibieron datos del sensorXX
                stepsTextView.setText("Pasos" + stepsCount);
                return;
            }

            // Calcular distancia aproximada (cada paso es aproximadamente 0.7 metros)
            distanceCovered = stepsCount * 0.7f;
            distanceTextView.setText("Distancia recorrida: " + distanceCovered + " metros");
            showAlert(String.valueOf(stepsCount));
        }
        {
            handler = new Handler();
            updateStepsRunnable = new Runnable() {
                @Override
                public void run() {
                    // Actualizar el TextView con los pasos contados
                    stepsTextView.setText("Pasos: " + stepsCount);
                    // Programar la actualización periódica cada segundo (1000 milisegundos)
                    handler.postDelayed(this, 1000);
                }
            };
        }
    }*/




    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        running = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (stepSensor != null) {
            sensorManager.unregisterListener(sensorListener);
        }
    }
    final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (running) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float deltaX = x - previousX;
                if (deltaX > ACCELEROMETER_THREHOLD) {
                    stepsCount++;
                    stepsTextView.setText("Pasos: " + stepsCount);
                }
                distanceCovered = stepsCount * 0.7f;
                distanceTextView.setText("Distancia recorrida: " + distanceCovered + " metros");
            }
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
