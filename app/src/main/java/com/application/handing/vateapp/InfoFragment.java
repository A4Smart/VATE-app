package com.application.handing.vateapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InfoFragment extends Fragment {

    private final static String INDIRIZZO_WEB = "http://vate.eu/";

    public static InfoFragment newInstance () {
        return new InfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.info);
        return inflater.inflate(R.layout.frag_info, container, false);
    }

}
