package com.example.anandundavia.filesharer;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerService extends Service
{
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private static final int PORT_ADDRESS = 53000;

    private static final String CLIENT_CONNECTED = "Client Connected !";

    public ServerService()
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
        new Thread(new ClientHandler()).start();
        return super.onStartCommand(intent, flags, startId);
    }


    class ClientHandler implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                serverSocket = new ServerSocket(PORT_ADDRESS);
                clientSocket = serverSocket.accept();
                PrintStream outputToClient = new PrintStream(clientSocket.getOutputStream());
                BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(new Intent(CLIENT_CONNECTED));
                File list[] = Server.SHARED_FOLDER.listFiles();
                String listOfFiles = "";
                for (File x : list)
                {
                    listOfFiles += x.getName()+" ";
                }
                outputToClient.println(listOfFiles);
                while (true)
                {
                    String fileName = inputFromClient.readLine();
                    Log.e("Anand", "File Name Received");
                    File fileToSend = new File(Environment.getExternalStorageDirectory() + "/SHARED/" + fileName);
                    if(fileToSend.exists())
                    {
                        Log.e("Anand","File Found");
                    }
                    else
                    {
                        Log.e("Anand","File Not Found");
                    }
                    byte[] bytes = new byte[16 * 1024];
                    InputStream in = new FileInputStream(fileToSend);
                    OutputStream out = clientSocket.getOutputStream();

                    int count;
                    while ((count = in.read(bytes)) > 0)
                    {
                        Log.e("Anand", "Data send");
                        out.write(bytes, 0, count);
                    }
                    out.close();

                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
