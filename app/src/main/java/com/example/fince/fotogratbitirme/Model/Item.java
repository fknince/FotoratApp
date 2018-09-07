package com.example.fince.fotogratbitirme.Model;

/**
 * Created by fince on 17.02.2018.
 */

public class Item {
    private String text,subText;
    private boolean isExpandable;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }




    public Item(String text, String subText, boolean isExpandable) {
        this.text = text;
        this.subText = subText;
        this.isExpandable = isExpandable;
    }

}
