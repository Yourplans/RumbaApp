package com.rumbaapp.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rumbaapp.R;
import com.rumbaapp.clasesVO.GridItemVo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rangel on 07/06/2016.
 */
public class GridViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<GridItemVo> arrayList=new ArrayList<>();
    public static String[] imagenes;

    public GridViewAdapter(Context context, ArrayList<GridItemVo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrayList.get(position).getCod();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.grid_item_adaptert,parent,false);
        ImageView imageView= (ImageView) view.findViewById(R.id.grid_item_image);
        GridItemVo gridItemVo=arrayList.get(position);
        Picasso.with(context).load(gridItemVo.getImage()).centerCrop()
                .resize(500,500)
                .into(imageView);
        return view;

    }
}
