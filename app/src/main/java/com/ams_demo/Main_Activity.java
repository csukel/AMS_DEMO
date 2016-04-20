package com.ams_demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ams_demo.core.CommAsyncTask;

public class Main_Activity extends AppCompatActivity implements SensorEventListener {

    private Button sendMsgToServer ;
    private TextView txtXvalue;
    private TextView txtYvalue;
    private TextView txtZvalue;
    private TextView txtStepsCounter;
    private TextView txtLightValue;
    private EditText edtName;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senStepDetector;
    private Sensor senLight;
    private long lastUpdate = 0;
    private float x, y, z;
    private float stepsCounter;
    private float lightValue;
    private String personId;
    private boolean isPressed = false;
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
        senLight = senSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);





        setContentView(R.layout.activity_main);
        initLayout(this);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    /*
    * Initialize UI components
    * */
    private void initLayout(final Main_Activity main_activity) {
        txtXvalue = (TextView)findViewById(R.id.txtXvalue);
        txtYvalue = (TextView)findViewById(R.id.txtYvalue);
        txtZvalue = (TextView)findViewById(R.id.txtZvalue);
        txtStepsCounter = (TextView)findViewById(R.id.txtStepsCounterValue);
        edtName = (EditText)findViewById(R.id.edtName);
        txtLightValue = (TextView)findViewById(R.id.txtLightValue);
        sendMsgToServer = (Button)findViewById(R.id.btn_Send);
        sendMsgToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(main_activity)) {

                    Toast.makeText(main_activity, "No network available...", Toast.LENGTH_SHORT).show();
                } else if (edtName.getText().toString().equals("") && !(isPressed)) {
                    Toast.makeText(main_activity, "Enter a name...", Toast.LENGTH_SHORT).show();
                } else if (!isPressed) {
                    senSensorManager.registerListener(main_activity, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                    senSensorManager.registerListener(main_activity, senStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
                    senSensorManager.registerListener(main_activity, senLight, SensorManager.SENSOR_DELAY_NORMAL);
                    personId = edtName.getText().toString();
                    //remove whitespaces
                    personId = personId.replace(" ","");
                    edtName.setText("");
                    edtName.setHint(personId);
                    edtName.setEnabled(false);
                    isPressed = !isPressed;
                } else {
                    Toast.makeText(main_activity, "You have already entered a name...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    protected void onPause() {
        super.onPause();
        if (isPressed) {
            //senSensorManager.unregisterListener(this);
        }
        //senSensorManager.unregisterListener(senStepDetector);
    }
    protected void onResume() {
        super.onResume();
        if (isPressed) {
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            senSensorManager.registerListener(this, senStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
            senSensorManager.registerListener(this, senLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
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
        if (isPressed) {
            switch (mySensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    x = sensorEvent.values[0];
                    y = sensorEvent.values[1];
                    z = sensorEvent.values[2];

                    break;
                case Sensor.TYPE_STEP_DETECTOR:
                    stepsCounter = stepsCounter + sensorEvent.values[0];
                    txtStepsCounter.setText(String.valueOf(stepsCounter));
                    new CommAsyncTask().execute(sensorId[1], sensorEvent.values[0],personId);
                    break;
                case Sensor.TYPE_LIGHT:
                    lightValue = sensorEvent.values[0];

                    break;
                default:
                    break;

            }
            //Update the UI (text views) every 2 secs
            if (curTime - lastUpdate > 2000) {
                txtXvalue.setText(String.valueOf(x));
                txtYvalue.setText(String.valueOf(y));
                txtZvalue.setText(String.valueOf(z));
                new CommAsyncTask().execute(sensorId[0], z * 1000,personId);
                txtLightValue.setText(String.valueOf(lightValue));
                new CommAsyncTask().execute(sensorId[2],lightValue*1000,personId);
                lastUpdate = curTime;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mitem_info:
                showDialogBox();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialogBox() {
        String msg ;
        msg = (senAccelerometer == null) ? "Accelerometer: null" : "Accelerometer: Available";
        msg += (senStepDetector == null) ?"\nStep Detector: null" :"\nStep Detector: Available";
        msg += (senLight == null) ? "\nLight Sensor: null" : "\nLight Sensor: Available";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setTitle("Sensors' Info")
                .show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
