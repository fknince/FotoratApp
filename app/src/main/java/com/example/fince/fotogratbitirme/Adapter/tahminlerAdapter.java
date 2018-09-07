package com.example.fince.fotogratbitirme.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fince.fotogratbitirme.MainActivity;
import com.example.fince.fotogratbitirme.Model.Item;
import com.example.fince.fotogratbitirme.R;
import com.example.fince.fotogratbitirme.openDialog;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.List;

/**
 * Created by fince on 17.02.2018.
 */

class MyViewHolderWithChild extends RecyclerView.ViewHolder
{
    public TextView textView,textViewChild;
    public RelativeLayout button;
    public ExpandableLinearLayout expandableLayout;

    public MyViewHolderWithChild(View itemView) {
        super(itemView);
        textView=(TextView)itemView.findViewById(R.id.textView);
        textViewChild=(TextView)itemView.findViewById(R.id.textViewChild);
        button=(RelativeLayout)itemView.findViewById(R.id.button);
        expandableLayout=(ExpandableLinearLayout) itemView.findViewById(R.id.expandableLayout);

    }
}
public class tahminlerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Item> items;
    Context context;
    SparseBooleanArray expandState=new SparseBooleanArray();

    public tahminlerAdapter(List<Item> items)
    {
        this.items=items;
        for(int i=0;i<items.size();i++)
        {
            expandState.append(i,false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position).isExpandable())
        {
            return 1;
        }
        else
            return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        if(viewType == 0  )//without item kullanılmayacak
        {
            //DO Nothing
            return null;
        }
        else
        {
            LayoutInflater inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.row_with_child,parent,false);
            return new MyViewHolderWithChild(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(holder.getItemViewType())
        {
            case 0://Do nothing
            {
                break;
            }
            case 1:
            {
                MyViewHolderWithChild viewHolder=(MyViewHolderWithChild)holder;
                Item item=items.get(position);
                viewHolder.setIsRecyclable(false);
                viewHolder.textView.setText(item.getText());
                viewHolder.expandableLayout.setInRecyclerView(true);
                viewHolder.expandableLayout.setExpanded(expandState.get(position));
                viewHolder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {

                    }

                    @Override
                    public void onPreOpen() {
                        changeRotate(viewHolder.button,0f,180f).start();
                        expandState.put(position,true);

                    }

                    @Override
                    public void onPreClose() {
                        changeRotate(viewHolder.button,180f,0f).start();
                        expandState.put(position,false);

                    }

                    @Override
                    public void onOpened() {

                    }

                    @Override
                    public void onClosed() {

                    }
                });
                viewHolder.button.setRotation(expandState.get(position)?180f:0f);
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.expandableLayout.toggle();
                    }
                });
                viewHolder.textViewChild.setText(items.get(position).getSubText());


            }
        }

    }

    private ObjectAnimator changeRotate(RelativeLayout button, float from, float to ){
        ObjectAnimator animator=ObjectAnimator.ofFloat(button,"rotation",from,to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
