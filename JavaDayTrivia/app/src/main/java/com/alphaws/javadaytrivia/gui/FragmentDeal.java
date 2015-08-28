package com.alphaws.javadaytrivia.gui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alphaws.javadaytrivia.R;
import com.alphaws.javadaytrivia.tools.Commons;

/**
 * Created by oscarvargas on 06/02/15.
 */
public class FragmentDeal extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deal, container, false);
        ImageView image = (ImageView) rootView.findViewById(R.id.activity_beacon_follow_image);
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        image.setImageBitmap(Commons.getRoundedBitmap(bitmap));

        return rootView;
    }

}
