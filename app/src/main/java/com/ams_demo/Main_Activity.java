package com.ams_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ams_demo.core.CommAsyncTask;

public class Main_Activity extends AppCompatActivity {

    private Button sendMsgToServer ;
    /*private Toast toast_Test;*/

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
    }

    private void initLayout() {
        sendMsgToServer = (Button)findViewById(R.id.btn_Send);
        sendMsgToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommAsyncTask().execute();
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
    }

}
