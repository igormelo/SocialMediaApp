package com.example.igorleite.nameapplication.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.igorleite.nameapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by igor.leite on 01/06/2017.
 */

public class ViewImage extends Activity {
    CircleImageView imageview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        int position = i.getExtras().getInt("position");
        String[] filepath = i.getStringArrayExtra("filepath");
        imageview = (CircleImageView) findViewById(R.id.profileImage);
        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);
        imageview.setImageBitmap(bmp);

    }
}

