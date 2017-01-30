package com.rumbaapp.mail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Daniel on 05/12/2016.
 */
public class SendMail  extends AsyncTask<Void,Void,Void> {


    // declaramos variables que necesitamos
    private Context context;
    private Session session;


    //informacion para enviar
    private String email;
    private String asunto;
    private String message;
    private String pathImage;

    private ProgressDialog progressDialog;


    public SendMail (Context context, String email, String asunto, String message, String pathImage){

        this.context = context;
        this.email = email;
        this.asunto = asunto;
        this.message = message;
        this.pathImage = pathImage;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //Mostrar Progress
        //progressDialog = new ProgressDialog(context);
       // progressDialog = ProgressDialog.show(context,"Enviando Informacion","....",false,false);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        //Quitar progress
       // progressDialog.dismiss();

        //confirmar envio

        Toast.makeText(context,"Informacion enviada\nGracias por ayudarnos a mejorar",Toast.LENGTH_LONG).show();

    }

    @Override
    protected Void doInBackground(Void... params) {

        //Crear las propiedades

        Properties  props = new Properties();

        //Configurar las propiedades para permisos y conexion con gmail

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        crearSesion(props);
        try {
            enviar(agregarContenido());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void enviar(MimeMessage mm) throws MessagingException {

        Transport.send(mm);
    }

    /**Agregamos informacion al mensaje*/
    private MimeMessage agregarContenido() {


        try {
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(Config.EMAIL));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.setSubject(asunto);
            mm.setText(message);

            Log.e("pathpath**",pathImage);

            if (!pathImage.equals("") && pathImage != null) {
                //Agrego Archivo adjunto
                Multipart multiPart = new MimeMultipart();

                BodyPart messageBodyPart = new MimeBodyPart();

                messageBodyPart.setText(message);

                multiPart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();

                DataSource source = new FileDataSource(pathImage);

                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName("Imagen");


                multiPart.addBodyPart(messageBodyPart);

                mm.setContent(multiPart);
            }


            return mm;

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void crearSesion(Properties props) {


        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                    }
                });

    }


}
