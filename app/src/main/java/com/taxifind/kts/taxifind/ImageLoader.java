package com.taxifind.kts.taxifind;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    public ImageLoader(){

    }
    
    final int stub_id = R.drawable.taxi_rank;
    public void DisplayImage(ImageView imageView)
    {
        //queuePhoto(url, imageView);
        imageView.setImageResource(stub_id);
    }
}
