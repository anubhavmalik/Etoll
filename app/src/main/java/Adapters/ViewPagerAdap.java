package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.anubhavmalikdeveloper.e_toll.Fragments.ShowQRFragment;
import com.anubhavmalikdeveloper.e_toll.Fragments.StartTripFragment;

/**
 * Created by kushalgupta on 10/12/17.
 * 
 */

public class ViewPagerAdap extends FragmentPagerAdapter {
    public ViewPagerAdap(FragmentManager fm) {


        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
       if (position==0)
        return new StartTripFragment();

       else{
           return new ShowQRFragment();
       }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
