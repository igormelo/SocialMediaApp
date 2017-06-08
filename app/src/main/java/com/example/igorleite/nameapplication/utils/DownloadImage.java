package com.example.igorleite.nameapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by igor.leite on 01/06/2017.
 */

public class DownloadImage extends AsyncTask<String,Void,Bitmap>{
    CircleImageView imageView;

    public DownloadImage(CircleImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap Icon = null;
        try{
            InputStream inputStream = new java.net.URL(url).openStream();
            Icon = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Icon;
    }
    protected void onPostExecute(Bitmap result){
        imageView.setImageBitmap(result);
    }
}
