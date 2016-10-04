package test.example.internetmonthlyrentcharges.models;

/**
 * Created by air on 08/09/16.
 */

public class MonthModel {
    String monthName = "";
    String monthCharge = "";
    boolean monthPay;
    boolean monthActive;

    public MonthModel() {
    }

    public MonthModel(String monthName, String monthCharge, boolean monthPay, boolean monthActive) {
        this.monthName = monthName;
        this.monthCharge = monthCharge;
        this.monthPay = monthPay;
        this.monthActive = monthActive;
    }

    public String getMonthName() {
        return monthName;
    }

    public String getMonthCharge() {
        return monthCharge;
    }

    public boolean isMonthPay() {
        return monthPay;
    }

    public boolean isMonthActive() {
        return monthActive;
    }
}
