package com.example.fince.fotogratbitirme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class registerScreen extends AppCompatActivity  {

    private AutoCompleteTextView email,passwoord,conPasswoord;
    private TextView baslik,bilgi,slogan;

    //firabase için global değişkenler
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ProgressDialog mDialog;
    private String strEmail,strPasswoord,strConPasswoord,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setBackgroundDrawableResource(R.drawable.backgroundlogin);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        email=(AutoCompleteTextView)findViewById(R.id.userEmail);
        passwoord=(AutoCompleteTextView)findViewById(R.id.userPasswoord);
        conPasswoord=(AutoCompleteTextView)findViewById(R.id.userCofirmPasswoord);
        baslik=(TextView)findViewById(R.id.txtKayit);
        bilgi=(TextView)findViewById(R.id.bilgiTxt);
        slogan=(TextView)findViewById(R.id.sloginText);

        Typeface myCustomTp=Typeface.createFromAsset(getAssets(), "font/acloni.ttf");
        email.setTypeface(myCustomTp);
        passwoord.setTypeface(myCustomTp);
        conPasswoord.setTypeface(myCustomTp);
        baslik.setTypeface(myCustomTp);
        bilgi.setTypeface(myCustomTp);
        slogan.setTypeface(myCustomTp);

        //firebase ön işlemleri
        mAuth=FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

    }
    public void geriDonBasildi(View view)
    {
        Intent intent=new Intent(this,loginScreen.class);
        startActivity(intent);
        finish();
    }
    public void kayitOlBasildi(View view)
    {
        strEmail=email.getText().toString().trim();
        strPasswoord=passwoord.getText().toString().trim();
        strConPasswoord=conPasswoord.getText().toString().trim();
        if(TextUtils.isEmpty(strEmail))
        {
            Toast.makeText(registerScreen.this,"Email girmediniz.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(strPasswoord))
        {
            Toast.makeText(registerScreen.this,"Şifre girmediniz.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(strConPasswoord))
        {
            Toast.makeText(registerScreen.this,"Şifre tekrarını girmediniz.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(strPasswoord.length() < 6)
        {
            Toast.makeText(registerScreen.this,"Şifreniz 6 harften kısa olamaz.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(strPasswoord.equals(strConPasswoord) != true)
        {
            Toast.makeText(registerScreen.this,"Şifreniz ile şifre tekrarı uyuşmamaktadır.", Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.setMessage("Kayıt olunuyor lütfen bekleyiniz...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(strEmail,strPasswoord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    sendEmailVerification();
                    mDialog.dismiss();
                    //kayıt başarılı ise başalngıç bilgilerini veri tabanına kaydetme
                    OnAuth(task.getResult().getUser());


                }
                else
                {
                    Toast.makeText(registerScreen.this,"Kayıt olma işlemi başarısız!", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    private void sendEmailVerification() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast checkMessage=Toast.makeText(registerScreen.this,"Lütfen email doğrulama işlemi için gönderdiğimiz emaili kontrol ediniz.", Toast.LENGTH_SHORT);
                        checkMessage.show();
                        FirebaseAuth.getInstance().signOut();
                        waitAndBack();


                    }
                }
            });
        }
    }

    private void waitAndBack() {
        new CountDownTimer(2000,1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {


                degis();




            }
        }
                .start();
    }

    private void degis() {
        Intent intent=new Intent(this,loginScreen.class);
        startActivity(intent);
        finish();
    }

    private void OnAuth(FirebaseUser user) {
        createAnewUser(user.getUid());
        uid=user.getUid();
    }

    private void createAnewUser(String uid) {
        User user=BuildNewUser();
        mDatabase.child(uid).setValue(user);

    }
    private User BuildNewUser()
    {
        return new User(
                strEmail,new Date().toGMTString()
        );



    }
    @Override
    public void onBackPressed() {
        System.runFinalizersOnExit(true);

    }


}
