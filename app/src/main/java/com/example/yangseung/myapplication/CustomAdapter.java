package com.example.yangseung.myapplication;

/**
 * Created by Sang-Won Yeo on 2016-11-19.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by Sang-Won Yeo on 2016-11-19.
 */

public class CustomAdapter extends PagerAdapter {


    LayoutInflater inflater;
    int counter;
    double x = 0.0;
    double y = 0.0;
    String globalResult = null;
    Bitmap[] bitmapArray = null;
    boolean flg = false;
    Context context;
    public CustomAdapter(LayoutInflater inflater, int counter) {
        // TODO Auto-generated constructor stub
        //전달 받은 LayoutInflater를 멤버변수로 전달

        this.inflater = inflater;
        this.counter = counter;
    }

    public CustomAdapter(LayoutInflater inflater, int counter, double x, double y, Context context) {
        // TODO Auto-generated constructor stub
        //전달 받은 LayoutInflater를 멤버변수로 전달
        this.context  = context;
        this.inflater = inflater;
        this.counter = counter;

        this.x = x;
        this.y = y;
        new Thread() {
            public void run() {
                new HttpAsyncTask().execute("http://ec2-52-34-244-152.us-west-2.compute.amazonaws.com:8080/download.jsp");
            }
        }.start();
    }

    //PagerAdapter가 가지고 잇는 View의 개수를 리턴
    //보통 보여줘야하는 이미지 배열 데이터의 길이를 리턴
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return counter; //이미지 개수 리턴(그림이 10개라서 10을 리턴)
    }

    //ViewPager가 현재 보여질 Item(View객체)를 생성할 필요가 있는 때 자동으로 호출
    //쉽게 말해, 스크롤을 통해 현재 보여져야 하는 View를 만들어냄.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : ViewPager가 보여줄 View의 위치(가장 처음부터 0,1,2,3...)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        View view = null;
        while (flg == false) {
        }
        //새로운 View 객체를 Layoutinflater를 이용해서 생성
        //만들어질 View의 설계는 res폴더>>layout폴더>>viewpater_childview.xml 레이아웃 파일 사용
        view = inflater.inflate(R.layout.viewpager_childview, null);

        //만들어진 View안에 있는 ImageView 객체 참조
        //위에서 inflated 되어 만들어진 view로부터 findViewById()를 해야 하는 것에 주의.
        ImageView img = (ImageView) view.findViewById(R.id.img_viewpager_childimage);

        //ImageView에 현재 position 번째에 해당하는 이미지를 보여주기 위한 작업
        //현재 position에 해당하는 이미지를 setting
        try {
            if (!bitmapArray[position].equals(null)) {
                img.setImageBitmap(bitmapArray[position]);
            }
        }catch (Exception e){
            img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.street));
        }

        //ViewPager에 만들어 낸 View 추가
        container.addView(view);

        //Image가 세팅된 View를 리턴
        return view;
    }

    //화면에 보이지 않은 View는파쾨를 해서 메모리를 관리함.
    //첫번째 파라미터 : ViewPager
    //두번째 파라미터 : 파괴될 View의 인덱스(가장 처음부터 0,1,2,3...)
    //세번째 파라미터 : 파괴될 객체(더 이상 보이지 않은 View 객체)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
        container.removeView((View) object);
    }

    //instantiateItem() 메소드에서 리턴된 Ojbect가 View가  맞는지 확인하는 메소드
    @Override
    public boolean isViewFromObject(View v, Object obj) {
        // TODO Auto-generated method stub
        return v == obj;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Log.d("1", "HttpAsyncTask function");
            return POST(urls[0]);
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
//        Log.d("7",jsonString);
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
        Log.d("7", "Base64 decoding");
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Log.d("8", "BitmapFactory decoding");
        return decodedByte;
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        String json = "";
        try {
            Log.d("2", "POST function");
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("x", x);
            jsonObject.accumulate("y", y);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            Log.d("3", "coordinate : " + json);

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

        bitmapArray = parseJsontoBitmap(result);

        // 11. return result
        return result;
    }

    public Bitmap[] parseJsontoBitmap(String jsonString) {
        Bitmap[] bitmapArr = new Bitmap[4];
        int index = 0;
        String image = null;

        String strArr[] = jsonString.split("~");
        Log.d("0", Integer.toString(strArr.length));


        try {
            for (int i = 0; i < strArr.length; i++) {
                JSONObject jsonObject = new JSONObject(strArr[i]);
                index = jsonObject.getInt("index");
                image = jsonObject.getString("image");
                image = image.replace("\\n", "\n");
                Bitmap bitmapImage = getBitmapFromString(image);
                bitmapArr[index] = bitmapImage;
            }

//            for (int i = 0; i < bitmapArr.length; i++) {
//                Log.d("11", Integer.toString(i));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        flg = true;
        return bitmapArr;
    }
}
