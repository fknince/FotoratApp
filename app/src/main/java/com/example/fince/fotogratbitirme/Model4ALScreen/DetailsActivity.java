package com.example.fince.fotogratbitirme.Model4ALScreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fince.fotogratbitirme.R;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private static final String aciklama="aciklama";
    private static final String tarih="tarih";
    private static final String extralar="extralar";
    private static final String resim="resim";

    private TextView txtV_aciklama,txtV_tarih;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle extras=getIntent().getBundleExtra(extralar);
        txtV_aciklama=(TextView)findViewById(R.id.txt_sonuclar);
        txtV_tarih=(TextView)findViewById(R.id.txt_tarih);
        img=(ImageView)findViewById(R.id.imgPhoto);
        txtV_aciklama.setText(extras.getString(aciklama));
        txtV_tarih.setText(extras.getString(tarih));
        Picasso.with(getBaseContext()).load(extras.getString(resim)).into(img);






    }
}
