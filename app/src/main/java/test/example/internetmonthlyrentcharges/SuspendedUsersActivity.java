package test.example.internetmonthlyrentcharges;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class SuspendedUsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspended_users);
        // we inflate the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // we set the support of action bar in the toolbar
        setSupportActionBar(toolbar);
        // we put the navegation menu to go up to the parent activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
