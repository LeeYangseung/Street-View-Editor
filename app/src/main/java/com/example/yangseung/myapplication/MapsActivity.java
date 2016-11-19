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

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {


    Marker selectedMarker;
    View marker_root_view;
    View my_position_view;
    TextView tv_marker;
    TextView my_marker;
    private GoogleMap mMap;
    private GpsInfo gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        gps = new GpsInfo(MapsActivity.this);
        double my_latitude = gps.getLatitude();
        double my_longitude = gps.getLongitude();
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(my_latitude, my_longitude), 18));//시작화면 좌표 (크기)
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


    private void getSampleMarkerItems() {

        ArrayList<MarkerItem> sampleList = new ArrayList();
        sampleList.add(new MarkerItem(37.281855, 127.043515, ""));
        sampleList.add(new MarkerItem(37.281203, 127.043571, ""));
        sampleList.add(new MarkerItem(37.282388, 127.043503, ""));
        sampleList.add(new MarkerItem(37.282361, 127.043084, ""));
        sampleList.add(new MarkerItem(37.282391, 127.043899, ""));
        sampleList.add(new MarkerItem(37.282686, 127.042821, ""));
        sampleList.add(new MarkerItem(37.282712, 127.043486, ""));
        sampleList.add(new MarkerItem(37.282729, 127.044157, ""));
        sampleList.add(new MarkerItem(37.281518, 127.043795, ""));
        sampleList.add(new MarkerItem(37.281842, 127.043794, ""));
        sampleList.add(new MarkerItem(37.281236, 127.043810, ""));
        sampleList.add(new MarkerItem(37.282149, 127.044084, ""));
        sampleList.add(new MarkerItem(37.281270, 127.044755, ""));
        sampleList.add(new MarkerItem(37.282519, 127.044037, ""));


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


        changeSelectedMarker(marker);


        return true;
    }


    private void changeSelectedMarker(Marker marker) {
        // 선택했던 마커 되돌리기
        if (selectedMarker != null) {
            addMarker(selectedMarker, false);
            selectedMarker.remove();

        }

        // 선택한 마커 표시
        if (marker != null) {
            selectedMarker = addMarker(marker, true);
            //marker.remove();
            Intent intent = new Intent(MapsActivity.this, SplitStreetViewPanoramaAndMapDemoActivity.class);
            startActivity(intent);
        }


    }


    @Override
    public void onMapClick(LatLng latLng) {
        changeSelectedMarker(null);
    }


}
