package com.example.fince.fotogratbitirme.Adapter4ALScreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fince.fotogratbitirme.Model4ALScreen.ListItem;
import com.example.fince.fotogratbitirme.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fince on 25.02.2018.
 */

public class DerpAdapter extends RecyclerView.Adapter<DerpAdapter.DerpHolder > {


    private List<ListItem> listData;
    private LayoutInflater inflater;

    private ItemClickCallback itemClickCallback;
    private Context con;

    public interface ItemClickCallback
    {
        void onItemClick(View v,int p);


    }
    public void setItemClickCallback(final ItemClickCallback itemClickCallback)
    {
        this.itemClickCallback=itemClickCallback;
    }
    public DerpAdapter(List<ListItem> listData, Context con)
    {
        this.inflater=LayoutInflater.from(con);
        this.listData=listData;
        this.con=con;
    }
    @Override
    public DerpHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.al_screen_list_item,parent,false);
        return new DerpHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(DerpHolder holder, int position) {
        ListItem item=listData.get(position);
        holder.title.setText(item.getTitle());
        holder.subTitle.setText(item.getSubTitle());
        Picasso.with(con).load(item.getImage_url()).into(holder.icon);

    }
    public void setListData(ArrayList<ListItem> exerciseList)
    {
        this.listData.clear();
        this.listData.addAll(exerciseList);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class DerpHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView title;
        private TextView subTitle;
        private ImageView icon;
        private View container;

        public DerpHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.lbl_item_text);
            subTitle=(TextView)itemView.findViewById(R.id.lbl_item_sub_title);
            icon=(ImageView)itemView.findViewById(R.id.im_item_icon);
            container=itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.cont_item_root)
            {
                itemClickCallback.onItemClick(v,getAdapterPosition());
            }

        }
    }
}
