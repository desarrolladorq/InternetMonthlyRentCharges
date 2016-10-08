package test.example.internetmonthlyrentcharges;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import test.example.internetmonthlyrentcharges.constants.Constants;
import test.example.internetmonthlyrentcharges.models.MonthModel;
import test.example.internetmonthlyrentcharges.models.UserAtributesModel;
import test.example.internetmonthlyrentcharges.models.UserNameModel;
import test.example.internetmonthlyrentcharges.semimodels.ArrayOfInputsFormToSendFirebaseO;
import test.example.internetmonthlyrentcharges.semimodels.ArrayOfMonthlyChargesFields;

public class MainActivity extends AppCompatActivity {
    // variables to store views that are in the window that appear to the user when the add
    // button is hit by the user
    private EditText editUserNameForm;
    private EditText billValueForm;
    private EditText installationChargedForm;

    // Variable to store the generated key of the user
    private String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // we inflate the view for this activity
        setContentView(R.layout.activity_main);
        // we inflate the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // we set the support to customize the action bar later
        setSupportActionBar(toolbar);
        /* helper function to calculate and set the toolbar title with the total number of active
         users */
        setToolbarTitleWithTotalNumberActiveUsers();

        /* helper function to set actions When we click over the plus sign button placed at the
         right end bottom of the user screen */
        setFloatingActionButton();

    }

    private void setToolbarTitleWithTotalNumberActiveUsers(){
        // we create the ref for the activeUsers
        Firebase usernameRef = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.
                ACTIVEUSERS);
        // we set a listener for this ref
        usernameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // we get the childrens of the datasnapshot as an iterable
                Iterable users =  dataSnapshot.getChildren();
                // we set a counter the count the number of times we iterate with a for loop below
                int counter = 0;
                // we iterate with each single element in the iterable
                for(Object user : users){
                    // we add 1 to the counter each time we iterate
                    counter ++;
                }
                /* we set the toolbar title with the total number we get from the for loop, and also
                 we add extra title */
                setTitle(" "+counter+" "+Constants.TITLEMAINACTIVITY);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        } */

        if (id==R.id.suspended_users) {
            Intent intent = new Intent(this, SuspendedUsersActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFloatingActionButton(){
        FloatingActionButton addNewUser = (FloatingActionButton) findViewById(R.id.addNewUser);
        addNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when click is done then display a form to fill with data that is asked
                // to the user
                View view = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.user_input_form, null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setView(view);

                // bind input views with this view, input views parameter are declared
                // at the top of this class and, "this view" refer to the view that is inflated when
                // the user click on the add button and the view is passsed as a parameter to the function)
                bindInputViews(view);

                alertBuilder.setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Capture the data from the input view (the variables of the input views
                        // are declared at the top of this class)

                        // Capture the data from inputs, and send it to Firebase
                        sendInputDataFromFormToFirebase();

                        /*
                         For the first time when we add a new user we also send the data structure for
                          monthly paid, though we send empty fields, we're going to edit these fields later
                          we send empty data for 3 years, from 2015 to 2017
                        */
                        sendMonthlyChargesFieldsToFirebase();

                    }
                });
                Dialog dialog = alertBuilder.create();
                dialog.show();
            }
        });

    }

    private void bindInputViews(View view) {
        editUserNameForm = (EditText) view.findViewById(R.id.editUserNameForm);
        billValueForm = (EditText) view.findViewById(R.id.billValueForm);
        installationChargedForm = (EditText) view.findViewById(R.id.installationChargedForm);
    }

    private void sendInputDataFromFormToFirebase(){

        // Get the string inputs from the TextEdit fields
        String strUserNameInput = editUserNameForm.getText().toString().trim();
        String strBill = billValueForm.getText().toString().trim();
        String strInstallationPayment = installationChargedForm.getText().toString().trim();

        /*
          if the input field showed to the user was not filled with at least
          one user name then  we don't invoke any action
         */
        if(!strUserNameInput.isEmpty()) {

            /*
             Build an array of elements to send to Firebase database, we first instantiate a fragment
             then after we invoke two methos in order to get the arraym first method take 7 (seven)
            parameters, each one of type String, (yes, when we register a new user for the first time
            we can send last fourth parameters as empty string)
            */
            ArrayOfInputsFormToSendFirebaseO arrayOfInputsFormToSendFirebaseO = new
                    ArrayOfInputsFormToSendFirebaseO();

            //from the instance above we invoke the methos that set the inputs
            arrayOfInputsFormToSendFirebaseO.getInputsFromForm(
                    strUserNameInput, strBill, strInstallationPayment, "","","","");


            Firebase usernameRef = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.
                    ACTIVEUSERS);
            Firebase refWithKey = usernameRef.push();
            userKey = refWithKey.getKey();
            UserNameModel userNameModel = new UserNameModel(strUserNameInput);

            refWithKey.setValue(userNameModel);


            //  we invoke the method that return an array of HashMap, we need this last one
            // to send it to Firebase database to user attributes ref
            UserAtributesModel[] userAtributesModel =
                    arrayOfInputsFormToSendFirebaseO.buildArrayOfMapToSend();

            Firebase userAttrRef = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.
                    USERS_ATRIBUTES).child(userKey);

            userAttrRef.setValue(userAtributesModel);
        }
    }

    private void sendMonthlyChargesFieldsToFirebase(){

        // Instantiate an object to build the array of Month Model objects to send to firebase
        ArrayOfMonthlyChargesFields arrayOfMonthlyChargesFields = new ArrayOfMonthlyChargesFields();

        // we invoke the method over the object instance above in order to get the array of MonthModel
        MonthModel[] monthModels = arrayOfMonthlyChargesFields.buildArrayOfMonthModelToSendToFirebase();

        // We get the reference to firebase to send the data
        Firebase ref = new Firebase(Constants.MONTLYINTERNETUSERSREF).child(Constants.USERSMONTHLYCHARGES).
                child(userKey);
        /* usersMontlyCharges take care of 3 years from 2015 to 2017, if the user need to add more
         years then here we're going to generate more references and fill it with empty data, don't
         forget to update the code to also read the data in fragments
         */
        Firebase ref2015 = ref.child("2015");
        // we send the array of MonthModel with empty data for 2015
        ref2015.setValue(monthModels);

        Firebase ref2016 = ref.child("2016");
        // again we send the array of MonthModel with empty data for 2016
        ref2016.setValue(monthModels);

        Firebase ref2017 = ref.child("2017");
        // And lastly we send the array of MonthModel with empty data for 2017
        ref2017.setValue(monthModels);
    }


}
