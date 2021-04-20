package com.example.compassapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    CompassView compassView;
    SensorManager sensorManager;
    SensorEventListener listener;
    Sensor mageticSensor, accelSensor;

    float [] accelValuse, magneticValuse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compassView = (CompassView)findViewById(R.id.compassView);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mageticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()){
                    case Sensor.TYPE_ACCELEROMETER:
                    accelValuse = event.values.clone(); break;
                    case Sensor.TYPE_MAGNETIC_FIELD:
                    magneticValuse = event.values.clone(); break;
                    default: break;
                }
                if(magneticValuse != null && accelValuse != null){
                    float[]R = new float[16];
                    float[]I = new float[16];
                    SensorManager.getRotationMatrix(R,I,accelValuse,magneticValuse);
                    float[] valuse = new float[3];
                    SensorManager.getOrientation(R, valuse);

                    compassView.azimuth = (int) radian2degree(valuse[0]);
                    compassView.invalidate();
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) { }
        };
        sensorManager.registerListener(listener, mageticSensor,sensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(listener, accelSensor, sensorManager.SENSOR_DELAY_UI);
    }

    private float radian2degree(float radian) {return radian * 180 / (float)Math.PI;}
}