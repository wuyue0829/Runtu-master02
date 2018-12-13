package com.mac.runtu.utils;

import android.support.v4.app.Fragment;

import com.mac.runtu.fragment.AllOrderFragment;
import com.mac.runtu.fragment.FinishOrderFragment;
import com.mac.runtu.fragment.WaitGetGoodsFragment;
import com.mac.runtu.fragment.WaitPayFragment;
import com.mac.runtu.fragment.WaitSetGoodsFragment;

import java.util.HashMap;


public class FragmentFactory {

    public static HashMap<Integer, Fragment> orderFragment = new
            HashMap<Integer, Fragment>();


    private static HashMap<Integer, Fragment> experFarmOrderFragment = new
            HashMap<Integer, Fragment>();


    public static Fragment getOrderFragment(int position) {

        Fragment fragment = orderFragment.get(position);

        if (fragment == null) {

            switch (position) {
                case 0:

                    fragment = new AllOrderFragment();
                    break;
                case 1:

                    fragment = new WaitPayFragment();
                    break;

                case 2:

                    fragment = new WaitSetGoodsFragment();

                    break;
                case 3:

                    fragment = new WaitGetGoodsFragment();
                    break;
                case 4:

                    fragment = new FinishOrderFragment();
                    break;
            }
            orderFragment.put(position, fragment);
        }

        return fragment;
    }

    public static Fragment getCreatOrderFragment(int position) {

        return orderFragment.get(position);
    }

}
