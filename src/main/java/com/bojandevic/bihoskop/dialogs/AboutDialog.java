package com.bojandevic.bihoskop.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.bojandevic.bihoskop.R;

public class AboutDialog extends DialogFragment {
    public AboutDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog);
        alertDialogBuilder.setTitle("Bihoskop");
        alertDialogBuilder.setMessage("Bihoskop je aplikacija za pregled repertoara Multipleksa Palas. Uskoro će imati mogućnost za pregled repertoara i drugih bioskopa iz Bosne i Hercegovine.");
        //null should be your on click listener
        alertDialogBuilder.setPositiveButton("Uredu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        return alertDialogBuilder.create();
    }
}
