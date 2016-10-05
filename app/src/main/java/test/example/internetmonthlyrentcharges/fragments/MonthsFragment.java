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
import test.example.internetmonthlyrentcharges.constants.Constants;
import test.example.internetmonthlyrentcharges.models.MonthModel;
import test.example.internetmonthlyrentcharges.models.UserNameModel;


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
    FirebaseListAdapter<MonthModel> firebaseListAdapter;

    ListView listViewCharged2015;
    ListView listViewCharged2016;
    ListView listViewCharged2017;

    public static int NUMBER_PAIDS_2015 = 0;
    public static int NUMBER_PAIDS_2016 = 0;
    public static int NUMBER_PAIDS_2017 = 0;
    
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

        // also with a helper function we set the onItemSelectedListener of the spinner to show
        // diferent listviews, depending the year we selected
        setSpinnerOnItemSelectedListener();

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
        // item position 1 is "2016" (the actual year we are, the last time this code was updated)
        spinnerYears.setSelection(1);
    }

    private void setSpinnerOnItemSelectedListener(){
        spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // if the item selected is 2015 then we show the apropiate listview for that case
                if(adapterYears.getItem(i).equals("2015")){
                    String year = "2015";
                    // Toast: only for test
                    //Toast.makeText(getActivity(), "2015", Toast.LENGTH_SHORT).show();

                    /*either last time we made click on 2016 or 2017 items, but now if we are in 2015
                     then wee need to hide the listview we visit last time that no belong to 2015*/
                    listViewCharged2016 = (ListView) rootView.findViewById(R.id.listViewCash2016);
                    listViewCharged2016.setVisibility(rootView.GONE);

                    listViewCharged2017 = (ListView) rootView.findViewById(R.id.listViewCash2017);
                    listViewCharged2017.setVisibility(rootView.GONE);


                    /* we make the listView for this case (2015) visible
                     */
                    listViewCharged2015 = (ListView) rootView.findViewById(R.id.listViewCash2015);
                    listViewCharged2015.setVisibility(rootView.getVisibility());

                    // we set an array adapter to populate the list view with a helper function
                    setAdapterAndpopulateListView2015();

                    // helper function to set the listeners of each view cliked on, over the listview
                    setOnItemClickListenerOnListViewCharge2015();
                    /* helper method to show the window options to 'suspend the user' temporarily
                     when long click is made over a view in the months listview, take as a parameter
                     the year of the if condition
                     */
                    setOnItemLongClickListenerOnListViewMonthCharge(listViewCharged2015, year);
                }
                // if the item selected is 2015 then we show the apropiate listview for that case
                if(adapterYears.getItem(i).equals("2016")){
                    String year = "2016";
                    /*either last time we made click on 2015 or 2017 items, but now if we are in 2016
                     then wee need to hide the listview we visit last time that no belong to 2016*/
                    listViewCharged2015 = (ListView) rootView.findViewById(R.id.listViewCash2015);
                    listViewCharged2015.setVisibility(rootView.GONE);

                    listViewCharged2017 = (ListView) rootView.findViewById(R.id.listViewCash2017);
                    listViewCharged2017.setVisibility(rootView.GONE);

                    /* we make the listView for this case (2016) visible
                     */
                    //Get a reference to a ListView
                    listViewCharged2016 = (ListView) rootView.findViewById(R.id.listViewCash2016);
                    listViewCharged2016.setVisibility(rootView.getVisibility());

                    // we set an array adapter to populate the list view with a helper function
                    setAdapterAndpopulateListView2016();

                    // helper function to set the OnItemClickListener of items on the listview
                    setOnItemClickListenerOnListViewCharge2016();

                    /* helper method to show the window options to 'suspend the user' temporarily
                     when long click is made over a view in the months listview, take as a parameter
                     the year of the if condition
                     */
                    setOnItemLongClickListenerOnListViewMonthCharge(listViewCharged2016, year);

                }
                if(adapterYears.getItem(i).equals("2017")){
                    String year = "2017";
                    /*either last time we made click on 2015 or 2016 items, but now if we are in 2017
                     then wee need to hide the listview we visit last time that no belong to 2016*/
                    listViewCharged2015 = (ListView) rootView.findViewById(R.id.listViewCash2015);
                    listViewCharged2015.setVisibility(rootView.GONE);

                    listViewCharged2016 = (ListView) rootView.findViewById(R.id.listViewCash2016);
                    listViewCharged2016.setVisibility(rootView.GONE);

                    /* we make the listView for this case (2017) visible
                     */
                    //Get a reference to a ListView
                    listViewCharged2017 = (ListView) rootView.findViewById(R.id.listViewCash2017);
                    listViewCharged2017.setVisibility(rootView.getVisibility());

                    // we set an array adapter to populate the list view with a helper function
                    setAdapterAndpopulateListView2017();


                    // helper function to set the OnItemClickListener of items on the listview
                    setOnItemClickListenerOnListViewCharge2017();

                    /* helper method to show the window options to 'suspend the user' temporarily
                     when long click is made over a view in the months listview, take as a parameter
                     the year of the if condition
                     */
                    setOnItemLongClickListenerOnListViewMonthCharge(listViewCharged2017, year);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setAdapterAndpopulateListView2015(){
        // we get a reference to the firebase actual user data
        Firebase userRef2015 = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.
                USERSMONTHLYCHARGES).child(catchedUserKey).child("2015");

        // We set the FirebaseListAdater
        firebaseListAdapter = new FirebaseListAdapter<MonthModel>(
                getActivity(),
                MonthModel.class,
                R.layout.list_item_montly_charges,
                userRef2015
        ) {
            @Override
            protected void populateView(View view, MonthModel monthModel) {
                boolean isMonthPay = monthModel.isMonthPay();
                boolean isMonthActive = monthModel.isMonthActive();
                if(isMonthPay){
                    TextView text = (TextView)view.findViewById(R.id.list_item_name_textview);
                    TextView text2 = (TextView) view.findViewById(R.id.list_item_name_textviewCash);
                    text.setText(monthModel.getMonthName());
                    text2.setText( "$ "+monthModel.getMonthCharge());
                    text.setTextColor(Color.parseColor("#C8E6C9"));
                    text2.setTextColor(Color.parseColor("#C8E6C9"));
                    NUMBER_PAIDS_2015++;

                }
                else if(!isMonthActive){
                    TextView text = (TextView)view.findViewById(R.id.list_item_name_textview);
                    TextView text2 = (TextView) view.findViewById(R.id.list_item_name_textviewCash);
                    text.setText(monthModel.getMonthName());
                    text2.setText("Suspendió");
                    text.setTextColor(Color.parseColor("#FFCDD2"));
                    text2.setTextColor(Color.parseColor("#FFCDD2"));

                }else {
                    TextView text = (TextView)view.findViewById(R.id.list_item_name_textview);
                    TextView text2 = (TextView) view.findViewById(R.id.list_item_name_textviewCash);
                    text.setText(monthModel.getMonthName());
                    text2.setText("Cobrar apenas");
                    text.setTextColor(Color.parseColor("#CFD8DC"));
                    text2.setTextColor(Color.parseColor("#CFD8DC"));
                }


            }
        };
        listViewCharged2015.setAdapter(firebaseListAdapter);
    }

    private  void setAdapterAndpopulateListView2016(){
        // we get a reference to the firebase actual user data
        Firebase userRef2016 = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.
                USERSMONTHLYCHARGES).child(catchedUserKey).child("2016");
        // We set the FirebaseListAdater
        firebaseListAdapter = new FirebaseListAdapter<MonthModel>(
                getActivity(),
                MonthModel.class,
                R.layout.list_item_montly_charges,
                userRef2016
        ) {
            @Override
            protected void populateView(View view, MonthModel monthModel) {
                boolean isMonthPay = monthModel.isMonthPay();
                boolean isMonthActive = monthModel.isMonthActive();
                if(isMonthPay){
                    TextView text = (TextView)view.findViewById(R.id.list_item_name_textview);
                    TextView text2 = (TextView) view.findViewById(R.id.list_item_name_textviewCash);
                    text.setText(monthModel.getMonthName());
                    text2.setText( "$ "+monthModel.getMonthCharge());
                    text.setTextColor(Color.parseColor("#C8E6C9"));
                    text2.setTextColor(Color.parseColor("#C8E6C9"));
                    NUMBER_PAIDS_2016++;

                }  else if(!isMonthActive){
                    TextView text = (TextView)view.findViewById(R.id.list_item_name_textview);
                    TextView text2 = (TextView) view.findViewById(R.id.list_item_name_textviewCash);
                    text.setText(monthModel.getMonthName());
                    text2.setText("Suspendió");
                    text.setTextColor(Color.parseColor("#FFCDD2"));
                    text2.setTextColor(Color.parseColor("#FFCDD2"));

                }else {
                    TextView text = (TextView)view.findViewById(R.id.list_item_name_textview);
                    TextView text2 = (TextView) view.findViewById(R.id.list_item_name_textviewCash);
                    text.setText(monthModel.getMonthName());
                    text2.setText("Cobrar apenas");
                    text.setTextColor(Color.parseColor("#CFD8DC"));
                    text2.setTextColor(Color.parseColor("#CFD8DC"));
                }


            }
        };
        listViewCharged2016.setAdapter(firebaseListAdapter);
    }


    private void setAdapterAndpopulateListView2017(){
        // we get a reference to the firebase actual user data
        Firebase userRef2017 = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.
                USERSMONTHLYCHARGES).child(catchedUserKey).child("2017");
        // We set the FirebaseListAdater
        firebaseListAdapter = new FirebaseListAdapter<MonthModel>(
                getActivity(),
                MonthModel.class,
                R.layout.list_item_montly_charges,
                userRef2017
        ) {
            @Override
            protected void populateView(View view, MonthModel monthModel) {
                boolean isMonthPay = monthModel.isMonthPay();
                boolean isMonthActive = monthModel.isMonthActive();
                if(isMonthPay){
                    TextView text = (TextView)view.findViewById(R.id.list_item_name_textview);
                    TextView text2 = (TextView) view.findViewById(R.id.list_item_name_textviewCash);
                    text.setText(monthModel.getMonthName());
                    text2.setText( "$ "+monthModel.getMonthCharge());
                    text.setTextColor(Color.parseColor("#C8E6C9"));
                    text2.setTextColor(Color.parseColor("#C8E6C9"));
                    NUMBER_PAIDS_2017++;

                }else if(!isMonthActive){
                    TextView text = (TextView)view.findViewById(R.id.list_item_name_textview);
                    TextView text2 = (TextView) view.findViewById(R.id.list_item_name_textviewCash);
                    text.setText(monthModel.getMonthName());
                    text2.setText("Suspendió");
                    text.setTextColor(Color.parseColor("#FFCDD2"));
                    text2.setTextColor(Color.parseColor("#FFCDD2"));

                }else {
                    TextView text = (TextView)view.findViewById(R.id.list_item_name_textview);
                    TextView text2 = (TextView) view.findViewById(R.id.list_item_name_textviewCash);
                    text.setText(monthModel.getMonthName());
                    text2.setText("Cobrar apenas");
                    text.setTextColor(Color.parseColor("#CFD8DC"));
                    text2.setTextColor(Color.parseColor("#CFD8DC"));
                }


            }
        };
        listViewCharged2017.setAdapter(firebaseListAdapter);

    }

    private void setOnItemClickListenerOnListViewCharge2015(){
        listViewCharged2015.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //we get the current year
                DateFormat df = new SimpleDateFormat("yyyy");
                String currentYear = df.format(Calendar.getInstance().getTime());
                int currentYearInt = Integer.parseInt(currentYear);
                //we declare the year for listViewCharge2015
                int year2015 = 2015;


                /* we need to get the model object in the FirebaseListAdapter in order to access to
                its isMontPay() value to be able to do a comparison later
                 */
                final MonthModel monthModel = firebaseListAdapter.getItem(i);

                /* we need the index of the view that we made click on, later in the code, and for
                 that reason we store it again in a final variable*/
                final int indxView = i;

                /* If the boolean property isMonthPay() is false then this mean that this month is still
                not paid therefore the user is allowed to set it to be paid this is done automatically
                when the DialogBox appear in front of the user and he put an amount and hit the button
                confirmed*/
                // when a user paid the december month, after made it, then all this year is blocked
                if(year2015 < currentYearInt&&firebaseListAdapter.getItem(11).isMonthPay()){
                    Toast.makeText(getActivity(), "Año terminado", Toast.LENGTH_SHORT).show();
                }
                /* if monthActive() is false this mean the user is suspended or was suspended (&&)
                and also if monthPay() is false this mean when the user suspended is not paid (should
                be not paid when is suspended), (||) or if the user solely have isMonthPay() true this
                mean the user have already paid, in all this cases we don't do anything but only to
                send a message to the user */
                else if(!monthModel.isMonthActive()&&!monthModel.isMonthPay()||monthModel.isMonthPay()){
                    Toast.makeText(getActivity(), "No se puede modificar, este mes ya se registró", Toast.LENGTH_SHORT).show();
                }else if(!monthModel.isMonthPay()){
                    // we set a new view to use it to show an alert dialog
                    final View viewForm = (LayoutInflater.from(getActivity())).inflate(R.layout.
                            confirm_payment_window, null);
                    //Build an alert dialog
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setView(viewForm);
                    alertBuilder.setCancelable(true).setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int indxDialog) {
                            // Send amount of payment and set boolean isMonthPaid to true in
                            // firebase database when button confirmed is pressed

                            //we inflate the EditText view
                            EditText paymentAmount = (EditText) viewForm.findViewById(R.id.paymentAmount);
                            // we get the amount the user enter in the EditText view as a string
                            String amount = paymentAmount.getText().toString().trim();

                            /* if the EditText view is not empty, then we proceed to send the tagTextView
                            to firebase */
                            if(!amount.isEmpty()){
                                /* We get the reference to the item we are interested in firebase
                                   using the actual positionn currently we made click in from the
                                   FirebaseListAdapter
                                */
                                //we convert the index position of the view we currently we made
                                //click on and convert iT into a string
                                String indxItemWeAreInterested = Integer.toString(indxView);

                                // we build the path to firebase to the year 2015
                                Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).
                                        child(Constants.USERSMONTHLYCHARGES).child(catchedUserKey).
                                        child("2015").child(indxItemWeAreInterested);

                                // we build a hash map to put the new values to send to firebase
                                HashMap valuesToUpdate = new HashMap();
                                // we set montPay as 'true' meaning the user just charged this month
                                valuesToUpdate.put("monthPay", true);
                                // we set the amount
                                valuesToUpdate.put("monthCharge", amount);

                                // we update the Firebase ref to the new values
                                ref.updateChildren(valuesToUpdate);
                            }
                            // if the EditText view is empty, then we don't do any operation

                        }
                    });
                    Dialog dialog = alertBuilder.create();
                    dialog.show();
                }

            }
        });

    }

    private void setOnItemClickListenerOnListViewCharge2016(){
        listViewCharged2016.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //we get the current year
                DateFormat df = new SimpleDateFormat("yyyy");
                String currentYear = df.format(Calendar.getInstance().getTime());
                int currentYearInt = Integer.parseInt(currentYear);
                //we declare the year for listViewCharge2016
                int year2016 = 2016;

                /* we need to get the model object in the FirebaseListAdapter in order to access to
                its isMontPay() value to be able to do a comparison later
                 */
                final MonthModel monthModel = firebaseListAdapter.getItem(i);

                /* we need the index of the view that we made click on, later in the code, and for
                 that reason we store it again in a final variable*/
                final int indxView = i;

                /* If the boolean property isMonthPay() is false then this mean that this month is still
                not paid therefore the user is allowed to set it to be paid this is done automatically
                when the DialogBox appear in front of the user and he put an amount and hit the button
                confirmed*/
                if(year2016 < currentYearInt&&firebaseListAdapter.getItem(11).isMonthPay()){
                    Toast.makeText(getActivity(), "Año terminado", Toast.LENGTH_SHORT).show();
                }
                /* if monthActive() is false this mean the user is suspended or was suspended (&&)
                and also if monthPay() is false this mean when the user suspended is not paid (should
                be not paid when is suspended), (||) or if the user solely have isMonthPay() true this
                mean the user have already paid, in all this cases we don't do anything but only to
                send a message to the user */
                else if(!monthModel.isMonthActive()&&!monthModel.isMonthPay()||monthModel.isMonthPay()){
                    Toast.makeText(getActivity(), "No se puede modificar, este mes ya se registró", Toast.LENGTH_SHORT).show();
                }
                else if(!monthModel.isMonthPay()){

                    // we set a new view to use it to show an alert dialog
                    final View viewForm = (LayoutInflater.from(getActivity())).inflate(R.layout.
                            confirm_payment_window, null);
                    //Build an alert dialog
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setView(viewForm);
                    alertBuilder.setCancelable(true).setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int indxDialog) {
                            // Send amount of payment and set boolean isMonthPaid to true in
                            // firebase database when button confirmed is pressed

                            //we inflate the EditText view
                            EditText paymentAmount = (EditText) viewForm.findViewById(R.id.paymentAmount);
                            // we get the amount the user enter in the EditText view as a string
                            String amount = paymentAmount.getText().toString().trim();

                            /* if the EditText view is not empty, then we proceed to send the tagTextView
                            to firebase */
                            if(!amount.isEmpty()){
                                /* We get the reference to the item we are interested in firebase
                                   using the actual positionn currently we made click from the
                                   FirebaseListAdapter
                                */
                                //we convert the index position of the view we currently we made
                                //click on and convert iT into a string
                                String indxItemWeAreInterested = Integer.toString(indxView);

                                // we build the path to firebase to the year 2016
                                Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).
                                        child(Constants.USERSMONTHLYCHARGES).child(catchedUserKey).
                                        child("2016").child(indxItemWeAreInterested);

                                // we build a hash map to put the new values to send to firebase
                                HashMap valuesToUpdate = new HashMap();
                                // we set montPay as 'true' meaning the user just charged this month
                                valuesToUpdate.put("monthPay", true);
                                // we set the amount
                                valuesToUpdate.put("monthCharge", amount);

                                // we update the Firebase ref to the new values
                                ref.updateChildren(valuesToUpdate);
                            }

                            // if the EditText view is empty, then we don't do any operation

                        }
                    });
                    Dialog dialog = alertBuilder.create();
                    dialog.show();

                }else if(monthModel.isMonthPay()){
                    /* if monthPaid() is true then we send a little message to the user indicating that
                    this month was already paid */
                    Toast.makeText(getActivity(), "Mes pagado", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void setOnItemClickListenerOnListViewCharge2017(){
        listViewCharged2017.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //we get the current year
                DateFormat df = new SimpleDateFormat("yyyy");
                String currentYear = df.format(Calendar.getInstance().getTime());
                int currentYearInt = Integer.parseInt(currentYear);
                //we declare the year for listViewCharge2017
                int year2017 = 2017;
                /* we need to get the model object in the FirebaseListAdapter in order to access to
                its isMontPay() value to be able to do a comparison later
                 */
                final MonthModel monthModel = firebaseListAdapter.getItem(i);

                /* we need the index of the view that we made click on, later in the code, and for
                 that reason we store it again in a final variable*/
                final int indxView = i;

                /* If the boolean property isMonthPay() is false then this mean that this month is still
                not paid therefore the user is allowed to set it to be paid this is done automatically
                when the DialogBox appear in front of the user and he put an amount and hit the button
                confirmed*/
                if(year2017 < currentYearInt&&firebaseListAdapter.getItem(11).isMonthPay()){
                    Toast.makeText(getActivity(), "Año terminado", Toast.LENGTH_SHORT).show();
                }/* if monthActive() is false this mean the user is suspended or was suspended (&&)
                and also if monthPay() is false this mean when the user suspended is not paid (should
                be not paid when is suspended), (||) or if the user solely have isMonthPay() true this
                mean the user have already paid, in all this cases we don't do anything but only to
                send a message to the user */
                else if(!monthModel.isMonthActive()&&!monthModel.isMonthPay()||monthModel.isMonthPay()){
                    Toast.makeText(getActivity(), "No se puede modificar, este mes ya se registró", Toast.LENGTH_SHORT).show();
                }
                else if(!monthModel.isMonthPay()){
                    // we set a new view to use it to show an alert dialog
                    final View viewForm = (LayoutInflater.from(getActivity())).inflate(R.layout.
                            confirm_payment_window, null);
                    //Build an alert dialog
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setView(viewForm);
                    alertBuilder.setCancelable(true).setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int indxDialog) {
                            // Send amount of payment and set boolean isMonthPaid to true in
                            // firebase database when button confirmed is pressed

                            //we inflate the EditText view
                            EditText paymentAmount = (EditText) viewForm.findViewById(R.id.paymentAmount);
                            // we get the amount the user enter in the EditText view as a string
                            String amount = paymentAmount.getText().toString().trim();

                            /* if the EditText view is not empty, then we proceed to send the tagTextView
                            to firebase */
                            if(!amount.isEmpty()){
                                /* We get the reference to the item we are interested in firebase
                                   using the actual positionn currently we made click from the
                                   FirebaseListAdapter
                                */
                                //we convert the index position of the view we currently we made
                                //click on and convert iT into a string
                                String indxItemWeAreInterested = Integer.toString(indxView);

                                // we build the path to firebase to the year 2017
                                Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).
                                        child(Constants.USERSMONTHLYCHARGES).child(catchedUserKey).
                                        child("2017").child(indxItemWeAreInterested);

                                // we build a hash map to put the new values to send to firebase
                                HashMap valuesToUpdate = new HashMap();
                                // we set montPay as 'true' meaning the user just charged this month
                                valuesToUpdate.put("monthPay", true);
                                // we set the amount
                                valuesToUpdate.put("monthCharge", amount);

                                // we update the Firebase ref to the new values
                                ref.updateChildren(valuesToUpdate);
                            }

                            // if the EditText view is empty, then we don't do any operation

                        }
                    });
                    Dialog dialog = alertBuilder.create();
                    dialog.show();

                }else if(monthModel.isMonthPay()){
                    /* if monthPaid() is true then we send a little message to the user indicating that
                    this month was already paid */
                    Toast.makeText(getActivity(), "Mes pagado", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setOnItemLongClickListenerOnListViewMonthCharge(ListView listView, final String year){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MonthModel monthModel = firebaseListAdapter.getItem(i);
                String monthName = monthModel.getMonthName();
                if(!firebaseListAdapter.getItem(i).isMonthPay()
                        &&firebaseListAdapter.getItem(i).isMonthActive()){
                    showDialogSelectOptionAndSelect(year, monthName, i);
                }
                return true;
            }
        });

    }

    public void showDialogSelectOptionAndSelect(String year, String monthName, int i){
        final String yearWeAreOn = year;
        final int indxOfViewWeClickedOn = i;

        View viewDialog = (LayoutInflater.from(getActivity())).inflate(R.layout.fragment_select_status, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setView(viewDialog);
        // inflate the title TextView Dialog
        TextView titleDialog = (TextView)viewDialog.findViewById(R.id.titleDialog);
        // set the text in titleDialog TextView to userName
        titleDialog.setText(monthName);

        //Get a reference to a ListView
        ListView listView = (ListView) viewDialog.findViewById(R.id.listviewSelectStatus);

        // build an array list of options items
        String[] optionItemsArray = {
                "Suspender mes", // suspend the user this month
        };

        // build an adapter to use it to set the listView later
        ListAdapter optionItemsAdapter = new ArrayAdapter<String>(
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
                // if i equals cero (0) this mean the user is going to suspend temporarily the service
                // and also if the isMonthPay() atribute is set to false then we suspend the user
                if(i == 0){
                    if(!firebaseListAdapter.getItem(indxOfViewWeClickedOn).isMonthPay()
                            &&firebaseListAdapter.getItem(indxOfViewWeClickedOn).isMonthActive()){
                        // we set the attribute 'isMonthActive()' of month model to be false
                        setIsMonthActiveToFalse(yearWeAreOn, indxOfViewWeClickedOn);
                        sendDataToSuspendedUsersRef();
                        // we remove the user data in the actual branch (activeUsers)
                        removeDataFromActiveUsersRef();
                        // we send a message to that this user was removed
                        Toast.makeText(getActivity(), "Usuario suspendido", Toast.LENGTH_SHORT).show();
                        // we quit the dialog window
                        dialog.dismiss();
                    }
                }else{
                    Toast.makeText(getActivity(), "no se puede suspender, mes pasado ya pagado",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        dialog.show();
    }

    private void sendDataToSuspendedUsersRef(){
        // we build a new ref to suspended users ref with the key of the user we're suspending
        Firebase userKeyFromRemovedUserRef = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.SUSPENDEDUSERS).
                child(catchedUserKey);

        // we build a UserName Model with the name we got from the intent
        UserNameModel userNameModel = new UserNameModel(catchedUserName);

        // We set the value of the ref with the the user model
        userKeyFromRemovedUserRef.setValue(userNameModel);

        // Toast.makeText(getActivity(), keyUser, Toast.LENGTH_SHORT).
        //       show();

    }
    private  void removeDataFromActiveUsersRef(){
        // we build active users ref to the user key and its data from it
        Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.ACTIVEUSERS).
                child(catchedUserKey);
        ref.setValue(null);
    }
    private void setIsMonthActiveToFalse(String year, int i){
        // we convert the index of the actual month we clik on above into a string
        String indxViewClickedOn = Integer.toString(i);
        /* we create a reference to signal the actual month we want to update */
        Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).
                child(Constants.USERSMONTHLYCHARGES).child(catchedUserKey).child(year).
                child(indxViewClickedOn);
        // we build the updated value
        HashMap isMontActive = new HashMap();
        isMontActive.put("monthActive", false);
        ref.updateChildren(isMontActive);


    }


}
