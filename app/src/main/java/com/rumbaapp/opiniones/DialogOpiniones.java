package com.rumbaapp.opiniones;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.rumbaapp.R;
import com.rumbaapp.activities.Bienvenida;
import com.rumbaapp.activities.InformacionStios;
import com.rumbaapp.activities.MainActivity;
import com.rumbaapp.adaptadores.AdapterSitios;
import com.rumbaapp.conexionVolley.CustomRequest;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andres Rangel on 24/01/2016.
 */
public class DialogOpiniones extends Dialog {

    RatingBar ratinBar;
    EditText titulo,opinion;
    Button cancelar,calificar;
    private TextView textTitulo;
    public float calificacion;
    private float califica;
    private String titu,opi,codUsuario;
    private int codSitio;
    private RequestQueue requestQueue;



    public DialogOpiniones(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calificaciones);
        ratinBar = (RatingBar) findViewById(R.id.ratingCalificacion);
        titulo= (EditText) findViewById(R.id.editTitulo);
        opinion= (EditText) findViewById(R.id.editOpinion);
        cancelar= (Button) findViewById(R.id.cancelar);
        calificar= (Button) findViewById(R.id.calificar);
        textTitulo= (TextView) findViewById(R.id.textTitulo);
        requestQueue= Volley.newRequestQueue(getContext());
        Typeface typeface=Typeface.createFromAsset(getContext().getAssets(),"font/Roboto-Regular.ttf");
        textTitulo.setTypeface(typeface);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ratinBar.setRating(InformacionStios.ratinValue);
        calificacion=InformacionStios.ratinValue;
        ratinBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                dismiss();
                InformacionStios.ratingBar.setRating(ratingBar.getRating());

            }
        });
        calificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llenarDatos();
                guardarCalificacion(califica, titu, opi, codSitio, codUsuario);
                Intent intent=new Intent(getContext(),InformacionStios.class);
                getContext().startActivity(intent);
                dismiss();
                InformacionStios.fin.finish();

            }
        });

    }

    private void llenarDatos() {
        califica=calificacion;
        titu=titulo.getText().toString();
        opi=opinion.getText().toString();
        codSitio= AdapterSitios.codSitio;
        codUsuario= MainActivity.codUsuario;

    }

    private void guardarCalificacion(float califica, String titu, String opi, int codSitio, String codUsuario) {

        CustomRequest jsonObjetRequest;
        String url= Bienvenida.ip+"/rumbaapp/agregarCalificaciones.php";
        Map<String,String> params=new HashMap<>();
        params.put("titulo",titu);
        params.put("opinion",opi);
        params.put("cod_sitio", String.valueOf(codSitio));
        params.put("cod_usuario", codUsuario);
        params.put("calificacion", String.valueOf(califica));
        Toast.makeText(getContext(),"se ha registrado su calificacion "+califica,Toast.LENGTH_SHORT).show();
        jsonObjetRequest=new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Error", error.toString());
            }
        });

        requestQueue.add(jsonObjetRequest);
    }



}
