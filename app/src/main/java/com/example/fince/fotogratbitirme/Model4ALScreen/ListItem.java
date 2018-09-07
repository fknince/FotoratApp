package com.example.fince.fotogratbitirme.Model4ALScreen;

import android.graphics.Bitmap;

/**
 * Created by fince on 25.02.2018.
 */

public class ListItem {

    public ListItem()
    {

    }


    private String title;
    private String subTitle;
    private String  image_url;

    public ListItem(String title, String subTitle, String image_url, String tahminler) {
        this.title = title;
        this.subTitle = subTitle;
        this.image_url = image_url;
        this.tahminler = tahminler;
    }

    private String tahminler;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTahminler() {
        return tahminler;
    }

    public void setTahminler(String tahminler) {
        this.tahminler = tahminler;
    }





}
