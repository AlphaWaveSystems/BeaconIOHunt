package com.alphaws.javadaytrivia.gui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.alphaws.javadaytrivia.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLoyalty extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment__loyality, container, false);
        // Inflate the layout for this fragment
        return rootView;
    }
}
