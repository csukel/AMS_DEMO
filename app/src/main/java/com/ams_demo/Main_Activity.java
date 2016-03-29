package com.ams_demo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ams_demo.core.CommAsyncTask;

public class Main_Activity extends AppCompatActivity implements SensorEventListener {

    private Button sendMsgToServer ;
    private TextView txtXvalue;
    private TextView txtYvalue;
    private TextView txtZvalue;
    private TextView txtStepsCounter;
    private TextView txtLightValue;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senStepDetector;
    private Sensor senAnononymous;
    private long lastUpdate = 0;
    private float x, y, z;
    private float stepsCounter;
    private float lightValue;
    private static final String[] sensorId = {"ACC","STEP","LIGHT"};
    /*private Toast toast_Test;*/

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);*/
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senStepDetector = senSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        senAnononymous = senSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this,senStepDetector,SensorManager.SENSOR_DELAY_NORMAL);
        //senSensorManager.registerListener(this,senTempreture,SensorManager.SENSOR_DELAY_NORMAL);

        setContentView(R.layout.activity_main);
        initLayout();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    /*
    * Initialize UI components
    * */
    private void initLayout() {
        txtXvalue = (TextView)findViewById(R.id.txtXvalue);
        txtYvalue = (TextView)findViewById(R.id.txtYvalue);
        txtZvalue = (TextView)findViewById(R.id.txtZvalue);
        txtStepsCounter = (TextView)findViewById(R.id.txtStepsCounterValue);
        txtLightValue = (TextView)findViewById(R.id.txtLightValue);
        sendMsgToServer = (Button)findViewById(R.id.btn_Send);
        sendMsgToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new CommAsyncTask().execute();
            }
        });
    }
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
        //senSensorManager.unregisterListener(senStepDetector);
    }
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this,senStepDetector,SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this,senAnononymous,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        long curTime = System.currentTimeMillis();
/*        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
        }

        if (mySensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            stepsCounter = stepsCounter + sensorEvent.values[0] ;
            txtStepsCounter.setText(String.valueOf(stepsCounter));
            new CommAsyncTask().execute(sensorId[1],sensorEvent.values[0]);
        }*/

        switch (mySensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                x = sensorEvent.values[0];
                y = sensorEvent.values[1];
                z = sensorEvent.values[2];

                break;
            case Sensor.TYPE_STEP_DETECTOR:
                stepsCounter = stepsCounter + sensorEvent.values[0] ;
                txtStepsCounter.setText(String.valueOf(stepsCounter));
                new CommAsyncTask().execute(sensorId[1],sensorEvent.values[0]);
                break;
            case Sensor.TYPE_LIGHT:
                lightValue = sensorEvent.values[0];

                break;
            default: break;

        }
        //Update the UI (text views) every 2 secs
        if (curTime-lastUpdate>2000) {
            txtXvalue.setText(String.valueOf(x));
            txtYvalue.setText(String.valueOf(y));
            txtZvalue.setText(String.valueOf(z));
            txtLightValue.setText(String.valueOf(lightValue));
            lastUpdate = curTime;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
