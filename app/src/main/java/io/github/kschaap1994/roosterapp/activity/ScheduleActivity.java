package io.github.kschaap1994.roosterapp.activity;

import android.support.v4.app.Fragment;

import io.github.kschaap1994.roosterapp.fragment.ScheduleFragment;

/**
 * Created by Kevin on 21-10-2016.
 */

public class ScheduleActivity extends BaseActivity {

    @Override
    public Fragment createFragment() {
        return new ScheduleFragment();
    }

}
