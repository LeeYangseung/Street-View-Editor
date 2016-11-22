package com.example.yangseung.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import static com.example.yangseung.myapplication.R.id.map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {


    Marker selectedMarker;
    View marker_root_view;
    View my_position_view;
    View number_view;
    TextView tv_marker;
    TextView my_marker;
    TextView number_marker;
    private GoogleMap mMap;
    private GpsInfo gps;
    double clicked_latitude;
    double clicekd_longtitude;
    static int flag=0;
    double my_latitude;
    double my_longitude;
    static int search_flag = 0;
    static int now_search_flag = 0;
    double search_marker_x1;
    double search_marker_y1;
    double search_marker_x2;
    double search_marker_y2;
    double[][] distance;
    double[][] connect;
    LatLng[] forkLoad;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        distance = new double[][]{{0,0.144230914,0.134343337, 0.085169365, 0.047493005,0.02496929,0.421942729,0.446387033,0.324417834,0.372598785,0.188569705},
                {0.144231,0.000000,0.043803,0.057121,0.087913,0.192957,0.704825,0.590820,0.368116,0.372576,0.121638},
                {0.134343,0.043803,0.000000,0.006947,0.030470,0.117623,0.423521,0.317663,0.158677,0.160981,0.019624},
                {0.085169,0.057121,0.006947,0.000000,0.008319,0.067439,0.360959,0.286289,0.146078,0.158551,0.025105},
                {0.047493,0.087913,0.030470,0.008319,0.000000,0.028428,0.307697,0.267132,0.147659,0.171236,0.046820},
                {0.024969,0.192957,0.117623,0.067439,0.028428,0.000000,0.242378,0.264368,0.186053,0.229741,0.127591},
                {0.421943,0.704825,0.423521,0.360959,0.307697,0.242378,0.000000,0.035226,0.103980,0.140601,0.296096},
                {0.446387,0.590820,0.317663,0.286289,0.267132,0.264368,0.035226,0.000000,0.030616,0.043916,0.189350},
                {0.324418,0.368116,0.158677,0.146078,0.147659,0.186053,0.103980,0.030616,0.000000,0.003996,0.069789},
                {0.372599,0.372576,0.160981,0.158551,0.171236,0.229741,0.140601,0.043916,0.003996,0.000000,0.068534},
                {0.188570,0.121638,0.019624,0.025105,0.046820,0.127591,0.296096,0.189350,0.069789,0.068534,0.000000}};
        connect = new double[][]{
                {0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0},
                {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0}
        };
        forkLoad = new LatLng[]{new LatLng(37.49858,127.0277113), new LatLng(37.4995817,127.0270488), new LatLng(37.4997389,127.0276917),
                new LatLng(37.4994986,127.0278), new LatLng(37.4992366,127.0279206), new LatLng(37.4987677,127.0281744),
                new LatLng(37.499186,127.029674), new LatLng(37.4997448,127.029474), new LatLng(37.4998955,127.0289416),
                new LatLng(37.5000929,127.0289101), new LatLng(37.4998952,127.0281062)};



        gps = new GpsInfo(MapsActivity.this);
        my_latitude = gps.getLatitude();
        my_longitude = gps.getLongitude();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


        final Button searchBt = (Button) findViewById(R.id.search_btn);
        searchBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(now_search_flag == 0) {
                    search_flag = 1;
                    now_search_flag = 1;
                    searchBt.setText("경로취소");
                }
                else{
                    now_search_flag = 0;
                    searchBt.setText("길찾기");
                    search_flag = 0;

                    mMap.clear();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(map);
                    mapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(my_latitude,my_longitude), 18));//시작화면 좌표 (크기)
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);


        setCustomMarkerView();
        getSampleMarkerItems();

    }


    private void setCustomMarkerView() {


        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        my_position_view = LayoutInflater.from(this).inflate(R.layout.myposition_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
        my_marker = (TextView) my_position_view.findViewById(R.id.my_position_marker);

    }

    private  void setNumberMarkerView(){
        number_view = LayoutInflater.from(this).inflate(R.layout.number_marker_layout, null);
        number_marker = (TextView) number_view.findViewById(R.id.number_marker);
    }

    private void getSampleMarkerItems() {

        ArrayList<MarkerItem> sampleList = new ArrayList();

        sampleList.add(new MarkerItem(37.49858,127.0277113, ""));
        sampleList.add(new MarkerItem(37.4995817,127.0270488, ""));
        sampleList.add(new MarkerItem(37.4997389,127.0276917, ""));
        sampleList.add(new MarkerItem(37.4994986,127.0278, ""));
        sampleList.add(new MarkerItem(37.4992366,127.0279206, ""));
        sampleList.add(new MarkerItem(37.4987677,127.0281744, ""));
        sampleList.add(new MarkerItem(37.499186,127.029674, ""));
        sampleList.add(new MarkerItem(37.4997448,127.029474, ""));
        sampleList.add(new MarkerItem(37.4998955,127.0289416, ""));
        sampleList.add(new MarkerItem(37.5000929,127.0289101, ""));
        sampleList.add(new MarkerItem(37.4998952,127.0281062, ""));


        //내위치 정보 받아오기
        gps = new GpsInfo(MapsActivity.this);
        double my_latitude = gps.getLatitude();
        double my_longitude = gps.getLongitude();
        MarkerItem my_marker = new MarkerItem(my_latitude,my_longitude,"");

        for (MarkerItem markerItem : sampleList) {
            addMarker(markerItem, false); //갈림길 위치 마크
        }
        add_myMarker(my_marker,false); //내위치 더하기


    }


    private Marker addMarker(MarkerItem markerItem, boolean isSelectedMarker) {


        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        String positionName = markerItem.getPositionName();

        //String formatted = NumberFormat.getCurrencyInstance().format(price);

        tv_marker.setText(positionName);
        my_marker.setText(positionName);

        if (isSelectedMarker) {
            tv_marker.setBackgroundResource(R.drawable.where);
            tv_marker.setTextColor(Color.WHITE);
        } else {
            tv_marker.setBackgroundResource(R.drawable.where);
            tv_marker.setTextColor(Color.BLACK);
        }


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(positionName);
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));


        return mMap.addMarker(markerOptions);


    }


    private Marker add_myMarker(MarkerItem markerItem, boolean isSelectedMarker) {


        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        String positionName = markerItem.getPositionName();

        //String formatted = NumberFormat.getCurrencyInstance().format(price);

        my_marker.setText(positionName);


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(positionName);
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, my_position_view)));


        return mMap.addMarker(markerOptions);


    }

    private Marker add_number(MarkerItem markerItem, boolean isSelectedMarker) {


        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        String positionName = markerItem.getPositionName();

        number_marker.setText(positionName);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(positionName);
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, number_view)));

        return mMap.addMarker(markerOptions);


    }


    // View를 Bitmap으로 변환
    private Bitmap createDrawableFromView(Context context, View view) {


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);


        return bitmap;
    }


    private Marker addMarker(Marker marker, boolean isSelectedMarker) {
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        String positionName = marker.getTitle();
        MarkerItem temp = new MarkerItem(lat, lon, positionName);
        return addMarker(temp, isSelectedMarker);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {


        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        mMap.animateCamera(center);


        if(search_flag == 1){
            search_marker_x1 =marker.getPosition().latitude;
            search_marker_y1=marker.getPosition().longitude;
            search_flag++;
        }
        else  if(search_flag == 2){
            search_marker_x2 =marker.getPosition().latitude;
            search_marker_y2=marker.getPosition().longitude;
            search_flag++;
            PolylineOptions polylineOptions = new PolylineOptions();
            super.onResume();  // Always call the superclass method first
            ArrayList<LatLng> arrayPoints = new ArrayList();
            arrayPoints.add(new LatLng(search_marker_x1, search_marker_y1));
            LatLng nowLatLng = new LatLng(search_marker_x1, search_marker_y1);
            LatLng arriveLatLng = new LatLng(search_marker_x2, search_marker_y2);
            int nowFork = -1;
            int arriveFork = -1;
            for(int i = 0; i < 11; i++){
                if(nowLatLng.equals(forkLoad[i])){
                    nowFork = i;
                }
                if(arriveLatLng.equals(forkLoad[i])){
                    arriveFork = i;
                }
            }

            while(nowFork != arriveFork){
                if(nowFork == -1 || arriveFork == -1){
                    break;
                }
                double min = Double.MAX_VALUE;
                int minIndex = -1;
                for (int i = 0; i < 11; i++){
                    if(connect[nowFork][i] == 1) {
                        double nowValue = distance[nowFork][i] + distance[i][arriveFork];
                        if(min > nowValue){
                            minIndex = i;
                            min = nowValue;
                        }
                    }
                }
                arrayPoints.add(forkLoad[minIndex]);
                nowFork = minIndex;
            }



            polylineOptions.addAll(arrayPoints);
            mMap.addPolyline(polylineOptions);
            search_flag = 0;
        }
        else if(search_flag == 0)
            changeSelectedMarker(marker);


        return true;

    }


    private void changeSelectedMarker(Marker marker) {
        // 선택했던 마커 되돌리기
        if (selectedMarker != null) {
            addMarker(selectedMarker, false);
            selectedMarker.remove();

        }

        ArrayList<MarkerItem> numberList = new ArrayList();

        // 선택한 마커 표시
        if (marker != null) {
            clicked_latitude =marker.getPosition().latitude;
            clicekd_longtitude = marker.getPosition().longitude;
            setNumberMarkerView();

            if(flag==0 ){
                flag=1;
//                numberList.add(new MarkerItem(37.281893, 127.043478, "1"));
//                numberList.add(new MarkerItem(37.281927, 127.043610, "2"));
//                numberList.add(new MarkerItem(37.281743, 127.043542, "3"));

                for (MarkerItem item : numberList) {
                    add_number(item, false); //갈림길 위치 마크
                }

            }
            else if (flag==1) {
                flag=0;
                mMap.clear();
                my_latitude=clicked_latitude;
                my_longitude=clicekd_longtitude;
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(map);
                mapFragment.getMapAsync(this);
                Intent intent = new Intent(MapsActivity.this, SplitStreetViewPanoramaAndMapDemoActivity.class);
                intent.putExtra("clicked_latitude",clicked_latitude);
                intent.putExtra("clicked_longtitude",clicekd_longtitude);
                startActivity(intent);

            }
        }




    }


    @Override
    public void onMapClick(LatLng latLng) {
        changeSelectedMarker(null);
    }


}