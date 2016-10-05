package test.example.internetmonthlyrentcharges.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by air on 13/08/16.
 */

public class SimpleFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments;

    public SimpleFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
