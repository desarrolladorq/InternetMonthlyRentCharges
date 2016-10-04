package test.example.internetmonthlyrentcharges.semimodels;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.example.internetmonthlyrentcharges.models.MonthModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArrayOfMonthlyChargesFields extends Fragment {

    public ArrayOfMonthlyChargesFields() {
        // Required empty public constructor
    }


    MonthModel monthModelJan = new MonthModel("Enero", "0", false, true);
    MonthModel monthModelFeb = new MonthModel("Febrero", "0", false, true);
    MonthModel monthModelMar = new MonthModel("Marzo", "0", false, true);
    MonthModel monthModelApr = new MonthModel("Abril", "0", false, true);
    MonthModel monthModelMay = new MonthModel("Mayo", "0", false, true);
    MonthModel monthModelJun = new MonthModel("Junio", "0", false, true);
    MonthModel monthModelJul = new MonthModel("Julio", "0", false, true);
    MonthModel monthModelAug = new MonthModel("Agosto", "0", false, true);
    MonthModel monthModelSep = new MonthModel("Septiembre", "0",false, true);
    MonthModel monthModelOct = new MonthModel("Octubre", "0", false, true);
    MonthModel monthModelNov = new MonthModel("Noviembre", "0", false, true);
    MonthModel monthModelDec = new MonthModel("Diciembre", "0", false, true);



    public MonthModel[] buildArrayOfMonthModelToSendToFirebase(){

        MonthModel[] monthlyCharges  = new  MonthModel[12];

        monthlyCharges[0] = monthModelJan;
        monthlyCharges[1] = monthModelFeb;
        monthlyCharges[2] = monthModelMar;
        monthlyCharges[3] = monthModelApr;
        monthlyCharges[4] = monthModelMay;
        monthlyCharges[5] = monthModelJun;
        monthlyCharges[6] = monthModelJul;
        monthlyCharges[7] = monthModelAug;
        monthlyCharges[8] = monthModelSep;
        monthlyCharges[9] = monthModelOct;
        monthlyCharges[10] = monthModelNov;
        monthlyCharges[11] = monthModelDec;


        return monthlyCharges;

    }


}
