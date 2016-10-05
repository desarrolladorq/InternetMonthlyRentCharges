package test.example.internetmonthlyrentcharges;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import test.example.internetmonthlyrentcharges.adapters.SimpleFragmentPagerAdapter;
import test.example.internetmonthlyrentcharges.fragments.MonthsFragment;
import test.example.internetmonthlyrentcharges.fragments.MoreInfoFragment;

public class DetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setUpViewPager();

        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
    }

    private ArrayList<Fragment> addCategoryFragments(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MonthsFragment());
        fragments.add(new MoreInfoFragment());;
        return fragments;
    }

    private void setUpViewPager(){
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), addCategoryFragments()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Mensualidades");
        tabLayout.getTabAt(1).setText("Mas informacion");;

    }


}
