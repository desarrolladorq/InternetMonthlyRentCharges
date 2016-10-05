package test.example.internetmonthlyrentcharges.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import test.example.internetmonthlyrentcharges.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthsFragment extends Fragment {
    private View rootView;
    /* this fragment hold 2 static var in order to use it to set the extras for an intent made by
    another activity which will launch this activity */
    public final static String USER_KEY = "userkey";
    public final static String USER_NAME = "userName";
    public static String catchedUserKey;
    public static String catchedUserName;
    private Toolbar toolbar;
    private Spinner spinnerYears;
    private ArrayAdapter<String> adapterYears;
    
    public MonthsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_months, container, false);

        // Get tge intent that trigger this fragment activity and store it in variable intent
        Intent intent = getActivity().getIntent();

        /*
        Get the key of the user from the intent in a new var, this info was stored in the intent as extra data
        the key is stored in the var catchedUserKey
        */
        catchedUserKey = intent.getStringExtra(USER_KEY);

        // the same way we get the userName from the intent
        catchedUserName = intent.getStringExtra(USER_NAME);

        // we set the toolbar title to be the name of person we currently consulting
        setToolbarTitleToCurrentUsername();

        /* we set the menu where we can choose which year to visualize its months, notice here we
         refer to a spinner as a menu-*/

        setSpinnerMenu();

        return rootView;


    }

    private void setToolbarTitleToCurrentUsername(){
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(catchedUserName);
    }

    private void setSpinnerMenu(){
        //we declare an array of years that currently our app support, we can add more years later
        String[] years = {"2015", "2016","2017"};
        //we inflate the spinner
        spinnerYears = (Spinner) rootView.findViewById(R.id.spinnerYears);

        //we set an array adapter of items for tge spinner
        adapterYears = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, years);

        // we set the visual effect drop down of the spinner
        adapterYears.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // we set the adapter of the spiner
        spinnerYears.setAdapter(adapterYears);

        // we set the item in the index position 1 as the default item selection to show
        // item position 1 is "2016" (the actual year we are) the last time this code was updated
        spinnerYears.setSelection(1);
    }


}
