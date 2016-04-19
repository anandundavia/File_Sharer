package com.example.anandundavia.filesharer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class Client extends AppCompatActivity
{

    private Button refreshButton;
    private ListView listView;
    private String x[] = {};
    private static final String FILE_LIST_FETCHED = "filelistfetched";
    private BroadcastReceiver messageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            x = ClientService.LIST_OF_FILES;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        startService(new Intent(this, ClientService.class));
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter(FILE_LIST_FETCHED));
        refreshButton = (Button) findViewById(R.id.refreshbtn);
        refreshButton.setOnClickListener(new Refresh());
    }

    class Refresh implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            listView = (ListView) findViewById(R.id.listview);
            listView.setAdapter(new FileListAdapter(getBaseContext(), x));
        }
    }

    @Override
    public void onDestroy()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onDestroy();
    }


}
