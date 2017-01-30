package com.rumbaapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rumbaapp.R;
import com.rumbaapp.adaptadores.AdapterSitios;
import com.rumbaapp.adaptadores.GridViewAdapter;
import com.rumbaapp.clasesVO.GridItemVo;

import java.util.ArrayList;

/**
 * Created by Rangel on 07/06/2016.
 */
public class Gallery extends AppCompatActivity {

    GridView gridView;
    GridViewAdapter gridViewAdapter;
    public static ArrayList<GridItemVo> arrayList;
    public String[] imagenes;
    private String imagen1,imagen2,imagen3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AdapterSitios.nombreSitio);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridView= (GridView) findViewById(R.id.gridView);
        arrayList=new ArrayList<>();
        gridViewAdapter=new GridViewAdapter(getApplicationContext(),arrayList);
        consulta();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int resul = position + 1;
                int total = imagenes.length;
                Intent intent = new Intent(getApplication(), ActitvityDetails.class);
                intent.putExtra("posicion", resul);
                intent.putExtra("total", total);
                startActivity(intent);
            }
        });
    }

    private void consulta() {
        imagen1=InformacionStios.imagen1;
        imagen2=InformacionStios.imagen2;
        imagen3=InformacionStios.imagen3;
        imagenes=new String[]{imagen1,imagen2,imagen3};

        for(int i=0;i<imagenes.length;i++){

            GridItemVo gridItemVo=new GridItemVo();
            gridItemVo.setImage(imagenes[i]);
            gridItemVo.setNombre("imagen "+i);
            arrayList.add(gridItemVo);
            gridView.setAdapter(gridViewAdapter);
        }
    }
}
