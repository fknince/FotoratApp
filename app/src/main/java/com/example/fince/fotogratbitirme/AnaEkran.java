package com.example.fince.fotogratbitirme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.MessageFormat;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnaEkran extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    boolean fb;
    private CircleImageView profileIV;
    private String link;
    private NavigationView nvDrawer;
    private TextView nameSurname,eMail;
    public static final int IMAGE_GALLERY_REQUEST = 20;
    private ProgressDialog mProgressDialog;

    //camera
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private String mImageFileLocation = "";
    private static final int PERMISSION_REQUEST_CODE = 1;


    //path for firebase
    String path;

    //firebase
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat);
        setContentView(R.layout.header);
        setContentView(R.layout.activity_ana_ekran);
        mProgressDialog=new ProgressDialog(this);
        Intent intent=getIntent();
        fb=intent.getBooleanExtra("facebook_connect",false);
        nvDrawer=(NavigationView)findViewById(R.id.nv);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(fb)
        {
            FacebookSdk.sdkInitialize(this);

            //facebook bilgilerinin gösterilmesi
            link=intent.getStringExtra("image");
            String name=intent.getStringExtra("name");
            View hView =  nvDrawer.getHeaderView(0);
            profileIV=(CircleImageView)hView.findViewById(R.id.profileImage);
            Picasso.with(this).load(link).into(profileIV);
            nameSurname=(TextView)hView.findViewById(R.id.txtNameSurname);
            eMail=(TextView)hView.findViewById(R.id.txtEmail);
            nameSurname.setText(name);
            eMail.setText("");
            path=name;



        }
        else
        {
            mAuth=FirebaseAuth.getInstance();
            View hView =  nvDrawer.getHeaderView(0);
            FirebaseUser mUser=FirebaseAuth.getInstance().getCurrentUser();
            String email=mUser.getEmail();
            nameSurname=(TextView)hView.findViewById(R.id.txtNameSurname);
            eMail=(TextView)hView.findViewById(R.id.txtEmail);
            nameSurname.setText(email);
            eMail.setText("");
            path=mUser.getUid();
            getProfileImageFromFirebase();

        }
        //profil resmine pop-up menü ekleme
        if(mAuth != null) {
            View hView =  nvDrawer.getHeaderView(0);
            profileIV=(CircleImageView)hView.findViewById(R.id.profileImage);
            profileIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu();
                }
            });
        }
        mDrawerLayout=(DrawerLayout)findViewById(R.id.dl);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        setupDrawerContent(nvDrawer);
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }
        //aratma ekranı açık halde gelme

        try {
            android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
            android.support.v4.app.Fragment myFragment= (android.support.v4.app.Fragment) aratmaEkrani.class.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fl, myFragment).commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);






    }

    private void popupMenu() {
        //popup menü işlemi
        PopupMenu popupMenu=new PopupMenu(AnaEkran.this,profileIV);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Kamera")) {
                    //kameradan resim al
                    takePictureFromCam();
                }
                else
                {
                    //galeriden resim al
                    takePictureFormGallery();
                }

                return true;
            }
        });
        popupMenu.show();

    }

    private void takePictureFormGallery() {
        // invoke the image gallery using an implict intent.
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        // where do we want to find the data?
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        // finally, get a URI representation
        Uri data = Uri.parse(pictureDirectoryPath);

        // set the data and type.  Get all image types.
        photoPickerIntent.setDataAndType(data, "image/*");

        // we will invoke this activity, and get something back from it.
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);

    }

    private void takePictureFromCam() {
        Intent intent=new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile=null;
        try
        {
            photoFile=createImageFile();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if(photoFile != null)
        {
            String authorities=getApplicationContext().getPackageName()+".fileprovider";
            Uri imageUri=FileProvider.getUriForFile(this,authorities,photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            startActivityForResult(intent,ACTIVITY_START_CAMERA_APP);
        }
        else
            Log.i("HATA","photo file boş");


    }
    File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName,".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();

        return image;

    }

    public void cikis()
    {

        if(mAuth!= null)
            mAuth.signOut();
        else
            LoginManager.getInstance().logOut();

        Intent login=new Intent(this.getBaseContext(),loginScreen.class);
        startActivity(login);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        /*if(!fb)
            mAuth.signOut();
        else
            LoginManager.getInstance().logOut();*/

    }



    public void selectItemDrawer(MenuItem menuItem)
    {
        android.support.v4.app.Fragment myFragment=null;
        Class fragmenClass = aratmaEkrani.class;



        switch(menuItem.getItemId())
        {

            case R.id.arama:
            {
                fragmenClass=aratmaEkrani.class;
                break;
            }
            case R.id.aramaGec:
            {
                fragmenClass=aratmaListeleEkrani.class;


                break;
            }
            case R.id.cikis:
            {
                cikis();
                break;
            }
            default:
            {

                break;
            }


        }
        try
        {

            myFragment= (android.support.v4.app.Fragment) fragmenClass.newInstance();
            Bundle bundle=new Bundle();
            if(path != null)
                bundle.putString("path",path);
            myFragment.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fl, myFragment).commit();
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
            mDrawerLayout.closeDrawers();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK)
        {
            if(requestCode == ACTIVITY_START_CAMERA_APP ) {

                setReducedImageSize();

            }
            if(requestCode == IMAGE_GALLERY_REQUEST )
            {
                Uri imageUri=data.getData();
                InputStream inputStream;
                try {
                    inputStream=getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
                    profileIV.setImageBitmap(bitmap);
                    uploadBitmapToFirebase();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    void setReducedImageSize() {
        Bitmap rotated=rotateBitmapOrientation(mImageFileLocation);
        profileIV.setImageBitmap(rotated);
        uploadBitmapToFirebase();


    }
    public Bitmap rotateBitmapOrientation(String photoFilePath) {

        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }
    void uploadBitmapToFirebase()
    {
        mProgressDialog.setMessage("Resminiz yükleniyor...");
        mProgressDialog.show();
        profileIV.setDrawingCacheEnabled(true);
        profileIV.buildDrawingCache();
        Bitmap bitmap = profileIV.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference mStorage= FirebaseStorage.getInstance().getReference();
        StorageReference filepath=mStorage.child("imagePhotos").child(mAuth.getUid());
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
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mProgressDialog.dismiss();

            }
        });
    }
    void getProfileImageFromFirebase()
    {
        StorageReference storageRef =FirebaseStorage.getInstance().getReference();
        StorageReference filepath=storageRef.child("imagePhotos").child(mAuth.getUid());
        final long ONE_MEGABYTE = 1024 * 1024;
        filepath.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profileIV.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AnaEkran.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(AnaEkran.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(AnaEkran.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(AnaEkran.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }



}
