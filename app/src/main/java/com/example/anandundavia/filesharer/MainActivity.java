package com.example.anandundavia.filesharer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton, receiveButton;
        sendButton = (Button) findViewById(R.id.sendbtn);
        receiveButton = (Button) findViewById(R.id.receivebtn);

        MyListener listener = new MyListener();
        sendButton.setOnClickListener(listener);
        receiveButton.setOnClickListener(listener);

    }

    class MyListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Intent intent;
            if (v.getId() == R.id.sendbtn)
            {
                intent = new Intent(MainActivity.this, Server.class);
            } else
            {
                intent = new Intent(MainActivity.this, Client.class);
            }
            startActivity(intent);
        }
    }
}
