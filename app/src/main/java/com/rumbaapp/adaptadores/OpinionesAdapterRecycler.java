package com.rumbaapp.adaptadores;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.rumbaapp.clasesVO.OpinionesVo;
import com.squareup.picasso.Picasso;
import com.rumbaapp.R;


import java.util.ArrayList;

/**
 * Created by Andres Rangel on 24/01/2016.
 */
public class OpinionesAdapterRecycler extends RecyclerView.Adapter<OpinionesAdapterRecycler.OpinionesViewHolder> {

    Context context;
    ArrayList<OpinionesVo> arrayList;

    public OpinionesAdapterRecycler(Context context, ArrayList<OpinionesVo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public OpinionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_calificacion,parent,false);
        OpinionesViewHolder opinionesViewHolder=new OpinionesViewHolder(view);
        return opinionesViewHolder;
    }

    @Override
    public void onBindViewHolder(OpinionesViewHolder holder, int position) {

        holder.ratiBar.setRating(arrayList.get(position).getCalificacion());
        holder.miNombre.setText(arrayList.get(position).getMiNombre());
        holder.titulo.setText(arrayList.get(position).getTitulo());
        holder.opinion.setText(arrayList.get(position).getOpinion());
        holder.picasso.with(context).load(arrayList.get(position).getImagenPerfil()).into(holder.imagenPerfil);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class OpinionesViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratiBar;
        TextView miNombre,titulo,opinion;
        ImageView imagenPerfil;
        Picasso picasso;

        public OpinionesViewHolder(View view) {
            super(view);

            ratiBar= (RatingBar) view.findViewById(R.id.ratingAdapter);
            miNombre= (TextView) view.findViewById(R.id.miNombre);
            titulo= (TextView) view.findViewById(R.id.titulo);
            opinion= (TextView) view.findViewById(R.id.opinion);
            imagenPerfil= (ImageView) view.findViewById(R.id.imagenPerfil);
            Typeface typeface=Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
            titulo.setTypeface(typeface);
            opinion.setTypeface(typeface);
            miNombre.setTypeface(typeface);


        }
    }
}
