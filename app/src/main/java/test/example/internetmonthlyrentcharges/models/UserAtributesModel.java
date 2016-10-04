package test.example.internetmonthlyrentcharges.models;

/**
 * Created by air on 14/09/16.
 */

public class UserAtributesModel {
    String atributeName;
    Object atributeValue;

    public UserAtributesModel() {
    }

    public UserAtributesModel(String atributeName, Object atributeValue) {
        this.atributeName = atributeName;
        this.atributeValue = atributeValue;
    }

    public String getAtributeName() {
        return atributeName;
    }

    public Object getAtributeValue() {
        return atributeValue;
    }


}
