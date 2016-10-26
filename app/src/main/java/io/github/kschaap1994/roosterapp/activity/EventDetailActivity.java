package io.github.kschaap1994.roosterapp.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;
import java.util.List;

import io.github.kschaap1994.roosterapp.R;
import io.github.kschaap1994.roosterapp.fragment.EventDetailFragment;
import io.github.kschaap1994.roosterapp.util.CustomWeekViewEvent;

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
        final List<CustomWeekViewEvent> events = (List<CustomWeekViewEvent>)
                intent.getSerializableExtra("events");
        final int id = intent.getIntExtra("id", -1);
        final boolean anim = intent.getBooleanExtra("anim", true);

        return EventDetailFragment.getInstance(name, startTime, endTime, location, events, id, anim);
    }
}
