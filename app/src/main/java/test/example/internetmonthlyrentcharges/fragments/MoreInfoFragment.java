package test.example.internetmonthlyrentcharges.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import test.example.internetmonthlyrentcharges.R;
import test.example.internetmonthlyrentcharges.constants.Constants;
import test.example.internetmonthlyrentcharges.models.UserAtributesModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreInfoFragment extends Fragment {
    public final static String USER_KEY = "userkey";
    public static String catchedUserKey;
    public View rootView;
    private FirebaseListAdapter<UserAtributesModel> firebaseListAdapter;
    private ListView listView;

    private Toolbar toolbar;

    // we declare the variables to inflate the views from Dialog layout later
    private View viewDialog;
    private TextView showActualItemTag;
    private TextView showActualItemValue;
    private EditText editActualItemValue;
    // we declare the variable button at the top because we need to access it later in a helper func.
    private Button editButton;


    public MoreInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_more_info, container, false);

        //Get a reference to a ListView
        listView = (ListView) rootView.findViewById(R.id.listviewMoreInfo);

        //we get catchedUserKey from MonthsFragment, MonthsFragment get its userKey from the intent
        catchedUserKey = MonthsFragment.catchedUserKey;

        /* we call this helper method after initialized catchedUserKey variable
         this method countNumberPaidsMade the total number of paids the user made */
        setNumberPaidsMadeAndAmountOfIncomePerUser();
        // we read data from database and show it in a listView to the screen user
        readFromFirebaseAndShowToScreen();

        // we set the OnClickListener of items on the screen
        interactWithDataOntoScreen();

        return rootView;


    }
    /* helper function to countNumberPaidsMade the number of paids made for the user, we add together paids made by
    the tree years our system support*/
    private void setNumberPaidsMadeAndAmountOfIncomePerUser(){
        // we make the reference to the data we're going to use
        final Firebase refPaids = new Firebase(Constants.MONTLYINTERNETUSERSREF).
                child(Constants.USERSMONTHLYCHARGES).child(catchedUserKey);
        // we set a listener to this ref
        refPaids.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /* variables to store the number of paid's made per month */
                int numberPaidsMade2015 = 0;
                int numberPaidsMade2016 = 0;
                int numberPaidsMade2017 = 0;
                /* this variable add together the number counts where monthPaid() is set to true
                 for the tree years our app currently support */
                int totalNumberPaids = 0;

                // variables to store the income of paid's  made
                int amountOftotalPaids2015 = 0;
                int amountOftotalPaids2016 = 0;
                int amountOftotalPaids2017 = 0;
                /* this variable add together the amounts for the tree years our app
                currently support*/
                int totalAmountNumberPaids = 0;
                // we get the children of the dataSnapshot as an iterable
                Iterable years = dataSnapshot.getChildren();
                /* each value of the iterable represent a branch of year, in this case we have tree
                values, the first is for 2015, the second for 2016 and the last one is for 2017
                 */
                for(Object year :years){
                    // we get the data snapshot for each branch
                    DataSnapshot yearSnap = ((DataSnapshot) year);
                    /* we compare the key of the snapshot to match it with the year we are
                       interested in this case 2015*/
                    if(yearSnap.getKey().equals("2015")){
                        // each branch of year has an array of items in it
                        ArrayList childsYearSnap = yearSnap.getValue(ArrayList.class);
                        // we loop through the values of the array
                        for(int i = 0; i<childsYearSnap.size(); i++){
                            // we extract the values
                            HashMap month = (HashMap) childsYearSnap.get(i);
                            /* we compare the atribute value monthPay to see if it is set to true,
                            if so, then that mean that the month is paid, therefore we increase
                            numberPaidsMade2015 by one each time we encounter monthPaid set to true
                             */
                            /*with a helper function, if monthPaid is true, then return 1 in order
                            to be able to count the items or return month.monthCharge() value in order
                            to add its value together
                             */
                            // we count the number 1 return if month.monthPay is true inside the func
                            numberPaidsMade2015 = numberPaidsMade2015 + countNumberPaidsMade(month);

                            /* we calculate the total incomes of paid's made*/
                            amountOftotalPaids2015 = amountOftotalPaids2015 + incomeOfPaidsMade(month);
                        }
                    }
                    /* we compare the key of the snapshot to match it with the year we are
                      interested in this case 2016*/
                    if(yearSnap.getKey().equals("2016")){
                        // each branch of year has an array of items in it
                        ArrayList childsYearSnap = yearSnap.getValue(ArrayList.class);
                        // we loop through the values of the array
                        for(int i = 0; i<childsYearSnap.size(); i++){
                            // we extract the values
                            HashMap month = (HashMap) childsYearSnap.get(i);
                            /* either return 1 if month.monthPay() is true, or return the value of
                             month.monthCharge().
                             */
                            // we count the number 1 return if month.monthPay is true inside the func.
                            numberPaidsMade2016 = numberPaidsMade2016 + countNumberPaidsMade(month);

                            /* we calculate the total incomes of paid's made*/
                            amountOftotalPaids2016 = amountOftotalPaids2016 + incomeOfPaidsMade(month);


                        }
                    }
                    /* we compare the key of the snapshot to match it with the year we are
                     interested in this case 2017*/
                    if(yearSnap.getKey().equals("2017")){
                        // each branch of year has an array of items in it
                        ArrayList childsYearSnap = yearSnap.getValue(ArrayList.class);
                        // we loop through the values of the array
                        for(int i = 0; i<childsYearSnap.size(); i++){
                            // we extract the values
                            HashMap month = (HashMap) childsYearSnap.get(i);
                            /* we compare the atribute value monthPay to see if it is set to true,
                            if so, then that mean that the month is paid, therefore we increase
                            numberPaidsMade2017 by one each time we encounter monthPaid set to true
                             */
                            /*with a helper function, if monthPaid is true, then return 1 in order
                            to be able to count the items or return month.monthCharge() value in order
                            to add its value together
                             */
                            // we count the number 1 return if month.monthPay is true inside the func.
                            numberPaidsMade2017 = numberPaidsMade2017 + countNumberPaidsMade(month);

                            /* we calculate the total incomes of paid's made*/
                            amountOftotalPaids2017 = amountOftotalPaids2017 + incomeOfPaidsMade(month);
                        }
                    }
                }

                /* we add together the tree variables that countNumberPaidsMade the monthPaids set to true for each
                year*/
                totalNumberPaids = numberPaidsMade2015+numberPaidsMade2016+numberPaidsMade2017;

                // we get the reference of the value to update count of month paids
                Firebase refToUpdateCounts = new Firebase(Constants.MONTLYINTERNETUSERSREF).
                        child(Constants.USERS_ATRIBUTES).child(catchedUserKey).child("3");
                // we build the map of the updated value
                HashMap<String, Object> valueUpdatedCounts = new HashMap<String, Object>();
                valueUpdatedCounts.put("atributeValue", totalNumberPaids);

                // we update the Firebase reference
                refToUpdateCounts.updateChildren(valueUpdatedCounts);

                /* we add together the amounts we receive from the tree years */
                totalAmountNumberPaids = amountOftotalPaids2015+amountOftotalPaids2016+amountOftotalPaids2017;

                // we get the reference of the value to update count of month paids
                Firebase refToUpdateAmounts = new Firebase(Constants.MONTLYINTERNETUSERSREF).
                        child(Constants.USERS_ATRIBUTES).child(catchedUserKey).child("4");

                /* if total amount of three years equals cero then we don't send any numbers to the
                firebase reference, instead we leave it with an ampty string */
                if(totalAmountNumberPaids == 0){
                    // we build the map of the updated value with an empty string
                    HashMap<String, Object> valueUpdatedAmounts = new HashMap<String, Object>();
                    valueUpdatedAmounts.put("atributeValue", "");
                    // we update the Firebase reference
                    refToUpdateAmounts.updateChildren(valueUpdatedAmounts);
                }else{
                    // we build the map of the updated value
                    HashMap<String, Object> valueUpdatedAmounts = new HashMap<String, Object>();
                    valueUpdatedAmounts.put("atributeValue", totalAmountNumberPaids);
                    // we update the Firebase reference
                    refToUpdateAmounts.updateChildren(valueUpdatedAmounts);
                }





            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private int countNumberPaidsMade(HashMap month){
        int count = 0;
        if(month.get("monthPay").equals(true)){
            count = 1;
        }
        return  count;
    }
    private int incomeOfPaidsMade(HashMap month){
        String value;
        int amount = 0;
        if(month.get("monthPay").equals(true)){
            value = (String) month.get("monthCharge");
            amount = Integer.parseInt(value);

        }
        return amount;

    }

    private void readFromFirebaseAndShowToScreen(){
        //we make a reference to firebase database
        Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.USERS_ATRIBUTES).
                child(catchedUserKey);
        firebaseListAdapter = new FirebaseListAdapter<UserAtributesModel>(
                getActivity(),
                UserAtributesModel.class,
                R.layout.list_item_more_info,
                ref
        ) {
            @Override
            protected void populateView(View view, UserAtributesModel value) {
                TextView tagTextView = (TextView)view.findViewById(R.id.list_item_name_tag);
                TextView valueTextView = (TextView) view.findViewById(R.id.list_item_name_value);

                /* we put a dollar sign in the TextView when we visualize the values that have to do with
                money and also we add two ceros (00) to the right of dollar sign
                to have a fancy view */
                tagTextView.setText(value.getAtributeName());
                // we set the values onto the view in the list
                valueTextView.setText("$"+value.getAtributeValue().toString());

                /* if the values we visualize do not have to do with money then we need get rid of
                 the dollar sign */
                // item in position 0 in the array do not have to do with money format
                if(firebaseListAdapter.getItem(0).getAtributeName().equals(value.getAtributeName())){
                    tagTextView.setText(value.getAtributeName());
                    valueTextView.setText(value.getAtributeValue().toString());
                }
                // item in position 3 in the array do not have to do with money format
                if(firebaseListAdapter.getItem(3).getAtributeName().equals(value.getAtributeName())){
                    tagTextView.setText(value.getAtributeName());
                    valueTextView.setText(value.getAtributeValue().toString());
                }

                // item in position 6 in the array do not have to do with money format
                if(firebaseListAdapter.getItem(6).getAtributeName().equals(value.getAtributeName())){
                    tagTextView.setText(value.getAtributeName());
                    valueTextView.setText(value.getAtributeValue().toString());
                }

                /* here also we need to give format to the long value we retrieve from the server
                in a SimpleDateFormat */
                // this position in the array have to do with the time when the user was registered
                // in the database.
                // also this value in position 7 do not have to do with money format
                if(firebaseListAdapter.getItem(7).getAtributeName().equals(value.getAtributeName())){
                    long datelong = (long) value.getAtributeValue();
                    SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String dateString = SIMPLE_DATE_FORMAT.format(datelong);
                    tagTextView.setText(value.getAtributeName());
                    valueTextView.setText(value.toString());
                    valueTextView.setText(dateString);
                }

            }
        };
        listView.setAdapter(firebaseListAdapter);

    }

    private void interactWithDataOntoScreen(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /* we set the index of the view that we receive to be a final variable to use it
                 later on code*/
                final int indxOfViewWeReceive = i;

                /* we set the the view that we're going to show to the user when click is done over
                 any item in the listView on fragment_more_info*/
                viewDialog = (LayoutInflater.from(getActivity())).inflate(R.layout.
                        edit_individual_item_more_info, null);

                /* we get the two TextViews of the view that we receive from the list */
                final TextView tagActualItemView = (TextView) view.findViewById(R.id.list_item_name_tag);
                final TextView valueActualItemView = (TextView) view.findViewById(R.id.list_item_name_value);

                /* we inflate all the views we need from edit_individual_item_more_info layout */
                showActualItemTag = (TextView) viewDialog.findViewById(R.id.show_actual_item_tag);
                showActualItemValue = (TextView) viewDialog.findViewById(R.id.show_actual_item_value);
                editActualItemValue = (EditText) viewDialog.findViewById(R.id.edit_actual_item_value);
                editButton = (Button) viewDialog.findViewById(R.id.editButton);

                /* we set the editActualItemValue view invisible, editActualItemValue view is where
                 the user is going to update any data of a particular item in the list, the view
                 only is show if the user hit the edit button*/
                editActualItemValue.setVisibility(viewDialog.GONE);

                 /* the item on the view we are currently over in the list has a tagTextView, we get this
                 tagTextView to use it in the Dialog view that raises when user click on an item */
                final String tagValueActualItem = tagActualItemView.getText().toString();

                /* the item on the view we are currently over in the list has a value, we get this
                 value to use it in the Dialog view that raises when user click on an item */
                final String valueActualItem = valueActualItemView.getText().toString();

                String valueActualItemToUse ="";

                /*when the Dialog windows is show we need to get rid of the dolar sign in order to
                avoid feddback and send the dolar sign accidentally to the firebase database*/

                // first we need the indx of the dolar sign
                int indxToGetRideOf = valueActualItem.indexOf("$");

                // if it contain a number not equal to negative 1 (-1) then, this mean that this text
                // contain a dollar sign and this also mean that the box we're about to show need to
                // be prepared to receive input of type number
                if(indxToGetRideOf!=-1){
                    /* we need to get rid of the dollar sign and get the substring of valueActualItem
                    and assign it to a new var */
                    valueActualItemToUse = valueActualItem.substring(indxToGetRideOf+1, valueActualItem.length());
                    // we set the EditText view of the Dialog window to be of type number
                    editActualItemValue.setInputType(InputType.TYPE_CLASS_NUMBER);

                }
                /* if the index of the view we receive is 6 then that view is of type date therefore
                we set the EditText that get show in the Dialog to be of type date*/
                else if(indxOfViewWeReceive == 6){
                    // we set the EditText view of the Dialog window to be of type date
                    editActualItemValue.setInputType(InputType.TYPE_CLASS_DATETIME);
                    /* and also we assign the same text as in valueActualItem to the new var without
                    changes */
                    valueActualItemToUse = valueActualItem;
                }
                else {
                    /*  if the text is not of type number or of type date then we assign to the new
                    var the same text from valueActualItem */
                    valueActualItemToUse = valueActualItem;
                    /*  and also we set the Edit Text to be of type text*/
                    editActualItemValue.setInputType(InputType.TYPE_CLASS_TEXT);

                }


                /* we set the tag of the Dialog view that get shown when user click on an item
                onto the listView */
                showActualItemTag.setText(tagValueActualItem);

                /* we set the value of the Dialog view that get shown when user click on an item
                onto the listView */
                showActualItemValue.setText(valueActualItemToUse);

                /* also, we set the value of editActualItemValue view that is currently hide to have
                 the same value of showActualItemValue */
                editActualItemValue.setText(valueActualItemToUse);


                /* the view that has index position 3 has the "Pagos realizados" atribute name,
                 we don't need to update it manualy, instead this update automaticaly
                with a helper function*/
                if(i==3){
                    Toast.makeText(getActivity(), "Se actualiza automaticamente", Toast.LENGTH_SHORT)
                            .show();
                }
                /* the view that has index position 4 has the "Monto de pagos realizados" atribute name
                 we don't need to update it manualy, instead this update automaticaly
                with a helper function */
                else if(i==4){
                    Toast.makeText(getActivity(), "Se actualiza automaticamente", Toast.LENGTH_SHORT)
                            .show();
                }
                /* the view that has index position 4 has the "Se registró en el sistema" atribute name,
                we don't need to update it manualy, this never changes*/
                else if(i==7){
                    Toast.makeText(getActivity(), "La fecha de registro no cambia", Toast.LENGTH_SHORT)
                            .show();

                }
                else {
                    // build a dialog with an EditText view to update values of the view clicked on
                    fromDialogUpdateMoreInfoItem(indxOfViewWeReceive, tagValueActualItem);
                }
            }
        });

    }

    private void fromDialogUpdateMoreInfoItem(final int indxOfViewWeReceive, final String tagValueActualItem){
             /* for any other case we set the alert dialog builder to allow the user to
                    insert inputs and updates the items in the specified position*/
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        // we set the view of the alertDialog, the view that we inflate above
        alertBuilder.setView(viewDialog);
        //we set the EditText button from the layout that we are about to show to the user
        setClickListenerOfEditTextButton();
                    /* we set actions either when the user hit aoutside of the alert dialog or the user
                    hit the button "ok" */
        alertBuilder.setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                        /* since values in showActualItemValue and editActualItemValue are the same
                        and also editActualItemValue is the last updated value then we get the value
                        of editActualItemValue to be the value we're going to send to firebase database*/
                String updatedText = editActualItemValue.getText().toString().trim();

                        /* we get the ID index of the view currently we are over, and  we turn it
                        into a string, this can help us build the path to firebase data item */
                String pathToFirebaseItem = Integer.toString(indxOfViewWeReceive);
                        /* we set the entire path to get the item that correspond the current
                         view we are on from firebase */
                Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).
                        child(Constants.USERS_ATRIBUTES).child(catchedUserKey).child(pathToFirebaseItem);
                // in the case that we click on the first item in the array
                // the also we need to update te activeUsersRef with the key of the user

                // we build a Map of the updated value to send
                HashMap updatedValue = new HashMap();
                updatedValue.put("atributeName", tagValueActualItem);
                updatedValue.put("atributeValue", updatedText);

                // we update the individual item of Firebase reference
                ref.updateChildren(updatedValue);

                // and also we update the name in activeUsers ref
                if(indxOfViewWeReceive == 0){
                    // we update catchedUserName variable from MonthsFragment this help us set the
                    // toolbar title for the activity of this fragment appropriately
                    MonthsFragment.catchedUserName = updatedText;
                    // we call the method that sets the title of the toolbar again, to ensure that the
                    // title is updated immediately
                    setToolbarTitleToCurrentUsername();
                    // we build the path to activeUsers ref
                    Firebase usernameRef = new Firebase(Constants.MONTLYINTERNETUSERSREF).
                            child(Constants.ACTIVEUSERS).child(catchedUserKey);
                    // we generate the updated value
                    HashMap userNameUpdated = new HashMap();
                    userNameUpdated.put("username", updatedText);
                    // we update the values
                    usernameRef.updateChildren(userNameUpdated);
                }
            }
        });
        // we build the alertDialog and we show it
        Dialog dialog = alertBuilder.create();
        dialog.show();

    }

    /* the same method as in MonthsFragment, but here the method is called only when the username is
     updated */
    private void setToolbarTitleToCurrentUsername(){
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(MonthsFragment.catchedUserName);
    }

    private void setClickListenerOfEditTextButton(){
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActualItemValue.setVisibility(viewDialog.GONE);
                editActualItemValue.setVisibility(viewDialog.getVisibility());
            }
        });
    }


}
