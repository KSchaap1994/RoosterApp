package io.github.kschaap1994.roosterapp.activity;

import android.support.v4.app.Fragment;

import io.github.kschaap1994.roosterapp.fragment.SettingsFragment;

/**
 * Created by Kevin on 21-10-2016.
 */

public class SettingsActivity extends BaseActivity {

    @Override
    public Fragment createFragment() {


        return new SettingsFragment();
    }
}
