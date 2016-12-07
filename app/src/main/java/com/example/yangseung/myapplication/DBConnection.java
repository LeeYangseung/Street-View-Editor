package com.example.yangseung.myapplication;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.spec.ECField;

/**
 * Created by Yangseung on 2016-12-03.
 */

public class DBConnection {
    private String jsp = null;
    private String url = null;
    private String result = null;
    private String[] parameter;
    private Thread thread = null;

    public DBConnection(String jsp){
        this.jsp = jsp;
        url = "http://ec2-52-34-244-152.us-west-2.compute.amazonaws.com:8080/"+jsp ;
    }

    public DBConnection(){
        url = "http://ec2-52-34-244-152.us-west-2.compute.amazonaws.com:8080/";
    }

    //데이터베이스에 접속하는 외부 호출함수
    public String dbConnection(String jsp, String ... args){

        try {
            if(this.jsp == null){
                url += jsp;
            }

            this.parameter = args;

            thread = new Thread(){
                public void run() {
//                    new HttpAsyncTask().execute(url);
                    POST(url,parameter);
                }
            };

            thread.start();
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return this.result;
    }


    public String POST(String url, String[] msgs) {

        InputStream inputStream = null;
        String result = "";
        try {
            Log.d("2", "POST function");
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            for(int i = 1; i <= msgs.length; i += 2) {
                Log.d("3", msgs[i-1] +" : "+msgs[i]);
                Log.d("3", "msg length : " + msgs[i].length());
                jsonObject.accumulate(msgs[i-1], msgs[i]);
            }

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.d("3", "JSON message : "+ jsonObject.toString());

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

        this.result = result;
        Log.d("6", result);
        Log.d("6", "result length: " + result.length());

        // 11. return result
        return result;
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            Log.d("1", "HttpAsyncTask function");
            return POST(urls[0], parameter);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
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

<<<<<<< HEAD
}
=======
}
>>>>>>> 9872a3353c212544ac68096b335589920c39a9b8
