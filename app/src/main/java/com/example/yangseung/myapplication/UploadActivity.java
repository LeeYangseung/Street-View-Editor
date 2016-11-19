package com.example.yangseung.myapplication;

/**
 * Created by Yangseung on 2016-11-18.
 */

        import java.io.DataOutputStream;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;


        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.StrictMode;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.view.ViewPager;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.Toast;

public class UploadActivity extends FragmentActivity {

    public final static int PAGES = 15;
    // You can choose a bigger number for LOOPS, but you know, nobody will fling
    // more than 1000 times just in order to test your "infinite" ViewPager :D
    public final static int LOOPS = 15;
    public final static int FIRST_PAGE = 0;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.8f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    public ViewPager pager;

    String fileName="",PanType="",Title="",Name="";

    //**
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    FileInputStream mFileInputStream =null;
    URL connectUrl=null;

    String urlString = "http://ec2-52-34-244-152.us-west-2.compute.amazonaws.com:8080";
    //**

    ProgressDialog dialog;

    String imgstr[]=null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileName = getIntent().getStringExtra("fileName");
        PanType = getIntent().getStringExtra("PanType");
        Title = getIntent().getStringExtra("Title");

        //Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_SHORT).show();

        // upload http multipart
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        Toast.makeText(getApplicationContext(),"upload 시작",Toast.LENGTH_SHORT).show();
        imgstr = fileName.split("\\,");//

        for(int j=0; j<imgstr.length; j++) {
            //	Toast.makeText(getApplicationContext(),Name+"::"+imgstr[j],Toast.LENGTH_SHORT).show(); //삭제부분
            HttpFileUpload(urlString, " ", imgstr[j]);
        }
        Toast.makeText(getApplicationContext(),"upload 완료",Toast.LENGTH_SHORT).show();


    }


    public void HttpFileUpload(String urlString, String params, String fileName) {
        try {

            File fileName2 = new File(fileName);

            mFileInputStream = new FileInputStream(fileName2);
            connectUrl = new URL(urlString);
            Log.d("Test", "mFileInputStream  is " + mFileInputStream);

            // open connection
            HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cookie",Name); //추가 Cos.jar 이용 다운로드 경로 변경에 따른 헤더 추가
            conn.setRequestProperty("Type",PanType); //추가
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName2+"\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            Log.d("Test", "image byte is " + bytesRead);

            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("Test" , "File is written");
            mFileInputStream.close();
            dos.flush(); // finish upload...

            // get response
            int ch;
            InputStream is = conn.getInputStream();
            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 ){
                b.append( (char)ch );
            }
            String s=b.toString();
            Log.e("Test", "result = " + s);
            // mEdityEntry.setText(s);
            dos.close();

        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
        }
    }
}

