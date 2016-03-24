package com.ams_demo.core;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by l.stylianou on 24/03/2016.
 */
public class CommunicationBridge {

    //private static String postUrl = "http://192.168.100.70:8000/SHIOT_02/Services/putSensorReading.xsjs?id=A001&value=80";
    //id=A001&value=50
    private static URL url ;
    private static HttpURLConnection conn ;

    private CommunicationBridge(){}

    public static void sendMsg(){

        String uri = "http://192.168.100.70:8000/SHIOT_02/Services/putSensorReading.xsjs?id=A001&value=";
        int random = (int )(Math. random() * 50 + 1);
        uri = uri + String.valueOf(random);
        String result = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);
        //conn.disconnect();

        try {
            // HttpResponse is an interface just like HttpPost.
            //Therefore we can't initialize them
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);

        } catch (ClientProtocolException cpe) {
            System.out.println("First Exception caz of HttpResponese :" + cpe);
            cpe.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Second Exception caz of HttpResponse :" + ioe);
            ioe.printStackTrace();
        }
    }
}
