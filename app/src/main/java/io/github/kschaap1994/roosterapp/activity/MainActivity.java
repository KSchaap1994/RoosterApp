package io.github.kschaap1994.roosterapp.activity;

import android.support.v4.app.Fragment;

import io.github.kschaap1994.roosterapp.fragment.PlaceholderFragment;
import io.github.kschaap1994.roosterapp.fragment.ScheduleFragment;

/**
 * Created by Kevin on 22-10-2016.
 */

public class MainActivity extends BaseActivity {

    @Override
    public Fragment createFragment() {
        if (lab.hasSettings()) {
            return new ScheduleFragment();
        } else {
            return new PlaceholderFragment();
        }
    }
}
