package test.example.internetmonthlyrentcharges;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import test.example.internetmonthlyrentcharges.constants.Constants;
import test.example.internetmonthlyrentcharges.models.UserNameModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class SuspendedUsersActivityFragment extends Fragment {
    private ListAdapter optionItemsAdapter;
    private ListView listView;

    // declaring variable to store the model to use in some functions later
    private UserNameModel userNameModelGlobal;
    // declaring variable to store the key of the user, which we're going to use to build a new ref
    String keyUser;


    public SuspendedUsersActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suspended_users, container, false);

        /* Get a reference to the list view inside fragment_suspended_users layout,
         and attach this adapter to it */
        ListView listView = (ListView) view.findViewById(
                R.id.listview);
        // Get a reference of the suspended users from Firebase
        Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.SUSPENDEDUSERS);

        // set the FirebaseListAdapter to populate the list view we just inflate above
        final FirebaseListAdapter<UserNameModel> firebaseListAdapter = new FirebaseListAdapter<UserNameModel>(
                getActivity(),
                UserNameModel.class,
                // for each item in the list we just the same layout we use in activeUsers fragment
                R.layout.list_item_user_name,
                ref
        ) {
            /* FirebaseListAdapter offer an override method to populate each view in the list,
             and the override method offer us an ArrayList of attributes from FirebaseListAdapter */
            @Override
            protected void populateView(View view, UserNameModel userNameModel) {
                /* we set the model to a new variable to be able to access it later*/
                userNameModelGlobal = userNameModel;
                /* we get the username value inside the userNameModelGlobal */
                String username = userNameModel.getUsername();
                // we get the TextView from the view we receive
                TextView textView  = (TextView)view.findViewById(R.id.list_item_name);
                // we set the TextView with the username value
                textView.setText(username);
            }
        };
        // finally we set the list view with all the values in firebaseListAdapter
        listView.setAdapter(firebaseListAdapter);

        // we set OnItemLongClickListener of the listView
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the user key
                keyUser = firebaseListAdapter.getRef(i).getKey();
                // we set the variable to store the model at this position
                userNameModelGlobal =  firebaseListAdapter.getItem(i);
                String userName = userNameModelGlobal.getUsername();
                /*show the dialog to select an options for this user and send the key and the name
                as parameters*/
                showDialogSelectOptionAndSelect(keyUser, userName);
                return true;
            }
        });

        // here we set OnItemClickListener of the listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /* we get the user key by getting the ref of the current view position in the
                firebaseListAdapter */
                keyUser = firebaseListAdapter.getRef(i).getKey();
                // we set the variable to store the model at this position
                userNameModelGlobal =  firebaseListAdapter.getItem(i);
                // we get the username value of the model
                String userName = userNameModelGlobal.getUsername();
                // we send both userKey and userName as extras in the intent
                //Intent intent = new Intent(getActivity(), DetailActivity.class);
                //intent.putExtra(MonthsFragment.USER_KEY, keyUser);
                //intent.putExtra(MonthsFragment.USER_NAME, userName);
                // we start the intent
                //startActivity(intent);

            }
        });

        return view;
    }

    private void showDialogSelectOptionAndSelect(String userKey, final String userName){
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
                "Volver a activar ", // suspend the user
        };
        // convert the array in a ArrayList
        List<String> namesAndLastNames = new ArrayList<String>(
                Arrays.asList(optionItemsArray));
        // build an adapter to set the listView later
        optionItemsAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_select_options,
                R.id.list_item_option,
                optionItemsArray
        );

        listView.setAdapter(optionItemsAdapter);

        // but here we make an exception we first built the dialog
        final Dialog dialog = alertBuilder.create();
        // then after: actions to do
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // if i equals cero this mean we activate the user back
                if(i == 0){
                    // we send data back to active users ref
                    sendDataToActiveUsersRef(userName);
                    // we removed data from suspendedUsers ref
                    removeDataFromSuspendedUsersRef();
                    Toast.makeText(getActivity(), userName+" acaba de ser activado", Toast.LENGTH_SHORT).
                            show();
                    // we quit the dialog window
                    dialog.dismiss();
                }
                if(i == 1){
                    Toast.makeText(getActivity(), userName+" deja el servicio", Toast.LENGTH_SHORT).
                            show();
                }
                return true;
            }
        });

        //alertBuilder.setCancelable(true);
        dialog.show();
    }

    private void sendDataToActiveUsersRef(String userName){
        // we build the ref to the new reference to send the data
        Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.ACTIVEUSERS).
                child(keyUser);

        // we generate the model we're going to send back to active users ref
        UserNameModel userNameModel = new UserNameModel(userName);
        // We send the data of the user that we just activate to the activeUsers ref we just generate
        ref.setValue(userNameModel);
    }

    private  void removeDataFromSuspendedUsersRef(){
        // we build suspendedUsers ref to remove data
         Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.SUSPENDEDUSERS).
                child(keyUser);
        // to remove the data we set the value of the ref to null
        ref.setValue(null);
    }


}
