package com.example.fince.fotogratbitirme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fince on 17.02.2018.
 */

@SuppressLint("ValidFragment")
public class openDialog extends AppCompatDialogFragment{

    View openingView;
    public openDialog(View openingView)
    {
        this.openingView=openingView;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        builder.setView(openingView).setPositiveButton("Kapat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup vGroup = (ViewGroup) openingView.getParent();
                vGroup.removeView(openingView);
                builder.create().cancel();
                builder.create().dismiss();

            }
        });
        return builder.create();


    }

}
