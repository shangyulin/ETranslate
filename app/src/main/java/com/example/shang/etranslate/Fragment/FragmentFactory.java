package com.example.shang.etranslate.Fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shang on 2017/4/1.
 */
public class FragmentFactory {

    private static Map<Integer, Fragment> map = new HashMap<Integer, Fragment>();

    public static Fragment getFragment(int position) {
        Fragment fragment = null;
        fragment = map.get(position);
        if (fragment == null) {
            if (position == 0) {
                fragment = new TranslateFragment();
            } else if (position == 1){
                fragment = new InformationFragment();
            }
        }
        return fragment;
    }
}
