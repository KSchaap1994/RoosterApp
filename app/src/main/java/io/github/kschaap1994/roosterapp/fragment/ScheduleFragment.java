package io.github.kschaap1994.roosterapp.fragment;

import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kschaap1994.roosterapp.R;
import io.github.kschaap1994.roosterapp.activity.EventDetailActivity;
import io.github.kschaap1994.roosterapp.api.ScheduleService;
import io.github.kschaap1994.roosterapp.api.model.TimeTable;
import io.github.kschaap1994.roosterapp.database.DbLab;

public class ScheduleFragment extends Fragment implements WeekView.EventClickListener,
        MonthLoader.MonthChangeListener {

    @BindView(R.id.weekView)
    public WeekView weekView;
    @BindView(R.id.avi)
    public AVLoadingIndicatorView loadingIndicatorView;

    private boolean synced = false;
    private List<WeekViewEvent> events = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this, view);

        synced = savedInstanceState != null && savedInstanceState.getBoolean("synced");

        weekView.setMonthChangeListener(this);
        weekView.setOnEventClickListener(this);

        setupDateTimeInterpreter(false);

        weekView.notifyDatasetChanged();

        getActivity().setTitle("Schedule");

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("synced", synced);
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        weekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE",
                        Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Source: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour, int minutes) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    private boolean eventMatches(final WeekViewEvent event, final int year, final int month) {
        final Calendar startTime = event.getStartTime();
        final Calendar endTime = event.getEndTime();

        return (startTime.get(Calendar.YEAR) == year && startTime.get(Calendar.MONTH) == month)
                || (endTime.get(Calendar.YEAR) == year && endTime.get(Calendar.MONTH) == month);
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        if (!synced) {
            new TimeTableTask().execute();
            synced = true;
        }

        final List<WeekViewEvent> matchedEvents = new ArrayList<>();
        for (WeekViewEvent event : events) {
            if (eventMatches(event, newYear, newMonth - 1)) { // -1, because month starts at index 0.
                matchedEvents.add(event);
            }
        }
        return matchedEvents;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        final Intent intent = new Intent(getActivity(), EventDetailActivity.class);

        intent.putExtra("eventName", event.getName());
        intent.putExtra("startTime", event.getStartTime());
        intent.putExtra("endTime", event.getEndTime());
        intent.putExtra("location", event.getLocation());

        startActivity(intent);
    }

    private class TimeTableTask extends AsyncTask<String, Void, List<TimeTable>> {

        @Override
        protected void onPreExecute() {
            loadingIndicatorView.show();
        }

        @Override
        protected List<TimeTable> doInBackground(String... params) {
            final DbLab lab = DbLab.get(getActivity());
            final ScheduleService scheduleService = new ScheduleService(lab.getSetting("class"));
            return scheduleService.getTimeTables();
        }

        @Override
        protected void onPostExecute(List<TimeTable> result) {
            if (result == null) return;

            for (TimeTable timeTable : result) {
                events.add(timeTable.toWeekViewEvent());
            }

            weekView.notifyDatasetChanged();
            loadingIndicatorView.hide();
        }
    }
}