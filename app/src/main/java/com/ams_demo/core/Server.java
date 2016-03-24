package com.ams_demo.core;

/**
 * Created by l.stylianou on 24/03/2016.
 */



public class Server {
    private String host;
    private String port;
    private String url;

    public Server (String host , String port){
        this.host = host;
        this.port = port;
    }

    public Server(String url){
        this.url = url;
    }

    public String getHost(){
        return this.host;
    }

    public String getPort(){
        return this.port;
    }


}
