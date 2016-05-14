package com.rumbaapp;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rangel on 13/05/2016.
 */
public class AdapterSitios extends RecyclerView.Adapter<AdapterSitios.AdapterViewHolder> {

    ArrayList <SitiosVO> arrayList;
    Context context;

    public AdapterSitios(ArrayList<SitiosVO> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public AdapterSitios.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_sitios,parent,false);
        AdapterViewHolder adapterViewHolder=new AdapterViewHolder(view);
        return adapterViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterSitios.AdapterViewHolder holder, int position) {

        holder.glide.with(context).load(arrayList.get(position).getImagenSitio()).into(holder.imagenSitios);
        holder.nombre.setText(arrayList.get(position).getNombreSitio());
        holder.direccion.setText(arrayList.get(position).getDireccionSitio());
        holder.ratingBar.setRating(arrayList.get(position).getCalificacionSitio());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        Glide glide;
        ImageView imagenSitios;
        TextView nombre,direccion;
        RatingBar ratingBar;
        public AdapterViewHolder(View view) {
            super(view);

            ratingBar= (RatingBar)view.findViewById(R.id.ratingBar);
            imagenSitios= (ImageView) view.findViewById(R.id.imagenSitio);
            nombre= (TextView) view.findViewById(R.id.txtNombre);
            direccion= (TextView) view.findViewById(R.id.txtDireccion);
            Typeface typeface=Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
            Typeface typefaceTitulo=Typeface.createFromAsset(context.getAssets(),"font/Roboto-Italic.ttf");// asignamos la tipografia roboto de material design a nuestros componentes
            nombre.setTypeface(typefaceTitulo);
            direccion.setTypeface(typeface);
        }
    }
}
