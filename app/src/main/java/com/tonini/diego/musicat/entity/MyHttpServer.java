package com.tonini.diego.musicat.entity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created by Diego on 06/09/2015.
 */
public class MyHttpServer {

    private ServletContextHandler handler;
    Server server;
    Context context;

    public MyHttpServer(Context context){

        this.context=context;

        server=new Server(8080); // The port is being set here
        handler=new ServletContextHandler();
        handler.setContextPath("/");
        handler.addServlet(new ServletHolder(new SimpleServlet()),"/");
        server.setHandler(handler);
    }

    /*
     * We better run the server in a separate thread.
     */
    public void start(){
        new Thread(){
            public void run(){
                try{
                    server.start();
                }catch(Exception e){
                    Log.e("SERVER_START",e.toString());
                }
            }
        }.start();
    }

    public String getIPAddress(){
        ConnectivityManager con;
        con = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(con.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI)
                .isConnected()){
            WifiManager wifi;
            wifi=(WifiManager)context
                    .getSystemService(Context.WIFI_SERVICE);
            int ip=wifi.getConnectionInfo().getIpAddress();
            return Formatter.formatIpAddress(ip);
        }else{
            return "You are not connected to WIFI";
        }
    }

    public void stop(){
        try{
            server.stop();
        }catch(Exception e){
            Log.e("SERVER_STOP", e.toString());
        }
    }
}
