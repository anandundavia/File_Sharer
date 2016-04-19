package com.example.anandundavia.filesharer;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FileListAdapter extends ArrayAdapter
{
    private String fileNames[];
    private Context localContext;

    ClientService cs = new ClientService();

    public FileListAdapter(Context context, String files[])
    {
        super(context, android.R.layout.simple_list_item_1, files);
        fileNames = files;
        localContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View localView = convertView;
        if (localView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) localContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            localView = layoutInflater.inflate(R.layout.list_item, null);
        }

        ((TextView) localView.findViewById(R.id.fileName)).setText(fileNames[position]);
        (localView.findViewById(R.id.downloadBtn)).setOnClickListener(new ButtonListener(position));
        return localView;
    }

    class ButtonListener implements View.OnClickListener
    {
        int pos;

        public ButtonListener(int n)
        {
            pos = n;
        }

        @Override
        public void onClick(View v)
        {
            Log.e("Anand", "Clicked " + pos);
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    cs.download(pos);
                }
            }).start();
        }
    }
}
