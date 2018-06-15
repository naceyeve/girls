package com.love.girls;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018/6/15.
 */

public class PictureLoader {
    private ImageView mImageView;
    private String mImgUrl;
    private byte[] mByteArray;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                if (mByteArray != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(mByteArray, 0, mByteArray.length);
                    mImageView.setImageBitmap(bitmap);
                }
            }
        }
    };
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(mImgUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("get");
                //set the outtime
                conn.setConnectTimeout(10000);
                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int length = -1;
                    while ((length = in.read(bytes)) != -1) {
                        out.write(bytes, 0, length);
                    }
                    mByteArray = out.toByteArray();
                    in.close();
                    out.close();
                    handler.sendEmptyMessage(0x123);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public void load(ImageView imageView, String imgUrl) {
        this.mImageView = imageView;
        this.mImgUrl = imgUrl;
        //recycle the Bitmap
        Drawable drawable = mImageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }

        }
        //  load the bitmap in the thread
        new Thread(mRunnable).start();
    }
}
