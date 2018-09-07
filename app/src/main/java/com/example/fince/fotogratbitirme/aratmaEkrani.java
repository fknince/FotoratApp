package com.example.fince.fotogratbitirme;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fince.fotogratbitirme.Adapter.tahminlerAdapter;
import com.example.fince.fotogratbitirme.Model.Item;
import com.example.fince.fotogratbitirme.Model4ALScreen.ListItem;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.api.client.http.HttpTransport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.io.ByteArrayOutputStream;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.yavski.fabspeeddial.FabSpeedDial;

import static android.app.Activity.RESULT_OK;


public class aratmaEkrani extends Fragment {






    CircleImageView addImage;
    LinearLayout llselect;
    private boolean isBasildi=false,resimSecildi=false;
    private TextView kamera,galeri;
    private Button araButton;
    private  Bitmap resim;
    ProgressDialog mProgressDialog;
    private TextView txtSonuc1,txtSonuc2,txtSonuc3;

    //camera
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private String mImageFileLocation = "";
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final int IMAGE_GALLERY_REQUEST = 20;


    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String API_KEY="AIzaSyC0ggQRjwYN6f0xsHb9BbzQLCH7XIJ6m9A";
    private static final String API_KEY2="AIzaSyBeAHfpeLSCX7xvaukf7u9q2WsRmbIg0sE";

    private  String[] metinler;
    private String[] months={"Ocak","Şubat","Mart","Nisan","Mayıs","Haziran","Temmuz","Ağustos","Eylül","Ekim","Kasım","Aralık"};

    FabSpeedDial fd;

