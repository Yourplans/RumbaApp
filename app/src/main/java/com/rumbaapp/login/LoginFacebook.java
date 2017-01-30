package com.rumbaapp.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.rumbaapp.activities.Bienvenida;
import com.rumbaapp.activities.MainActivity;
import com.rumbaapp.R;
import com.rumbaapp.conexionVolley.CustomRequest;


import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andres Rangel on 03/02/2016.
 */
public class LoginFacebook extends Fragment {

    private CallbackManager callbackManager;


    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private FacebookCallback<LoginResult> callback;
    static String codigo;
    static String nombre;
    Byte contador = 0;
    public LoginButton loginButton;
    public static String imagenPerfilFacebook;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_facebook, container, false);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        progressDialog = new ProgressDialog(getContext());
        requestQueue = Volley.newRequestQueue(getContext());
        loginButton.setReadPermissions("public_profile");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                displayMessage(profile);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        };


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setMessage("obteniendo datos...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    private void displayMessage(Profile profile) {
        if (profile != null) {

            codigo = profile.getId();
            nombre = profile.getName();
            imagenPerfilFacebook = String.valueOf(profile.getProfilePictureUri(500, 500));
           registrarUsuario(codigo, nombre, imagenPerfilFacebook);
            guardarTextCodigo();
            contador++;
            if (contador == 1) {

                Toast.makeText(getActivity(), "Bienvenido a RumbaApp " + nombre, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                Login.finalizar.finish();

            }
        }
    }

    private void guardarTextCodigo() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput("codigo.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(codigo);
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();

    }

    @Override
    public void onResume() {
        super.onResume();
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);

    }

    public void registrarUsuario(String codigo, String personName, String imagenPerfilPlus){


        CustomRequest jsonObjetRequest;
        String url= Bienvenida.ip+"/rumbaapp/agregarUsuarios.php";
        Map<String,String> params=new HashMap<>();
        params.put("id_usuario",codigo);
        params.put("nombre_usuario", personName);
        params.put("foto_usuario", imagenPerfilPlus);

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