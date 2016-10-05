package test.example.internetmonthlyrentcharges.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.example.internetmonthlyrentcharges.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreInfoFragment extends Fragment {
    private View rootView;


    public MoreInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_more_info, container, false);

        return rootView;


    }


}
