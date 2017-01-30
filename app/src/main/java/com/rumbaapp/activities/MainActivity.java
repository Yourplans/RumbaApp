package com.rumbaapp.activities;

import android.app.ProgressDialog;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.rumbaapp.LocationMaps.MapsActivity;
import com.rumbaapp.R;
import com.rumbaapp.adaptadores.AdapterSitios;
import com.rumbaapp.clasesVO.SitiosVO;
import com.rumbaapp.conexionVolley.CustomRequest;
import com.rumbaapp.favoritos.FavoritosOpenHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //  declarar Componentes  necesarios para capturar la posicion
    private Intent intentThatCalled;
    public static double latitude;
    public static double longitude;

    public static ArrayList<SitiosVO> arrayListMarkers = new ArrayList();

    private CountDownTimer timer;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<SitiosVO> arrayList;
    RequestQueue requestQueue;
    static String url = Bienvenida.ip + "/rumbaapp/consultaSitios.php";
    SitiosVO sitiosVO;
    public static String codUsuario;
    ArrayList<String> favoritos;
    int contador = 0;
    private CircleImageView imgPeril;
    TextView nombrePerfil, correoPerfil;
    private String nombre, imagen, email;
    private Typeface typeface;
    private Toolbar toolbar;
    private static int categoria = 0;
    private Button maps;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sitios");
        setSupportActionBar(toolbar);
        typeface = Typeface.createFromAsset(getAssets(), "font/Roboto-Regular.ttf");


        //En este metodo procedemos a conocer la ubicacion mas reciente del usuario

        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new AdapterSitios(arrayList, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        imgPeril = (CircleImageView) header.findViewById(R.id.imgPerfil);

        nombrePerfil = (TextView) header.findViewById(R.id.nombrePerfil);
        correoPerfil = (TextView) header.findViewById(R.id.correoPerfil);

        consultarSitios(url, categoria);

        consultaDatosPerfil(codUsuario);

        maps = (Button) findViewById(R.id.maps);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("parametro", 1);
                    intent.putExtra("latitud", latitude);
                    intent.putExtra("longitud", longitude);
                    intent.putExtra("imagen", url);
                    intent.putExtra("nombre", nombre);
                    intent.putExtra("direccion", url);
                    startActivity(intent);

            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();


    }



    private void consultaDatosPerfil(final String codUsuario) {
        String url = Bienvenida.ip + "/rumbaapp/consultaDatosUsuario.php";
        CustomRequest jsonObjetRequest;
        Map<String, String> params = new HashMap<>();
        params.put("codUsuario", String.valueOf(codUsuario));

        jsonObjetRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("datosUsuario");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        nombre = jsonObject.getString("nombre_usuario");
                        imagen = jsonObject.getString("foto_usuario");
                        email = jsonObject.getString("email_usuario");
                        nombrePerfil.setText(nombre);
                        correoPerfil.setText(email);
                        Glide.with(getApplicationContext()).load(imagen).into(imgPeril);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                consultaDatosPerfil(codUsuario);
            }
        });
        requestQueue.add(jsonObjetRequest);
    }

    private void consultarSitios(final String url, final int categoria) {
        Log.d("entro a consultar", url);
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Cargando Informacion...");
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.show();
        CustomRequest jsonObjetRequest;
        Map<String, String> params = new HashMap<>();
        params.put("categorias", String.valueOf(categoria));
        Log.e("######", "Aquiiiii");
        jsonObjetRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("sitio");
                    Log.d("*********", response.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        sitiosVO = new SitiosVO();
                        SitiosVO sitiosMarker = new SitiosVO();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        sitiosVO.setCodSitio(jsonObject.getInt("cod_sitio"));
                        sitiosVO.setImagenSitio(jsonObject.getString("img"));
                        sitiosVO.setNombreSitio(jsonObject.getString("nombre"));
                        sitiosVO.setDireccionSitio(jsonObject.getString("direccion"));
                        sitiosVO.setCalificacionSitio((float) jsonObject.getDouble("calificacion"));
                        arrayList.add(sitiosVO);

                        if (!jsonObject.getString("latitud").equals("0") && jsonObject.getString("latitud") != null) {
                            sitiosMarker.setNombreSitio(jsonObject.getString("nombre"));
                            sitiosMarker.setLatitud(jsonObject.getString("latitud"));
                            sitiosMarker.setLongitud(jsonObject.getString("longitud"));
                            sitiosMarker.setImagenSitio(jsonObject.getString("img"));
                            sitiosMarker.setDireccionSitio(jsonObject.getString("direccion"));
                            arrayListMarkers.add(sitiosMarker);
                        }

                        recyclerView.setAdapter(adapter);
                        progressDialog.hide();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (contador <= 1) {
                    contador++;
                    consultarSitios(url, categoria);
                }

                if (contador == 1) {
                    Toast.makeText(getApplication(), "error al cargar intentelo mas tarde " +
                            "o revisa tu conexion a la red", Toast.LENGTH_SHORT).show();
                }
                contador++;
                Log.e("Errr", "" + error.getMessage() + "  " + error.getLocalizedMessage() + "\n" + error.networkResponse + "\n" + error.getClass());
                arrayList.clear();
                recyclerView.setAdapter(adapter);
                progressDialog.hide();
            }
        });
        // jsonObjetRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjetRequest);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String url1 = Bienvenida.ip + "/rumbaapp/consultaSitiosCategoria.php";
        Log.d("entro ***", url1);
        if (id == R.id.todos) {
            arrayList.clear();
            toolbar.setTitle("Sitios");
            consultarSitios(url, categoria);
        }
        if (id == R.id.rock) {
            arrayList.clear();
            categoria = 2;
            toolbar.setTitle("Rock");
            consultarSitios(url1, categoria);
        } else if (id == R.id.croosover) {
            arrayList.clear();
            categoria = 1;
            toolbar.setTitle("Crosover");
            consultarSitios(url1, categoria);

        } else if (id == R.id.antigua) {
            arrayList.clear();
            toolbar.setTitle("Para Recordar");
            categoria = 3;
            consultarSitios(url1, categoria);
        } else if (id == R.id.favoritos) {
            toolbar.setTitle("Favoritos");
            arrayList.clear();
            consultaSitiosFav();

        } else if (id == R.id.share) {
            shareApp();

        } else if (id == R.id.agregarSitio) {
            intentThatCalled = new Intent(getApplicationContext(), DialogActivity.class);
            startActivity(intentThatCalled);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareApp() {
       /* ShareDialog shareDialog;
        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("RumbaApp")
                .setContentDescription(
                        "Aplicacion donde podras encontrar los mejores sitios para la diversion nocturna")
                .setContentUrl(Uri.parse("https://sitesrumba.000webhostapp.com/downloads/"))
                .build();

        shareDialog.show(linkContent);*/

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "https://sitesrumba.000webhostapp.com/downloads/");
        intent.putExtra(android.content.Intent.EXTRA_STREAM, R.drawable.rock_gesture);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Rumba");
        startActivity(Intent.createChooser(intent, "Comparte Con Tus Amigos"));
    }

    private void consultaSitiosFav() {
        FavoritosOpenHelper favoritosOpenHelper = new FavoritosOpenHelper(getApplicationContext(), "favoritos", null, 1);
        SQLiteDatabase db = favoritosOpenHelper.getReadableDatabase();

        if (db != null) {

            Cursor c = db.rawQuery("select * from favoritos", null);
            favoritos = new ArrayList<>();

            while (c.moveToNext()) {
                favoritos.add(String.valueOf(c.getInt(0)));
            }
            consultaSitiosFavoritos(favoritos);
        }
    }

    private void consultaSitiosFavoritos(final ArrayList<String> favoritos) {
        FavoritosOpenHelper favoritosOpenHelper = new FavoritosOpenHelper(getApplicationContext(), "favoritos", null, 1);
        SQLiteDatabase db = favoritosOpenHelper.getReadableDatabase();

        if (db != null) {
            Cursor c = db.rawQuery("select * from favoritos", null);

            while (c.moveToNext()) {
                SitiosVO sitiosVO = new SitiosVO();
                sitiosVO.setCodSitio(c.getInt(0));
                sitiosVO.setImagenSitio(c.getString(1));
                sitiosVO.setNombreSitio(c.getString(2));
                sitiosVO.setDireccionSitio(c.getString(3));
                sitiosVO.setCalificacionSitio(c.getFloat(4));
                arrayList.add(sitiosVO);
                recyclerView.setAdapter(adapter);
            }
        }
    }


}