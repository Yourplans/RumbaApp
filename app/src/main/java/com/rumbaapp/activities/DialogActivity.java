package com.rumbaapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rumbaapp.R;
import com.rumbaapp.mail.SendMail;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DialogActivity extends AppCompatActivity implements View.OnClickListener {


    //Declaramos las variables gradicas

    com.getbase.floatingactionbutton.FloatingActionButton fabCargarImagen;
    com.getbase.floatingactionbutton.FloatingActionButton fabTomarFoto;
    public final static int RESP_TOMAR_FOTO = 1000;
    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    private ImageView imageView;



    private Button buttonEnviar;
    private EditText editNombre, editDireccion, editTelefono, editCorreo;

     //Identificadores Request code

    private final int NRO_REQUEST =7;
    private final int CAMERAREQUEST=8;
    private File f;
    private String path="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        crearInstancias();
    }

    /**Creamos las instancias de los componentes graficos*/
    private void crearInstancias() {

        editNombre = (EditText) findViewById(R.id.name);
        editDireccion = (EditText) findViewById(R.id.direccion);
        editTelefono = (EditText) findViewById(R.id.telefono);
        editCorreo = (EditText) findViewById(R.id.email);

        imageView = (ImageView) findViewById(R.id.result);



        fabCargarImagen = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.accion_gallery);
        fabCargarImagen.setOnClickListener(this);
        fabTomarFoto = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.accion_foto);
        fabTomarFoto.setOnClickListener(this);
        buttonEnviar= (Button) findViewById(R.id.enviar);
        buttonEnviar.setOnClickListener(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERAREQUEST && resultCode == RESULT_OK) {

            loadImage(path);

        }


        if (requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                getPath(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private File getPhoto( ) throws IOException {

        path="";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName= "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        path = image.getAbsolutePath();
        Log.e("path:::::::>>>>>>",path);



        return  image;
        }



    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.accion_gallery:

                openGallery();


                break;

            case  R.id.accion_foto:

                    if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP ){

                        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA);

                        if (permissionCheck==PackageManager.PERMISSION_GRANTED){
                            iniciarCamara();
                        }else if (permissionCheck==PackageManager.PERMISSION_DENIED){

                            permission();

                        }

                    }else{
                        iniciarCamara();
                    }

                break;

            case R.id.title_dialog:



                break;

            case R.id.enviar:
                Log.e("##########", "******"+path);
                    sendMessage();

                break;
        }


    }

    private void permission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {



            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        10);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.



                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void openGallery() {

        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);

    }


    private void getPath(Intent data) throws IOException {
        // TODO Auto-generated method stub

        Uri uri = data.getData();
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();

        Log.d("", DatabaseUtils.dumpCursorToString(cursor));

        int columnIndex = cursor.getColumnIndex(projection[0]);
        String picturePath = cursor.getString(columnIndex); // returns null
        cursor.close();

        loadImage(picturePath);

    }


    public void loadImage(String path){

        imageView.setImageBitmap(decodeSampledBitmapFromPath(path, 300, 300));

    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
                                                     int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        return bmp;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    private void iniciarCamara() {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = getPhoto();
                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                            "com.rumbaapp",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAMERAREQUEST);

                }


            }
        }



    private void sendMessage() {

        String mensaje =""+obtenerInformacion();
            SendMail mail = new SendMail(getApplicationContext(), "dan.el12@hotmail.com", "Nuevo Sitio", mensaje, path);
            mail.execute();


            mostrarDialog();

           }

    private void mostrarDialog() {

        new AlertDialog.Builder(DialogActivity.this)
                .setIcon(R.drawable.heart)
                .setTitle("Gracias Por Ayudarnos a Mejorar")
                .setMessage("¿Quieres Registrar Otro sitio?")
                .setPositiveButton("Aceptar",null)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        limpiarCampos();
                    }
                })
                .setNegativeButton("Regresar a la diversion", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent retornarMain = new Intent(DialogActivity.this, MainActivity.class);
                        startActivity(retornarMain);

                    }
                }).show();


    }

    private void limpiarCampos() {

        editNombre.setText("");
        editDireccion.setText("");
        editTelefono.setText("");
        editCorreo.setText("");

    }

    private StringBuilder obtenerInformacion() {

        StringBuilder info = new StringBuilder();
        info.append(editNombre.getText());
        info.append("\n");
        info.append(editDireccion.getText());
        info.append("\n");
        info.append(editTelefono.getText());
        info.append("\n");
        info.append(editCorreo.getText());

        return info;

    }



}
