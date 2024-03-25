package com.gamecodeschool.totalhealthplus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;



public class SecondFragment extends Fragment {

    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        progressBar = view.findViewById(R.id.progBar);
        updateProgress(50);



        return view;

    }


    public void updateProgress(int progress) {
        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
    }


}