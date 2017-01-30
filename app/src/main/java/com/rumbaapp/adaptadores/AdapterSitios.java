package com.rumbaapp.adaptadores;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rumbaapp.activities.InformacionStios;
import com.rumbaapp.R;
import com.rumbaapp.clasesVO.SitiosVO;
import com.rumbaapp.favoritos.FavoritosOpenHelper;

import java.util.ArrayList;

/**
 * Created by Rangel on 13/05/2016.
 */
public class AdapterSitios extends RecyclerView.Adapter<AdapterSitios.AdapterViewHolder> {

    ArrayList <SitiosVO> arrayList;
    Context context;
    static int valorFavorito;
    public static String nombreSitio;
    public static int codSitio=0;

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
    public void onBindViewHolder(final AdapterSitios.AdapterViewHolder holder, int position) {

        holder.glide.with(context).load(arrayList.get(position).getImagenSitio()).into(holder.imagenSitios);
        holder.nombre.setText(arrayList.get(position).getNombreSitio());
        holder.direccion.setText(arrayList.get(position).getDireccionSitio());
        holder.ratingBar.setRating(arrayList.get(position).getCalificacionSitio());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Glide glide;
        ImageView imagenSitios;
        TextView nombre,direccion;
        RatingBar ratingBar;
        ImageView favorito;
        public AdapterViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);

            ratingBar= (RatingBar)view.findViewById(R.id.ratingBar);
            imagenSitios= (ImageView) view.findViewById(R.id.imagenSitio);
            nombre= (TextView) view.findViewById(R.id.txtNombre);
            direccion= (TextView) view.findViewById(R.id.txtDireccion);
            favorito= (ImageView) view.findViewById(R.id.btnFavorito);
            Typeface typeface=Typeface.createFromAsset(context.getAssets(),"font/Roboto-Regular.ttf");
            Typeface typefaceTitulo=Typeface.createFromAsset(context.getAssets(), "font/Roboto-Italic.ttf");// asignamos la tipografia roboto de material design a nuestros componentes
            nombre.setTypeface(typefaceTitulo);
            direccion.setTypeface(typeface);
            ImageButton imageButton= (ImageButton) view.findViewById(R.id.music_menu);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(v);
                }
            });

        }

        public void showPopup(View v) {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            validarRegistro();
            if (valorFavorito==1){
                inflater.inflate(R.menu.main2, popup.getMenu());
            }else {
                inflater.inflate(R.menu.main, popup.getMenu());
            }
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    if (valorFavorito == 1) {
                        eliminarFavorito();
                    }else {
                        agregarFavoritos();
                    }

                    return true;
                }
            });
            popup.show();
        }

        private void eliminarFavorito() {
            FavoritosOpenHelper favoritosOpenHelper=new FavoritosOpenHelper(context,"favoritos",null,1);
            SQLiteDatabase db=favoritosOpenHelper.getWritableDatabase();

            if (db!=null){
                long i=db.delete("favoritos", " codigo_sitio=" + arrayList.get(getAdapterPosition()).getCodSitio(), null);
                if (i>0){
                    Toast.makeText(context,"se elimino el sitio de tu lista",Toast.LENGTH_SHORT).show();
                    valorFavorito=0;
                }else {
                    Toast.makeText(context,"fallo al eliminar el sitio",Toast.LENGTH_SHORT).show();
                    valorFavorito=1;
                }
            }

        }

        private void validarRegistro() {
            FavoritosOpenHelper favoritosOpenHelper=new FavoritosOpenHelper(context,"favoritos",null,1);
            SQLiteDatabase db=favoritosOpenHelper.getReadableDatabase();
            if (db!=null){
                Cursor c=db.rawQuery("select * from favoritos where codigo_sitio="+arrayList.get(getAdapterPosition()).getCodSitio(),null);
               if (c.moveToNext()){
                    valorFavorito=1;
                }else  valorFavorito=0;
            }
        }

        private void agregarFavoritos() {
            FavoritosOpenHelper favoritosOpenHelper=new FavoritosOpenHelper(context,"favoritos",null,1);
            SQLiteDatabase db=favoritosOpenHelper.getWritableDatabase();

            if (db!=null){
                ContentValues contentValues=new ContentValues();
                contentValues.put("codigo_sitio",arrayList.get(getAdapterPosition()).getCodSitio());
                contentValues.put("imagen_sitio",arrayList.get(getAdapterPosition()).getImagenSitio());
                contentValues.put("nombre_sitio",arrayList.get(getAdapterPosition()).getNombreSitio());
                contentValues.put("direccion_sitio",arrayList.get(getAdapterPosition()).getDireccionSitio());
                contentValues.put("calificacion_sitio",arrayList.get(getAdapterPosition()).getCalificacionSitio());
                long i=db.insert("favoritos",null,contentValues);
                if (i>0){
                    Toast.makeText(context, "Se ha agregado a tu lista " + arrayList.get(getAdapterPosition()).getNombreSitio(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"fallo al agregar el sitio",Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onClick(View v) {

            SitiosVO sitiosVO=arrayList.get(getAdapterPosition());
            Intent intent=new Intent(v.getContext(),InformacionStios.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
            nombreSitio=sitiosVO.getNombreSitio();
            codSitio=sitiosVO.getCodSitio();
            Toast.makeText(v.getContext()," "+nombreSitio,Toast.LENGTH_SHORT).show();


        }
    }
}
