package com.ams_demo.core;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by l.stylianou on 24/03/2016.
 */
public class CommAsyncTask extends AsyncTask<Object , Void , Integer> {


    protected void onPreExecute (){
        //Log.d("PreExceute", "On pre Exceute......");
    }

    protected Integer doInBackground(Object ... params) {
        //Log.d("DoINBackGround","On doInBackground...");

        /*for(int i=0; i<10; i++){
            Integer in = new Integer(i);
            publishProgress(i);
        }*/
        //to check on click listener
        //Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
        //Log.d("Test async task","Success");
        CommunicationBridge.sendMsg(params[0],params[1],params[2]);
        return 0;
    }

    protected void onProgressUpdate(){
        //Log.d("You are in progress update ... " + a[0]);
    }

    protected void onPostExecute(String result) {
        //Log.d(""+result);
    }
}
