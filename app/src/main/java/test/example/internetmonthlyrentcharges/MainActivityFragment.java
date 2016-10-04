package test.example.internetmonthlyrentcharges;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.client.Firebase;

import test.example.internetmonthlyrentcharges.constants.Constants;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the view for MainActivityFragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //Get a reference to a ListView, and attach this adapter to it
        ListView listView = (ListView) view.findViewById(
                R.id.listview);

        // Get a reference from firebase to a ref call 'usernames' to populate the listview
        Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.ACTIVEUSERS);


        return view;
    }
}
