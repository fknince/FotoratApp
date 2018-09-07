package com.example.fince.fotogratbitirme;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.fince.fotogratbitirme.Model4ALScreen.ListItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by fince on 26.02.2018.
 */

public class firebaseGetSet {

    static DatabaseReference mDatabase;

    public ArrayList<ListItem> getList() {
        return list;
    }

    aratmaListeleEkrani ae;

    public boolean isDone=false;

    static ArrayList<ListItem> list=new ArrayList<>();
    Uri downloadUrl=null;

    public firebaseGetSet()
    {

    }
    public firebaseGetSet(aratmaListeleEkrani ae)
    {
        this.ae=ae;
    }

    public void addSearchingInformation(ListItem item,String path)
    {

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Searches");
        if(item != null)
        {
            mDatabase.child(path).push().setValue(item);

        }

    }
    public void addSearchingImage(Bitmap image,aratmaEkrani ae,String path,String date)
    {



        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference mStorage= FirebaseStorage.getInstance().getReference();
        StorageReference filepath=mStorage.child("allphotos").child(path).child(date);
        UploadTask uploadTask = filepath.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                downloadUrl = taskSnapshot.getDownloadUrl();
                ae.addInformation(downloadUrl);


            }
        });




    }
    private void fetchData(DataSnapshot dataSnapshot)
    {

        int i=0;
        list.clear();
        for(DataSnapshot ds:dataSnapshot.getChildren())
        {

            ListItem item=  ds.getValue(ListItem.class);
            list.add(item);
        }
        Log.i("Fetchdata",list.size()+"");


        /*ListItem item=new ListItem();
        for(DataSnapshot ds:dataSnapshot.getChildren())
        {
            Log.i("i",i+" ");
            switch (i)
            {
                case 0:
                {
                    item.setImage_url(ds.getValue().toString());
                    break;
                }
                case 1:
                {
                    item.setSubTitle(ds.getValue().toString());
                    break;
                }
                case 2:
                {
                    item.setTahminler(ds.getValue().toString());
                    break;
                }
                case 3:
                {
                    item.setTitle(ds.getValue().toString());
                    list.add(item);
                    break;
                }
            }
            i++;



        }*/
    }

    public firebaseGetTask retrieve(String path)
    {


        isDone=false;
        firebaseGetTask fgt=new firebaseGetTask();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Searches");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //fetchData(dataSnapshot);
                if(dataSnapshot.getKey().equals(path))
                    fgt.execute(dataSnapshot);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                //fetchData(dataSnapshot);
                fgt.execute(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                fgt.execute();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("Status", fgt.getStatus().toString());

        return fgt;




    }
    public class firebaseGetTask extends AsyncTask<DataSnapshot,String,String>
    {


        @Override
        protected String doInBackground(DataSnapshot... dataSnapshots) {
            int i=0;
            list.clear();
            for(DataSnapshot ds:dataSnapshots[0].getChildren())
            {

                ListItem item=  ds.getValue(ListItem.class);
                //Log.i("GetTask",item.getTitle());
                //Log.i("KEY",ds.getKey());
                list.add(item);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if(ae != null)
                ae.updateList(list);
        }
    }

    // get key with title
    public class firebaseGetKey extends AsyncTask<DataSnapshot,String,String>
    {


        @Override
        protected String doInBackground(DataSnapshot... dataSnapshots) {
            int i=0;
            list.clear();
            for(DataSnapshot ds:dataSnapshots[0].getChildren())
            {

                ListItem item=  ds.getValue(ListItem.class);
                Log.i("GetTask",item.getTitle());
                Log.i("KEY",ds.getKey());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if(ae != null)
                ae.updateList(list);
        }
    }

    public firebaseGetKey getKey(String path,String title)
    {

        isDone=false;
        firebaseGetKey fgk=new firebaseGetKey();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Searches");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //fetchData(dataSnapshot);
                fgk.execute(dataSnapshot);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                //fetchData(dataSnapshot);
                fgk.execute(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                fgk.execute();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return fgk;




    }



}
