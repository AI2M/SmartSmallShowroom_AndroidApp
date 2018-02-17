package com.example.tongchaitonsau.smartsmallshowroom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by tongchaitonsau on 30/10/2017 AD.
 */

public class PagerAdapter extends FragmentStatePagerAdapter{
    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm , int NumberOfTabs){
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public  Fragment getItem(int position){
        switch (position)
        {
            case 0:
                Design design = new Design();
                return  design;
            case 1:
                Main main = new Main();
                return main;
            case 2:
                Control control = new Control();
                return control;
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return mNoOfTabs;
    }

}
