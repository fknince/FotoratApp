package com.example.fince.fotogratbitirme;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by fince on 23.01.2018.
 */

public class SliderAdapter extends PagerAdapter{
    Context context;
    LayoutInflater layoutInflater;
    public SliderAdapter(Context context)
    {
        this.context=context;
    }
    //Arrays
    public int[] slide_images={
            R.drawable.testicon,
            R.drawable.testicon2,
            R.drawable.testicon3
    };
    //slide headings
    public String[] slide_headings={
            "EAT",
            "SLEEP",
            "CODE"
    };
    //slide descriptions
    public String[] slide_descs={
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce porta est quis rutrum eleifend.",
            "İki Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce porta est quis rutrum eleifend",
            "Üç Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce porta est quis rutrum eleifend."
    };
    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(RelativeLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageview=(ImageView)view.findViewById(R.id.slide_image);
        TextView slideHeading=(TextView)view.findViewById(R.id.slide_heading);
        TextView slideDescription=(TextView)view.findViewById(R.id.slide_desc);

        slideImageview.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
