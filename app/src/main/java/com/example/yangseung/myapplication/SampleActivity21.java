package com.example.yangseung.myapplication;

/**
 * Created by Sang-Won Yeo on 2016-11-09.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/** GPS 샘플 */
public class SampleActivity21 extends Activity {

    private Button btnShowLocation;
    private TextView txtLat;
    private TextView txtLon;

    // GPSTracker class
    private GpsInfo gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_activity21);

        btnShowLocation = (Button) findViewById(R.id.button);
        txtLat = (TextView) findViewById(R.id.textView);
        txtLon = (TextView) findViewById(R.id.textView2);

        // GPS 정보를 보여주기 위한 이벤트 클래스 등록
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                gps = new GpsInfo(SampleActivity21.this);
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    txtLat.setText(String.valueOf(latitude));
                    txtLon.setText(String.valueOf(longitude));

                    Toast.makeText(
                            getApplicationContext(),
                            "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                            Toast.LENGTH_LONG).show();
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });
    }
}
