package com.example.gemsupport;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class First extends Fragment {

    TextView txt1;

ViewPager viewPager;
   public First(){
       
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_first, container, false);


        viewPager =getActivity().findViewById(R.id.viewPager);

        txt1=view.findViewById(R.id.txtNext);

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });

        return  view;
    }
}