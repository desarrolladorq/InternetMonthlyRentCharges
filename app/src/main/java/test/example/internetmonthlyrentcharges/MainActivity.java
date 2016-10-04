package test.example.internetmonthlyrentcharges;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.firebase.client.Firebase;

import test.example.internetmonthlyrentcharges.constants.Constants;
import test.example.internetmonthlyrentcharges.models.UserAtributesModel;
import test.example.internetmonthlyrentcharges.models.UserNameModel;
import test.example.internetmonthlyrentcharges.semimodels.ArrayOfInputsFormToSendFirebaseO;

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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* helper function to set actions When we click over the plus sign button placed at the
         right end bottom of the user screen */
        setFloatingActionButton();
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
        if (id == R.id.action_settings) {
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
                        //sendMonthlyChargesFieldsToFirebase();

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


}
