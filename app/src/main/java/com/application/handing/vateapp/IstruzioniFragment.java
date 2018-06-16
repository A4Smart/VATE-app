package com.application.handing.vateapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IstruzioniFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.title_activity_istruzioni);
        return inflater.inflate(R.layout.frag_istruzioni, container, false);
    }

}
