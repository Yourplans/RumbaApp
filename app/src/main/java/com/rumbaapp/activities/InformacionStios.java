package com.rumbaapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.rumbaapp.LocationMaps.MapsActivity;
import com.rumbaapp.opiniones.DialogOpiniones;
import com.rumbaapp.opiniones.DividerItemDecoration;
import com.rumbaapp.R;
import com.rumbaapp.adaptadores.AdapterSitios;
import com.rumbaapp.adaptadores.CustomPagerAdapter;
import com.rumbaapp.adaptadores.OpinionesAdapterRecycler;
import com.rumbaapp.clasesVO.OpinionesVo;
import com.rumbaapp.conexionVolley.CustomRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class InformacionStios extends AppCompatActivity {

    int contadorConsulta = 0;
    private String url = Bienvenida.ip + "/rumbaapp/consultarInformacion.php";
    RequestQueue requestQueue;
    TextView descripcion, direccion, telefono, email, horarios, text, textOpiniones;
    ImageView telefonoCall;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private ProgressDialog progressDialog;
    public static String imagen1, imagen2, imagen3;
    private String tel;
    ViewPager viewPager;
    CustomPagerAdapter customPagerAdapter;
    private Timer timer;
    int contador = 0;
    private TimerTask timerTask;
    public static RatingBar ratingBar;
    public static float ratinValue;
    ArrayList<OpinionesVo> arrayListOpiniones;
    RecyclerView listaOpiniones;
    RecyclerView.Adapter adapterOpiniones;
    RecyclerView.LayoutManager layoutManagerOpiniones;
    public static Activity fin;
    public String latitud, longitud;
    private String direccionSitio;
    String nombreSitio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_stios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AdapterSitios.nombreSitio);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fin = this;
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        customPagerAdapter = new CustomPagerAdapter(getApplicationContext());
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        nombreSitio = AdapterSitios.nombreSitio;
        telefonoCall = (ImageView) findViewById(R.id.telefonoCall);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/Roboto-Regular.ttf");
        descripcion = (TextView) findViewById(R.id.descripcion);
        descripcion.setTypeface(typeface);
        direccion = (TextView) findViewById(R.id.direccion);
        direccion.setTypeface(typeface);
        telefono = (TextView) findViewById(R.id.telefono);
        telefono.setTypeface(typeface);
        email = (TextView) findViewById(R.id.email);
        email.setTypeface(typeface);
        horarios = (TextView) findViewById(R.id.horarios);
        horarios.setTypeface(typeface);
        arrayListOpiniones = new ArrayList<>();
        listaOpiniones = (RecyclerView) findViewById(R.id.listaOpiniones);
        adapterOpiniones = new OpinionesAdapterRecycler(getApplicationContext(), arrayListOpiniones);
        layoutManagerOpiniones = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        listaOpiniones.setLayoutManager(layoutManagerOpiniones);
        listaOpiniones.setNestedScrollingEnabled(false);
        listaOpiniones.setHasFixedSize(false);
        listaOpiniones.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.divider));
        listaOpiniones.setAdapter(adapterOpiniones);
        consultaInformacionSitios(nombreSitio);
        consultaOpiniones(AdapterSitios.codSitio);
        text = (TextView) findViewById(R.id.textCalificanos);

        text.setTypeface(typeface);
        textOpiniones = (TextView) findViewById(R.id.textOpiniones);
        textOpiniones.setTypeface(typeface);
        ratingBar = (RatingBar) findViewById(R.id.ratingInfo);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                DialogOpiniones dialogOpiniones = new DialogOpiniones(InformacionStios.this);
                dialogOpiniones.requestWindowFeature(Window.FEATURE_NO_TITLE);
                ratinValue = ratingBar.getRating();
                dialogOpiniones.show();

            }
        });
        telefonoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamar();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);

                intent.putExtra("parametro",0);
                intent.putExtra("longitud",Double.parseDouble(longitud));
                intent.putExtra("latitud",Double.parseDouble(latitud));
                intent.putExtra("imagen",imagen1);
                intent.putExtra("nombre",nombreSitio);
                intent.putExtra("direccion",direccionSitio);
                startActivity(intent);
            }
        });
        timerAnimacion();
    }

    private void consultaInformacionSitios(final String nombreSitio) {
        progressDialog = new ProgressDialog(InformacionStios.this);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Cargando Informacion...");
        progressDialog.show();
        CustomRequest jsonObjetRequest;
        Map<String, String> params = new HashMap<>();
        params.put("nombresitio", nombreSitio);

        jsonObjetRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    jsonArray = response.getJSONArray("informacion_sitios");
                    Log.d("******", response.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        descripcion.setText(jsonObject.getString("descripcion"));
                        direccion.setText("Direccion: " + jsonObject.getString("direccion"));
                        tel = jsonObject.getString("telefono");
                        telefono.setText("Telefono: " + tel);
                        email.setText("Email: " + jsonObject.getString("e_mail"));
                        imagen1 = jsonObject.getString("img");
                        imagen2 = jsonObject.getString("img2");
                        imagen3 = jsonObject.getString("img3");
                        latitud = jsonObject.getString("latitud");
                        longitud = jsonObject.getString("longitud");
                        customPagerAdapter.imagenes = new String[]{imagen1, imagen2, imagen3};
                        viewPager.setAdapter(customPagerAdapter);
                        horarios.setText("Horarios de Atencion:" + "\n" + jsonObject.getString("horarios"));
                        progressDialog.hide();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.hide();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (contadorConsulta <= 1) {
                    contadorConsulta++;
                    consultaInformacionSitios(nombreSitio);
                }

                if (contadorConsulta == 1) {
                    Toast.makeText(getApplication(), "error al cargar intentelo mas tarde " +
                            "o revisa tu conexion a la red", Toast.LENGTH_SHORT).show();
                }
                contadorConsulta++;
                progressDialog.hide();

            }
        });
        requestQueue.add(jsonObjetRequest);


    }


    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void timerAnimacion() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (customPagerAdapter.imagenes != null) {
                            viewPager.setCurrentItem(getItem(+1));
                            contador++;
                            if (contador == customPagerAdapter.imagenes.length) {
                                contador = 0;
                                viewPager.setCurrentItem(0);
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 4000, 4000);

    }

    private void llamar() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + String.valueOf(tel))));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_informacion_stios, menu);
        return true;
    }


    public void consultaOpiniones(int codigoSitio) {

        CustomRequest jsonObjetRequest;
        String urlOpiniones=Bienvenida.ip+"/rumbaapp/consultaOpiniones.php";
        Map<String,String> params=new HashMap<>();
        params.put("cod_sitio", String.valueOf(codigoSitio));
        jsonObjetRequest=new CustomRequest(Request.Method.POST, urlOpiniones, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray=response.getJSONArray("opiniones");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        OpinionesVo opinionesVo=new OpinionesVo();
                        opinionesVo.setOpinion(jsonObject.getString("opinion"));
                        opinionesVo.setTitulo(jsonObject.getString("titulo"));
                        opinionesVo.setCalificacion((float) jsonObject.getDouble("calificacion"));
                        ratinValue= (float) jsonObject.getDouble("calificacion");
                        opinionesVo.setImagenPerfil(jsonObject.getString("foto_usuario"));
                        opinionesVo.setMiNombre(jsonObject.getString("nombre_usuario"));
                        arrayListOpiniones.add(opinionesVo);
                        listaOpiniones.setAdapter(adapterOpiniones);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                consultaOpiniones(AdapterSitios.codSitio);
            }
        });

        requestQueue.add(jsonObjetRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent BackpressedIntent = new Intent();
        BackpressedIntent .setClass(getApplicationContext(),MainActivity.class);
        BackpressedIntent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(BackpressedIntent );
        finish();
    }

}