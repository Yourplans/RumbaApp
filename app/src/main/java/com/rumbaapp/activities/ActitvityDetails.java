package com.rumbaapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rumbaapp.R;
import com.rumbaapp.adaptadores.DetailsAdapter;
import com.rumbaapp.clasesVO.GridItemVo;

import java.util.ArrayList;

public class ActitvityDetails extends AppCompatActivity {

    int total,posicion;
    ViewPager viewPager;

    private DetailsAdapter mSectionsPagerAdapter;
    private ArrayList<GridItemVo> arrayList;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        posicion = getIntent().getIntExtra("posicion", 1);
        total = getIntent().getIntExtra("total", 2);
        toolbar.setTitle("imagen " + posicion + "/" + total);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        arrayList = new ArrayList<>();
        arrayList = Gallery.arrayList;
        viewPager = (ViewPager) findViewById(R.id.view);
        mSectionsPagerAdapter = new DetailsAdapter(getSupportFragmentManager(),getApplicationContext(), arrayList);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setCurrentItem(posicion - 1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pos;
                if (position + 1 == total) {
                    toolbar.setTitle("imagen " + total + "/" + total);
                } else {
                    pos = position + 1;
                    toolbar.setTitle(arrayList.get(pos).getNombre() + "/" + total);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}