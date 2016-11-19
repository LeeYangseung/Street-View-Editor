/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.yangseung.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * This shows how to create a simple activity with streetview and a map
 */
public class SplitStreetViewPanoramaAndMapDemoActivity extends AppCompatActivity {

    private static final LatLng SYDNEY = new LatLng(37.4958471,127.033087);
    ViewPager pager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.split_street_view_panorama_and_map_demo);



        SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment)
                        getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                new OnStreetViewPanoramaReadyCallback() {
                    @Override
                    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {

                        if (savedInstanceState == null) {
                            panorama.setPosition(SYDNEY);
                        }
                    }
                });

        final CustomAdapter adapter= new CustomAdapter(getLayoutInflater(), 10);
        pager= (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);



        Button btn1 = (Button) findViewById(R.id.button_upload);
        btn1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(SplitStreetViewPanoramaAndMapDemoActivity.this, Post.class);
                intent.putExtra("now",String.valueOf(pager.getCurrentItem()));
                startActivity(intent);
            }
        });

        Button btn2 = (Button) findViewById(R.id.button_evaluate);
        btn2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(SplitStreetViewPanoramaAndMapDemoActivity.this, Evaluate.class);
                startActivity(intent);
            }
        });
    }

}