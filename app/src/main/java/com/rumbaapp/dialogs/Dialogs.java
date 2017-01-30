package com.rumbaapp.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.rumbaapp.R;
import com.rumbaapp.activities.DialogActivity;

/**
 * Created by Daniel on 20/01/2017.
 */
public class Dialogs {

    private final String ACTION_POSITIVE;
    private String ACTION_NEUTRAL;
    Context context;
    String title;
    String message;
    String buttonPositive;
    String buttonNegative;
    String buttonNeutral;

    public Dialogs(Context context,String title, String message,String buttonPositive, String buttonNegative, String buttonNeutral,String ACTION_POSITIVE,String ACTION_NEUTRAL){

       this.context =context;
        this.title=title;
        this.message =message;
        this.buttonNegative=buttonNegative;
        this.buttonPositive=buttonPositive;
        this.buttonNeutral=buttonNeutral;
        this.ACTION_NEUTRAL=ACTION_NEUTRAL;
        this.ACTION_POSITIVE=ACTION_POSITIVE;
    }

    public void showSettingsAlert(View gps) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle(title);

        alertDialog.setMessage(message);

        alertDialog.setPositiveButton(buttonPositive, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (ACTION_POSITIVE != null) {
                    Intent intent = new Intent(ACTION_POSITIVE);
                    context.startActivity(intent);
                }
            }
        });

        if (buttonNegative!=null && !buttonNegative.equals("")) {

            alertDialog.setNegativeButton(buttonNegative, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        }

        if (buttonNeutral!=null && buttonNeutral.equals("")) {

            alertDialog.setNegativeButton(buttonNegative, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (ACTION_NEUTRAL!=null) {
                        Intent intent = new Intent(ACTION_NEUTRAL);
                        context.startActivity(intent);
                    }

                }
            });

        }
        if (gps!=null){
        alertDialog.setView(gps);}
        alertDialog.show();
    }


}
