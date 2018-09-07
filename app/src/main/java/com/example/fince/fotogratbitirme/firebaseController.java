package com.example.fince.fotogratbitirme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.fince.fotogratbitirme.Model4ALScreen.ListItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by fince on 2.02.2018.
 */

public class firebaseController {
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    firebaseController(AnaEkran anaEkran)
    {
        mStorage= FirebaseStorage.getInstance().getReference();
        mProgressDialog=new ProgressDialog(anaEkran);


    }

    public void imageUpload(String userid,Uri uri)
    {
        mProgressDialog.setMessage("Resminiz yükleniyor...");
        mProgressDialog.show();
        StorageReference filepath=mStorage.child("Photos").child(userid);
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Log.i("firebaseController","Upload done!");
                mProgressDialog.dismiss();

            }
        });

    }

}
