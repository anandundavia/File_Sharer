package com.example.anandundavia.filesharer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Server extends AppCompatActivity
{
    private WifiManager myManager;
    public static File SHARED_FOLDER = null;

    public static final String TAG = "Anand";
    private static final String CLIENT_CONNECTED = "Client Connected !";

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ((TextView)findViewById(R.id.client)).setText("Client Connected..   ");
            findViewById(R.id.loadingPanel2).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.tick2)).setText(Html.fromHtml("&#x2713;"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        startService(new Intent(getBaseContext(),ServerService.class));

        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,new IntentFilter(CLIENT_CONNECTED));

        SHARED_FOLDER = new File(Environment.getExternalStorageDirectory() + "/SHARED");
        if (!SHARED_FOLDER.exists())
        {
            SHARED_FOLDER.mkdir();
        }

        myManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (myManager.isWifiEnabled())
            myManager.setWifiEnabled(false);

        WifiConfiguration wifi_configuration = null;

        try
        {
            Method method = myManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(myManager, wifi_configuration, true);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
        } catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.loadingPanel);
        progressBar.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                progressBar.setVisibility(View.GONE);
                TextView tick = (TextView) findViewById(R.id.tick);
                tick.setText(Html.fromHtml("&#x2713;"));
            }
        },2500);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onDestroy();
    }


}
