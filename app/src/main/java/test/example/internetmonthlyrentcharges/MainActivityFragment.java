package test.example.internetmonthlyrentcharges;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    // declaring variable to store the key of the user, which we're going to use to build a new ref
    String keyUser;

    /* declaring the variable to store the username model of the user */
    UserNameModel userNameModel;



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


        // helper function to set the onItemClickListener on the list view
        setOnItemClickListenerOnListView();

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

    private void setOnItemClickListenerOnListView(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /* we get user the user key by getting the ref of the current view position in the
                firebaseListAdapter */
                keyUser = firebaseListAdapter.getRef(i).getKey();
                String keyUser = firebaseListAdapter.getRef(i).getKey();
                /* we get the model from the firebaseListAdapter based on the index
                of the view that we make click on */
                userNameModel = firebaseListAdapter.getItem(i);
                // we get the user name from the model
                String userName = userNameModel.getUsername();
                // we send both userKey and userName as extras in the intent
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                //intent.putExtra(MonthsFragment.USER_KEY, keyUser);
                //intent.putExtra(MonthsFragment.USER_NAME, userName);
                // we start the intent
                startActivity(intent);

            }
        });
    }
}
