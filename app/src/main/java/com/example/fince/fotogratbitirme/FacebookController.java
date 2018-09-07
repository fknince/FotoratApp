package com.example.fince.fotogratbitirme;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class FacebookController {
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private Profile profile;
    private Context context;
    private loginScreen ls;
    FacebookController(LoginButton loginButton, final Context context, loginScreen ls)
    {
        this.context=context;
        this.ls=ls;
        callbackManager= CallbackManager.Factory.create();
        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                profileChanged(currentProfile);
            }
        };
        startActions();
        FacebookCallback<LoginResult> callback=new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile= Profile.getCurrentProfile();
                profileChanged(profile);
                Toast.makeText(context,"Bağlanma İşlemi Başarılı.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
        loginButton.setReadPermissions("user_friends","email");
        loginButton.registerCallback(callbackManager,callback);
    }

    public void tokenChanged(AccessToken newToken)
    {

    }
    public void profileChanged(Profile newProfile)
    {
        this.profile=newProfile;
        if(profile != null)
        {
            Intent main=new Intent(context,AnaEkran.class);
            main.putExtra("facebook_connect",true);
            main.putExtra("name",profile.getName());
            main.putExtra("image",profile.getProfilePictureUri(200,200).toString());
            ls.changeActivity(main);


        }
    }
    public Profile getCurrentProfile() {
        return Profile.getCurrentProfile();
    }
    public void stopActions()
    {
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
    public void startActions()
    {
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }
    public CallbackManager getCallbackManager() {
        return callbackManager;
    }






}
