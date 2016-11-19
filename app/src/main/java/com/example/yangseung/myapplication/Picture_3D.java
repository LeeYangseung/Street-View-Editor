package com.example.yangseung.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sang-Won Yeo on 2016-11-09.
 */


public class Picture_3D extends Activity {

    ImageView imView; //Set Image View for Picture
    //String imgUrl = "http://52.34.244.152:80/images/out.img";  //Server URL
    String imgUrl = "http://52.34.244.152:8080/upload.jsp";  //Server URL
    Bitmap bmImg; //Bit map for Picture
    back task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture);

        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.street);
        setContentView(image);


        task = new back(); //Task for back
        imView = (ImageView) findViewById(R.id.imag); //Image View for Picture
        //task.execute(imgUrl);//execute using URL


    }

    /*Image create part*/
    private class back extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            // TODO Auto-generated method stub
            try {
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);


            } catch (IOException e) {
                e.printStackTrace();
            }
            bmImg = rotate(bmImg, 270);
            return bmImg;
        }

        /*Set Image part*/
        protected void onPostExecute(Bitmap img) {
            imView.setImageBitmap(bmImg);
        }

    }

    /*Rotate Image*/
    public Bitmap rotate(Bitmap bitmap, int degrees) {

        if (degrees != 0 && bitmap != null) {

            Matrix m = new Matrix();

            m.setRotate(degrees);

            try {

                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,

                        bitmap.getWidth(), bitmap.getHeight(), m, true);

                if (bitmap != converted) {

                    bitmap = null;

                    bitmap = converted;

                    converted = null;

                }

            } catch (OutOfMemoryError ex) {

            }

        }

        return bitmap;

    }
}