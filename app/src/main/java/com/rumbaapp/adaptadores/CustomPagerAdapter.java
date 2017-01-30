package com.rumbaapp.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rumbaapp.R;
import com.rumbaapp.activities.Gallery;

/**
 * Created by Rangel on 03/06/2016.
 */
public class CustomPagerAdapter extends PagerAdapter {

    public static String imagenes[];
    Context context;
    LayoutInflater layoutInflater;

    public CustomPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imagenes.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=(LayoutInflater.from(context)).inflate(R.layout.fragment_informacion,container,false);
        ImageView imageView= (ImageView) view.findViewById(R.id.imageInformacion);
        Glide.with(context).load(imagenes[position]).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.cargando)
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Gallery.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
