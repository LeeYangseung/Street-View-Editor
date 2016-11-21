package com.example.yangseung.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Post extends AppCompatActivity {

    //글로벌 변수
    final int REQ_CODE_SELECT_IMAGE = 100;
    String bookPath;
    Bitmap image_bitmap = null;
    String globalResult = null;

    //layout 변수
    ImageView bookImage = null;
    ImageView imageView_test = null;
    Button uploadBt = null;
    TextView bookName = null;
    Button button_test = null;

    //Intent로 받아오는 변수
    String now = null;
    double clicked_latitude = 0.0;
    double clicked_longtitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        Intent intent = getIntent();
        clicked_latitude = intent.getDoubleExtra("clicked_latitude",0.0);
        clicked_longtitude = intent.getDoubleExtra("clicked_longtitude",0.0);
        now = intent.getStringExtra("now");

        bookImage = (ImageView) findViewById(R.id.imageView4);
        uploadBt = (Button) findViewById(R.id.button_upload);
        bookName = (TextView) findViewById(R.id.nowPath);
        imageView_test = (ImageView) findViewById(R.id.imageView_test);
        button_test = (Button) findViewById(R.id.button_test);

        bookName.setText(now);
        bookImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

                File tempFile = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
                Uri tempUri = Uri.fromFile(tempFile);

                intent.putExtra("crop", "true");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);

                intent.setType("image/*");
            }
        });
        uploadBt.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        new HttpAsyncTask().execute("http://ec2-52-34-244-152.us-west-2.compute.amazonaws.com:8080/upload.jsp");
                    }
                }.start();
            }
        });
        button_test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String tmp = globalResult.replace("\\n","\n");
                Bitmap bitmapImage = getBitmapFromString(tmp);

                imageView_test.setImageBitmap(bitmapImage);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    String name_Str = getImageNameToUri(data.getData());
                    bookPath = getRealPathFromURI(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView) findViewById(R.id.imageView4);

                    //배치해놓은 ImageView에 set
                    image.setImageBitmap(image_bitmap);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgName;
    }

    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {
        /*
        * This functions converts Bitmap picture to a string which can be
        * JSONified.
        * */
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

        bitmapPicture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    private Bitmap getBitmapFromString(String jsonString) {
        /*
        * This Function converts the String back to Bitmap
        * */
        Log.d("7", jsonString);
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
        Log.d("7", "Base64 decoding");
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Log.d("8", "BitmapFactory decoding");
        return decodedByte;
    }

    public String POST(String url, String msg) {

        InputStream inputStream = null;
        String result = "";
        try {
            Log.d("2", "POST function");
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";
            Log.d("3", msg);
            Log.d("3", "msg length : " + msg.length());
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("image", msg);
            jsonObject.accumulate("coordinate_x",clicked_latitude);
            jsonObject.accumulate("coordinate_y",clicked_longtitude);
            jsonObject.accumulate("fork_num",Integer.parseInt(now)+1);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            Log.d("4", "Post executed");
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            Log.d("5", "Server response");
            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        globalResult = result;
        Log.d("6", result);
        Log.d("6", "result length: " + result.length());

        // 11. return result
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
//            Context context = getApplicationContext();/*
//            Bitmap pictureBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.aa);*/
            String str = getStringFromBitmap(image_bitmap);

            Log.d("1", "HttpAsyncTask function");
            return POST(urls[0], str);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}
