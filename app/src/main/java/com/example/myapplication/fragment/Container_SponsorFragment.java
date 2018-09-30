package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.Spon_JSONTaskAndParsor;
import com.example.myapplication.R;
import com.example.myapplication.adapter.SponAdapter;
import com.example.myapplication.bin.SponsorBoardBin;

import java.util.ArrayList;


public class Container_SponsorFragment extends Fragment {
    private RecyclerView recyclerView;
    private SponAdapter adapter;
    ArrayList<SponsorBoardBin> sponsorBoardBins;
    public Button spon_enroll;


    public Container_SponsorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public static Container_SponsorFragment newInstance() {
        Container_SponsorFragment fragment = new Container_SponsorFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View mLayout = inflater.inflate(R.layout.fragment_container__sponsor, container, false);

        sponsorBoardBins = new ArrayList<SponsorBoardBin> ();
        Spon_JSONTaskAndParsor sponJsonTaskAndParsor = new Spon_JSONTaskAndParsor(sponsorBoardBins,null);
        sponJsonTaskAndParsor.execute(Spon_JSONTaskAndParsor.INFOBOARD_ALL_URL);


        recyclerView = (RecyclerView)mLayout.findViewById(R.id.spon_container_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                1, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

       /* layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);*/

        adapter = new SponAdapter(R.layout.spon_recycle_item);
        recyclerView.setAdapter(adapter);


        spon_enroll = (Button)mLayout.findViewById(R.id.spon_enrollment);

        spon_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SponEnrollment.class);
                startActivity(intent);


            }
        });

        return mLayout;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
