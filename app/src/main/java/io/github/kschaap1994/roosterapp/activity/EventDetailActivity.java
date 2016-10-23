package io.github.kschaap1994.roosterapp.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Calendar;

import io.github.kschaap1994.roosterapp.fragment.EventDetailFragment;

/**
 * Created by Kevin on 21-10-2016.
 */

public class EventDetailActivity extends BaseActivity {

    @Override
    public Fragment createFragment() {
        final Intent intent = getIntent();

        final String name = intent.getStringExtra("eventName");
        final Calendar startTime = (Calendar) intent.getSerializableExtra("startTime");
        final Calendar endTime = (Calendar) intent.getSerializableExtra("endTime");
        final String location = intent.getStringExtra("location");

        return EventDetailFragment.getInstance(name, startTime, endTime, location);
    }
}
