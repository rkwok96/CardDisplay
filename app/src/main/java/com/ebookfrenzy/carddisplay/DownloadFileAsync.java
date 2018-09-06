package com.ebookfrenzy.carddisplay;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Randy on 7/28/2018. https://stackoverflow.com/questions/3028306/download-a-file-with-android-and-showing-the-progress-in-a-progressdialog
 */

public class DownloadFileAsync extends AsyncTask<String, String, String> {

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        System.out.println("Starting download!");
    }

    @Override
    protected String doInBackground(String... f_url){
        int count;
        try {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.v("fucking", "root path is " + root);
            System.out.println("Downloading");
            URL url = new URL(f_url[0]);

            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file

            OutputStream output = new FileOutputStream(root+"/Pictures/downloadedfile.jpg");
            byte data[] = new byte[1024];

            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);

            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }


    @Override
    protected void onPostExecute(String file_url){
        System.out.println("Downloaded!");
    }
}
