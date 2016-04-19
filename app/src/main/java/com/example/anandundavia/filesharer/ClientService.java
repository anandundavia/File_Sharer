package com.example.anandundavia.filesharer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class ClientService extends Service
{
    static Socket client;

    static final String FILE_LIST_FETCHED = "filelistfetched";

    static final int PORT_ADDRESS = 53000;
    static final String IP_ADDRESS = "192.168.43.1";

    public static String LIST_OF_FILES[];

    static BufferedReader inputFromServer;
    static PrintStream outputToServer;

    public ClientService()
    {

    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        new Thread(new ConnectionHandler(getBaseContext())).start();
        return super.onStartCommand(intent, flags, startId);
    }

    public void download(final int position)
    {

        try
        {
            Log.e("Anand", "Sending filename");
            outputToServer.println(LIST_OF_FILES[position]);
            Log.e("Anand", "filename sent");

            InputStream in = client.getInputStream();
            File downloadedFile = new File(Environment.getExternalStorageDirectory() + "/" + LIST_OF_FILES[position]);
            Log.e("Anand", Environment.getExternalStorageDirectory() + "/" + LIST_OF_FILES[position]);
            if (!downloadedFile.exists())
            {
                downloadedFile.createNewFile();
                Log.e("Anand", "File Created");
            }
            OutputStream out = new FileOutputStream(downloadedFile);
            byte[] bytes = new byte[16 * 1024];
            Log.e("Anand","Before while");
            int count;
            while ((count = in.read(bytes)) > 0)
            {
                Log.e("Anand", "Data received");
                out.write(bytes, 0, count);
            }
            Log.e("Anand","After While");
            out.close();
            Toast.makeText(getBaseContext(),"File Downloaded",Toast.LENGTH_SHORT).show();
            Log.e("Anand", "Downloaded");
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}

class ConnectionHandler implements Runnable
{
    Context ctx;
    ConnectionHandler(Context c){
        ctx = c;
    }
    @Override
    public void run()
    {
        try
        {
            ClientService.client = new Socket(ClientService.IP_ADDRESS, ClientService.PORT_ADDRESS);
            ClientService.inputFromServer = new BufferedReader(new InputStreamReader(ClientService.client.getInputStream()));
            ClientService.outputToServer = new PrintStream(ClientService.client.getOutputStream());
            ClientService.LIST_OF_FILES = ClientService.inputFromServer.readLine().split(" ");
            LocalBroadcastManager.getInstance(ctx).sendBroadcast(new Intent(ClientService.FILE_LIST_FETCHED));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
