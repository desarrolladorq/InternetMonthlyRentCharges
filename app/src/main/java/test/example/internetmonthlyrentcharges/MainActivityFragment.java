package test.example.internetmonthlyrentcharges;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import test.example.internetmonthlyrentcharges.constants.Constants;
import test.example.internetmonthlyrentcharges.models.UserNameModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    // variable to store a FirebaseListAdapter
    FirebaseListAdapter<UserNameModel> firebaseListAdapter;

    // variable to store a ListView
    ListView listView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the view for MainActivityFragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // inflate the layout for the listView
        listView = (ListView) view.findViewById(
                R.id.listview);

        // set the adapter with firebase data and populate the list view
        setAdapterAndPopulateListView();



        return view;
    }

    private void setAdapterAndPopulateListView(){
        // Get a reference from firebase to a ref call 'usernames' to populate the listview
        Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.ACTIVEUSERS);
        // set the adapter with the firebase ref and also give a model to land the data on it
        firebaseListAdapter = new FirebaseListAdapter<UserNameModel>(
                getActivity(),
                UserNameModel.class,
                R.layout.list_item_user_name,
                ref
        ) {
            @Override
            protected void populateView(View view, UserNameModel userNameModel) {
                // we get the name of the user from the model
                String username = userNameModel.getUsername();
                TextView textView  = (TextView)view.findViewById(R.id.list_item_name);
                textView.setText(username);
            }
        };
        // attach the adapter to the listView
        listView.setAdapter(firebaseListAdapter);
    }
}
