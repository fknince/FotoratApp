package com.example.fince.fotogratbitirme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.media.Image;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class loginScreen extends AppCompatActivity {

    //Facebook api global değişkenleri
    private LoginButton loginButton;
    private Profile profile;
    private FacebookController fc;

    //
    private TextView giris,bilgi,slogan,uyeOl;
    private AutoCompleteTextView email,sifre;
    private Button btnGiris,btnUyeOl,btnSifremiUnuttum;

    //firebase değişkenleri
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser mUser;
    public static final String TAG="Login";
    String strEmail,strPassword;
    ProgressDialog mDialog;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fc.getCallbackManager().onActivityResult(requestCode,resultCode,data);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_screen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setBackgroundDrawableResource(R.drawable.backgroundlogin);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);





        uyeOl=(TextView)findViewById(R.id.uyeOl);
        btnUyeOl=(Button)findViewById(R.id.uyeolButton);
        btnGiris=(Button)findViewById(R.id.girisButton);
        btnSifremiUnuttum=(Button)findViewById(R.id.sifremiUnuttumBtn);

        email=(AutoCompleteTextView) findViewById(R.id.userEmail);
        sifre=(AutoCompleteTextView)findViewById(R.id.userPasswoord);

        Typeface myCustomTp=Typeface.createFromAsset(getAssets(), "font/lobster.ttf");



       // email.setTypeface(myCustomTp);
        //sifre.setTypeface(myCustomTp);
        uyeOl.setTypeface(myCustomTp);
         btnUyeOl.setTypeface(myCustomTp);
        //btnGiris.setTypeface(myCustomTp);
        btnSifremiUnuttum.setTypeface(myCustomTp);





        //facebook api işlemleri
        loginButton=(LoginButton)findViewById(R.id.login_button);
        fc=new FacebookController(loginButton,getApplicationContext(),this);

        //firebase işlemleri
        mAuth=FirebaseAuth.getInstance();
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mUser != null)
                {
                    Intent intent=new Intent(loginScreen.this,AnaEkran.class);
                    intent.putExtra("facebook_connect",false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    Log.d(TAG,"AuthStateChange:LogOut");
                }
            }
        };
        mDialog=new ProgressDialog(this);


    }
    /*void printKeyHash()
    {
        try{
            PackageInfo info=getPackageManager().getPackageInfo("com.example.fince.fotogratbitirme", PackageManager.GET_SIGNATURES);
            for(Signature s:info.signatures)
            {
                MessageDigest md= MessageDigest.getInstance("SHA");
                md.update(s.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.profile=fc.getCurrentProfile();
        fc.profileChanged(this.profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(fc != null)
            fc.stopActions();
        if(mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);

    }
    public void changeActivity(Intent intent)
    {
        startActivity(intent);
        finish();
    }
    public void sifremiUnuttum(View view)
    {

    }
    public void uyeOlBasildi(View view)
    {
        Intent intent=new Intent(this,registerScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        System.runFinalizersOnExit(true);

    }
    public void girisBasildi(View view)
    {
        strEmail=email.getText().toString().trim();
        strPassword=sifre.getText().toString().trim();
        if(TextUtils.isEmpty(strEmail))
        {
            Toast.makeText(loginScreen.this,"Lütfen bir email adresi giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(strPassword.length()<6)
        {
            Toast.makeText(loginScreen.this,"Lütfen 6 karakterli şifrenizi giriniz.", Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.setMessage("Giriş yapılıyor lütfen bekleyiniz...");
        mDialog.setIndeterminate(true);
        mDialog.show();
        mAuth.signInWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                {

                    mDialog.dismiss();
                    Toast.makeText(loginScreen.this,"Bağlanma işlemi başarısız.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mDialog.dismiss();
                    startActivity(new Intent(loginScreen.this,AnaEkran.class));

                }
            }

        });
        mAuth.signInWithEmailAndPassword(strEmail,strPassword).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("HATA",e.getMessage());
            }
        });


    }
}
