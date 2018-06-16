package com.application.handing.vateapp;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;


public class Fragments {

    public static void changeMenuItem(FragmentActivity activity, int pos) {
        Menu menu = ((NavigationView)(activity.findViewById(R.id.nav_view))).getMenu();
        int length = menu.size();
        for (int i = 0; i < length; i++) menu.getItem(pos).setChecked(false);
        menu.getItem(pos).setChecked(true);
    }

    private static void setFragment(FragmentManager fragmentManager, Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
    }

    public static void setFragment(FragmentActivity activity, Fragment fragment) {
        setFragment(activity.getSupportFragmentManager(), fragment);
    }

    public static void setFragment(FragmentActivity activity, Fragment fragment, String key) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragment, key);
        ft.addToBackStack(key);
        ft.commit();
    }

    public static void popSetFragment(FragmentActivity activity, Fragment fragment) {
        FragmentManager fm = activity.getSupportFragmentManager();
        if (fm.getBackStackEntryCount()>0)
            fm.popBackStackImmediate();
        else {
            setFragment(fm, fragment);
        }
    }

    public static void popAllFragments(FragmentActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}

