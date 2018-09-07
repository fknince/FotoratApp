package com.example.fince.fotogratbitirme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fince.fotogratbitirme.Adapter4ALScreen.DerpAdapter;
import com.example.fince.fotogratbitirme.Model4ALScreen.DetailsActivity;
import com.example.fince.fotogratbitirme.Model4ALScreen.ListItem;

import java.util.ArrayList;


public class aratmaListeleEkrani extends Fragment implements DerpAdapter.ItemClickCallback {

    private static final String aciklama="aciklama";
    private static final String tarih="tarih";
    private static final String extralar="extralar";
    private static final String resim="resim";




    private RecyclerView recView;
    private DerpAdapter adapter;
    private ArrayList listData;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mProgressDialog=new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Aratma geçmişiniz hazırlanıyor");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        View view = inflater.inflate(R.layout.fragment_aratma_listele_ekrani, container, false);


        recView=(RecyclerView)view.findViewById(R.id.rec_list);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseGetSet fgs=new firebaseGetSet(this);
        String path=this.getArguments().getString("path");

        fgs.retrieve(path);
        listData=new ArrayList();




        adapter=new DerpAdapter(listData,getActivity());
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallBack());
        itemTouchHelper.attachToRecyclerView(recView);


        return view;
    }

    private ItemTouchHelper.Callback createHelperCallBack() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback=
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN ,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
                {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
                return simpleItemTouchCallback;
    }

    private void deleteItem(int position) {
        listData.remove(position);
        adapter.notifyItemRemoved(position);


    }

    private void moveItem(int oldPos, int newPos) {
        ListItem item=(ListItem)listData.get(oldPos);
        listData.remove(oldPos);
        listData.add(newPos,item);
        adapter.notifyItemMoved(oldPos,newPos);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onItemClick(View v,int p) {
        ListItem item=(ListItem) listData.get(p);
        Intent i=new Intent(getActivity(), DetailsActivity.class);

        Bundle extras=new Bundle();
        extras.putString(tarih,item.getTitle());
        extras.putString(aciklama,item.getTahminler());
        extras.putString(resim,item.getImage_url());


        i.putExtra(extralar,extras);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {

            getActivity().getWindow().setEnterTransition(new Fade(Fade.IN));
            getActivity().getWindow().setExitTransition(new Fade(Fade.OUT));

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity());


            ActivityCompat.startActivity(getContext(),i,options.toBundle());
        }
        else
        {
            startActivity(i);
        }


    }
    public void updateList(ArrayList<ListItem> list)
    {
        listData=list;
        adapter=new DerpAdapter(list,getActivity());
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallBack());
        itemTouchHelper.attachToRecyclerView(recView);

        recView.invalidate();
        mProgressDialog.cancel();
    }
}
