package com.rumbaapp.adaptadores;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rumbaapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Andres Rangel on 19/01/2016.
 */
public class FragmentDetailsAdapter extends Fragment {

    String url;
    int pos;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_IMG_URL = "image_url";
    public static ImageView imageView;


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.pos = args.getInt(ARG_SECTION_NUMBER);
        this.url = args.getString(ARG_IMG_URL);
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentDetailsAdapter newInstance(int sectionNumber, String url) {
        FragmentDetailsAdapter fragment = new FragmentDetailsAdapter();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_IMG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentDetailsAdapter() {
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.details_adapter, container, false);

        imageView = (ImageView) rootView.findViewById(R.id.imagenDetails);

        Picasso.with(getActivity()).load(url).resize(1000,1000)
                .onlyScaleDown()
                .into(imageView);

        return rootView;
    }



}

