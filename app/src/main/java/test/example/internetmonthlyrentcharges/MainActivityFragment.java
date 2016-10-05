package test.example.internetmonthlyrentcharges;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import test.example.internetmonthlyrentcharges.constants.Constants;
import test.example.internetmonthlyrentcharges.fragments.MonthsFragment;
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

    private ListAdapter optionItemsAdapter;


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

        // helper function to set longItemClickListener for the listView
        setOnItemLongClickListenerOnListView();

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
                intent.putExtra(MonthsFragment.USER_KEY, keyUser);
                intent.putExtra(MonthsFragment.USER_NAME, userName);
                // we start the intent
                startActivity(intent);

            }
        });
    }

    private void setOnItemLongClickListenerOnListView(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the user key
                keyUser = firebaseListAdapter.getRef(i).getKey();
                /* we get the model from the firebaseListAdapter based on the index
                of the view that we make click on */
                userNameModel = firebaseListAdapter.getItem(i);
                // get the username, from the model
                String userName = userNameModel.getUsername();
                /*show the dialog to select an options for this user and send the key and the name
                as parameters*/
                showDialogSelectOptionAndSelect(keyUser, userName);
                return true;
            }
        });

    }

    private void showDialogSelectOptionAndSelect(String userKey, String userName){
        View viewDialog = (LayoutInflater.from(getActivity())).inflate(R.layout.fragment_select_status, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setView(viewDialog);

        // inflate the title TextView Dialog
        TextView titleDialog = (TextView)viewDialog.findViewById(R.id.titleDialog);
        // set the text in titleDialog TextView to userName
        titleDialog.setText(userName);

        //Get a reference to a ListView
        listView = (ListView) viewDialog.findViewById(R.id.listviewSelectStatus);

        // build an array list of options items
        String[] optionItemsArray = {
                "Suspender temporalmente", // suspend the user
                "Quitar definitivamente" // the user unsubscribed from the service
        };

        // build an adapter to set the listView later
        optionItemsAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_select_options,
                R.id.list_item_option,
                optionItemsArray
        );

        listView.setAdapter(optionItemsAdapter);

        // normally any actions to do is set after alertBuilder.setCancelable(true) ...
        alertBuilder.setCancelable(true);

        // but here we make an exception we first built the dialog
        final Dialog dialog = alertBuilder.create();

        /* and then after built it we set the OnItemClickListener of the list view that is inside of
        the dialog after we select an option, we use dialog.dismiss() method to quit the dialog */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // if i equals cero (0) this mean the user is going to suspend temporarily the service
                if(i == 0){
                    // we send the user data to the branch 'suspendedUsers' in firebase database
                    sendDataToSuspendedUsersRef();
                    // we remove the user data in the actual branch (activeUsers)
                    removeDataFromActiveUsersRef();
                    // we send a message to that this user was removed
                    Toast.makeText(getActivity(), "Usuario suspendido", Toast.LENGTH_SHORT).show();
                    // we quit the dialog window
                    dialog.dismiss();

                }
                // if i equals one (1) this mean the user is going to leave forever the service
                if(i == 1){
                    Toast.makeText(getActivity(), "Este usuario deja el servicio", Toast.LENGTH_SHORT).
                            show();
                }

                return true;
            }
        });
        dialog.show();
    }
    private void sendDataToSuspendedUsersRef(){
        // we build the ref to the new reference to send the data
        Firebase removedUsersRef = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.SUSPENDEDUSERS).
                child(keyUser);

        // We send the the model of the user
        removedUsersRef.setValue(userNameModel);

        // Toast.makeText(getActivity(), keyUser, Toast.LENGTH_SHORT).
        //       show();

    }

    private  void removeDataFromActiveUsersRef(){
        Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.ACTIVEUSERS).
                child(keyUser);
        ref.setValue(null);
    }
}