    //facebook resim paylaşma
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    Target target= new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto=new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if(ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent content=new SharePhotoContent.Builder()

                        .addPhoto(sharePhoto)
                        .build();
                shareDialog.show(content);
            }



        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };







    public aratmaEkrani() {
        // Required empty public constructor
    }


    private LinkedList<String> tahminList;
    private LinkedList<String> skorList;

    //tahmin liste
    RecyclerView list;
    RecyclerView.LayoutManager layoutManager;
    List<Item> items=new ArrayList<>();
    View viewList;

    //galery or camera
    public boolean isTakenFromCam=false;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        //facebook share
        FacebookSdk.sdkInitialize(getContext());
        callbackManager=CallbackManager.Factory.create();
        shareDialog=new ShareDialog(getActivity());



        View view=inflater.inflate(R.layout.fragment_aratma_ekrani,container,false);
        addImage=(CircleImageView) view.findViewById(R.id.circleImgView);
        llselect=(LinearLayout)view.findViewById(R.id.llselect);
        kamera=(TextView)view.findViewById(R.id.dlg_camera);
        galeri=(TextView)view.findViewById(R.id.dlg_gallery);
        araButton=(Button)view.findViewById(R.id.araButton);
        mProgressDialog=new ProgressDialog(getActivity());
        txtSonuc1=(TextView)view.findViewById(R.id.txtSonuc1);
        txtSonuc2=(TextView)view.findViewById(R.id.txtSonuc2);
        txtSonuc3=(TextView)view.findViewById(R.id.txtSonuc3);
        txtSonuc3.setMovementMethod(new ScrollingMovementMethod());
        txtSonuc2.setMovementMethod(new ScrollingMovementMethod());
        txtSonuc1.setMovementMethod(new ScrollingMovementMethod());
        fd=(FabSpeedDial)view.findViewById(R.id.actionButton);
        fd.setEnabled(false);
        fd.setVisibility(View.INVISIBLE);





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

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               if(isBasildi)
               {
                   animDown();
               }
               else
               {
                   animUp();
               }
               isBasildi=!isBasildi;

            }
        });


        kamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureFromCam();


            }
        });

        galeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureFormGallery();


            }
        });

        araButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resimSecildi  && !mProgressDialog.isShowing())
                {
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.setMessage("İşlem yapılıyor...");
                    mProgressDialog.show();
                    try {
                        if(resimSecildi)
                        {
                            callCloudVision(resim);
                            if(!fd.isEnabled())
                            {
                                fd.setEnabled(true);
                                fd.setVisibility(View.VISIBLE);


                            }

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(getContext(),"Aratılacak resmi seçme için kamera ikonuna dokununuz.",Toast.LENGTH_SHORT);

            }
        });


        tahminList=new LinkedList<String>();
        skorList=new LinkedList<String>();

        //action button
        fd.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true; //false don show menu
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //seçilen item şimdilik ekrana yazı yazdırma
                if(menuItem.getTitle().equals("Listele"))
                    openTahminDialog();
                else
                    facebookShare();
                return false;
            }

            @Override
            public void onMenuClosed() {

            }
        });
        
        //tahmin liste
        viewList=inflater.inflate(R.layout.tahmin_list,container,false);
        list=(RecyclerView)viewList.findViewById(R.id.recycler);
        list.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);

        return view;



    }

    private void openTahminDialog() {


        setData();
        openDialog od=new openDialog(viewList);
        od.show(getActivity().getSupportFragmentManager(),"Listeleme");
    }


    private void setData() {
        if(tahminList.size() != 0 && skorList.size() != 0)
        {
            items.clear();
            for(int i=0;i<tahminList.size();i++)
            {
                Item item=new Item(tahminList.get(i),skorList.get(i),true);
                items.add(item);
            }
            tahminlerAdapter adapter=new tahminlerAdapter(items);

            list.setAdapter(adapter);
        }
    }

    private void animUp() {
        ObjectAnimator animatorX=ObjectAnimator.ofFloat(llselect,"y",1230);
        animatorX.setDuration(650);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(animatorX);
        llselect.setEnabled(true);
        animatorSet.start();
    }

    private void animDown()
    {
        ObjectAnimator animatorX=ObjectAnimator.ofFloat(llselect,"y",1700f);
        animatorX.setDuration(650);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(animatorX);
        llselect.setEnabled(false);
        animatorSet.start();
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
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
            String authorities=getContext().getApplicationContext().getPackageName()+".fileprovider";
            Uri imageUri= FileProvider.getUriForFile(getActivity(),authorities,photoFile);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK)
        {
            if(requestCode == ACTIVITY_START_CAMERA_APP ) {

                setReducedImageSize();
                animDown();
                isBasildi=false;
                isTakenFromCam=true;
                return;

            }

            if(requestCode == IMAGE_GALLERY_REQUEST )
            {
                Uri imageUri=data.getData();
                InputStream inputStream;
                try {
                    inputStream=getContext().getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap=BitmapFactory.decodeStream(inputStream);

                    addImage.setImageBitmap(bitmap);
                    resim=bitmap;
                    resimSecildi=true;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                animDown();
                isBasildi=false;
                isTakenFromCam=false;
                return;
            }
        }
    }
    void setReducedImageSize() {


        Bitmap gelen=rotateBitmapOrientation(mImageFileLocation);
        addImage.setImageBitmap(gelen);
        resim=gelen;
        resimSecildi=true;



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
    @SuppressLint("StaticFieldLeak")
    private void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading


        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(API_KEY) {
                                /**
                                 * We override this so we can inject important identifying fields into the HTTP
                                 * headers. This enables use of a restricted cloud platform API key.
                                 */
                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getContext().getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(getContext().getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {

                metinler=result.split("\n");



                tahminList.clear();
                skorList.clear();
                for(String satir:metinler)
                {
                    cevir(satir.split(":")[1]);
                    skorList.add(satir.split(":")[0]);
                }













            }
        }.execute();
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {

        String message="";
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message += String.format(Locale.US, "%.3f: %s", label.getScore(), label.getDescription());
                message += "\n";
            }
        } else {
            message += "nothing";
        }

        return message;
    }

    public void cevir(String metin)
    {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try
                {

                    GoogleTranslate gt=new GoogleTranslate(API_KEY2);
                    String cevrilen=gt.translte(metin,"eng","tr");

                    return cevrilen;


                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {


              tahminList.add(s.toUpperCase());
              Log.i("Sonuç",s);
                if(tahminList.size() >= 3)
                {
                    String tahmin1=tahminList.get(0);
                    String tahmin2=tahminList.get(1);
                    String tahmin3=tahminList.get(2);

                    txtSonuc2.setText(tahmin1);
                    txtSonuc3.setText(tahmin2);
                    txtSonuc1.setText(tahmin3);

                    if(tahminList.size() == metinler.length)
                    {
                        mProgressDialog.setMessage("Arama işlemi sonlandırılıyor..");
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        prepareInformation();

                    }
                }


            }
        }.execute();
    }

  public void facebookShare()
  {

      //create callback
      shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
          @Override
          public void onSuccess(Sharer.Result result) {
              Toast.makeText(getActivity(),"Paylaşma başarılı!",Toast.LENGTH_SHORT).show();

          }

          @Override
          public void onCancel() {
              Toast.makeText(getActivity(),"Paylaşma iptal edildi!",Toast.LENGTH_SHORT).show();

          }

          @Override
          public void onError(FacebookException error) {
              Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();

          }
      });
      //load image
      Uri gelen=getImageUri(getContext(),resim);
      Picasso.with(getContext())
              .load(gelen)
              .into(target);
  }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void prepareInformation()
    {

        Intent intent=getActivity().getIntent();
        String path="";
        if(intent.getBooleanExtra("facebook_connect",false))
        {
            path=intent.getStringExtra("name");
        }
        else
        {
            FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
            path=mUser.getUid();
        }
        firebaseGetSet fgs=new firebaseGetSet();
        Date currentTime = Calendar.getInstance().getTime();
        int year=Calendar.getInstance().get(Calendar.YEAR);
        int day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String saat=currentTime.getHours() < 10 ? "0"+currentTime.getHours():String.valueOf(currentTime.getHours());
        String dakika=currentTime.getMinutes() < 10 ? "0"+currentTime.getMinutes():String.valueOf(currentTime.getMinutes());
        String saniye=currentTime.getSeconds() < 10 ? "0"+currentTime.getSeconds():String.valueOf(currentTime.getSeconds());
        String date="";
        date+=day+" "+months[currentTime.getMonth()]+" "+year+"/"+saat+":"+
                dakika+":"+saniye;
        fgs.addSearchingImage(resim,this,path,date);


    }
    public void addInformation(Uri gelen)
    {
        Intent intent=getActivity().getIntent();
        String path="";
        if(intent.getBooleanExtra("facebook_connect",false))
        {
            path=intent.getStringExtra("name");
        }
        else
        {
            FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
            path=mUser.getUid();
        }
        String metin="";
        if(isTakenFromCam)
            metin="Kamera ile çekildi.";
        else
            metin="Galeriden alındı.";
        String tahminler="";
        if(tahminList != null)
        {
            for(int i=0;i<tahminList.size();i++)
            {
                tahminler+=skorList.get(i)+"\t\t\t\t\t\t"+tahminList.get(i)+"\n";
            }
        }
        Date currentTime = Calendar.getInstance().getTime();
        int year=Calendar.getInstance().get(Calendar.YEAR);
        int day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String saat=currentTime.getHours() < 10 ? "0"+currentTime.getHours():String.valueOf(currentTime.getHours());
        String dakika=currentTime.getMinutes() < 10 ? "0"+currentTime.getMinutes():String.valueOf(currentTime.getMinutes());
        String saniye=currentTime.getSeconds() < 10 ? "0"+currentTime.getSeconds():String.valueOf(currentTime.getSeconds());
        String date="";
        date+=day+" "+months[currentTime.getMonth()]+" "+year+"/"+saat+":"+
                dakika+":"+saniye;
        firebaseGetSet fgs=new firebaseGetSet();
        ListItem item=new ListItem(date,metin,gelen.toString(),tahminler);
        fgs.addSearchingInformation(item,path);
        if(mProgressDialog.isShowing())
            mProgressDialog.cancel();

    }




}
