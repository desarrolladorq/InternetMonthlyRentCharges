package test.example.internetmonthlyrentcharges.semimodels;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.ServerValue;

import test.example.internetmonthlyrentcharges.models.UserAtributesModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArrayOfInputsFormToSendFirebaseO extends Fragment {
    String username;
    String bill;
    String installationPayment;
    String numberPaids;
    String totalAmountPaids;
    String initialBudget;
    String date;

    public ArrayOfInputsFormToSendFirebaseO() {
        // Required empty public constructor
    }

    // set variables with input data
    public void getInputsFromForm(String usernameInput, String billInput, String installationPaymentInput,
                                  String numberPaidsInput, String totalAmountPaidsInput, String initialBudgetInput,
                                  String dateInput){
        username = usernameInput;
        bill = billInput;
        installationPayment = installationPaymentInput;
        numberPaids = numberPaidsInput;
        totalAmountPaids = totalAmountPaidsInput;
        initialBudget = initialBudgetInput;
        date = dateInput;
    }




    public UserAtributesModel[] buildArrayOfMapToSend(){
        UserAtributesModel userAtrName = new UserAtributesModel("Nombre del cliente", username);
        UserAtributesModel userAtrBill = new UserAtributesModel("Paga mensualmente", bill);
        UserAtributesModel userAtrInstallationPayment = new UserAtributesModel("Se cobr\u00f3 instalaci\u00f3n", installationPayment);
        UserAtributesModel userAtrNumberPaids = new UserAtributesModel("Numero de pagos", numberPaids);
        UserAtributesModel userAtrTotalAmountOfPaids = new UserAtributesModel("Ingreso por pagos", totalAmountPaids);
        UserAtributesModel userAtrInitialBudget = new UserAtributesModel("Dinero invertido", initialBudget);
        UserAtributesModel userAtrDate = new UserAtributesModel("Fecha de instalaci\u00f3n", date);
        UserAtributesModel userAtrTimeStampCreated = new UserAtributesModel("Se registr\u00f3 en el sistema",ServerValue.TIMESTAMP);

        UserAtributesModel[] userAtributesModel  = new  UserAtributesModel[8];
        userAtributesModel[0] = userAtrName;
        userAtributesModel[1] = userAtrBill;
        userAtributesModel[2] = userAtrInstallationPayment;
        userAtributesModel[3] = userAtrNumberPaids;
        userAtributesModel[4] = userAtrTotalAmountOfPaids;
        userAtributesModel[5] = userAtrInitialBudget;
        userAtributesModel[6] = userAtrDate;
        userAtributesModel[7] = userAtrTimeStampCreated;

        return  userAtributesModel;
    }


}
