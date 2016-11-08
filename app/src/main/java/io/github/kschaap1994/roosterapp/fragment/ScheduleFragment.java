package io.github.kschaap1994.roosterapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.kschaap1994.roosterapp.R;
import io.github.kschaap1994.roosterapp.activity.EventDetailActivity;
import io.github.kschaap1994.roosterapp.activity.SettingsActivity;
import io.github.kschaap1994.roosterapp.api.ScheduleService;
import io.github.kschaap1994.roosterapp.api.model.TimeTable;
import io.github.kschaap1994.roosterapp.database.DbLab;
import io.github.kschaap1994.roosterapp.util.CustomWeekViewEvent;
import io.github.kschaap1994.roosterapp.util.Toaster;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ScheduleFragment extends Fragment implements WeekView.EventClickListener,
        MonthLoader.MonthChangeListener {

    @BindView(R.id.weekView)
    public WeekView weekView;
    @BindView(R.id.avi)
    public AVLoadingIndicatorView loadingIndicatorView;
    @BindView(R.id.empty_layout_subtext)
    public TextView subtext;
    @BindView(R.id.empty_layout_header)
    public TextView header;
    @BindView(R.id.empty_layout_image)
    public ImageView image;

    private boolean synced = false;
    private List<CustomWeekViewEvent> events = new ArrayList<>();
    private DbLab lab;

    private final int ADD_SCHEDULE_REQUEST = 200;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        lab = DbLab.get(getActivity());
    }

    private void hideViews() {
        if (lab.hasSettings()) {
            subtext.setVisibility(GONE);
            header.setVisibility(GONE);
            image.setVisibility(GONE);

            weekView.setVisibility(VISIBLE);
            loadingIndicatorView.setVisibility(VISIBLE);
        } else {
            weekView.setVisibility(GONE);
            loadingIndicatorView.setVisibility(GONE);

            subtext.setVisibility(VISIBLE);
            header.setVisibility(VISIBLE);
            image.setVisibility(VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        ButterKnife.bind(this, view);

        hideViews();

        synced = savedInstanceState != null && savedInstanceState.getBoolean("synced");

        weekView.setMonthChangeListener(this);
        weekView.setOnEventClickListener(this);

        setupDateTimeInterpreter(false);

        weekView.notifyDatasetChanged();
        getActivity().setTitle("Schedule");

        weekView.goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)); //goes to current hour

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_SCHEDULE_REQUEST) {
                hideViews();
            }
        }
    }

    @OnClick(R.id.empty_layout_subtext)
    public void addSchedule() {
        final Intent intent = new Intent(getActivity(), SettingsActivity.class);
        intent.putExtra("firstTime", true);
        startActivityForResult(intent, ADD_SCHEDULE_REQUEST);
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
        final CustomWeekViewEvent customEvent = (CustomWeekViewEvent) event;

        intent.putExtra("eventName", customEvent.getName());
        intent.putExtra("startTime", customEvent.getStartTime());
        intent.putExtra("endTime", customEvent.getEndTime());
        intent.putExtra("location", customEvent.getLocation());
        intent.putExtra("events", (Serializable) events);
        intent.putExtra("id", events.indexOf(customEvent));

        intent.putExtra("anim", false);

        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_schedule, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.today) {
            weekView.goToToday();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class TimeTableTask extends AsyncTask<String, Void, List<TimeTable>> {

        @Override
        protected void onPreExecute() {
            loadingIndicatorView.show();
        }

        @Override
        protected List<TimeTable> doInBackground(String... params) {
            final DbLab lab = DbLab.get(getActivity());
            System.out.println(lab.hasSettings());
            System.out.println(lab.getSettings().size());

            for (String key : lab.getSettings().keySet()) {
                System.out.println(key);
            }

            //System.out.println(lab.getSetting("class"));

            final ScheduleService scheduleService =
                    new ScheduleService(lab.getSetting("class"));
            return scheduleService.getTimeTables();
        }

        @Override
        protected void onPostExecute(List<TimeTable> result) {
            if (result == null) { // The API didn't give any JSON data
                Toaster.
                        fromContext(getActivity()).
                        setText("Failed to reach the API. Please try to restart " +
                                "the application").
                        setGravity(Gravity.BOTTOM).
                        setDuration(2500). // 2.5 seconds
                        show();

                loadingIndicatorView.hide();
                return;
            }

            for (TimeTable timeTable : result) {
                events.add(timeTable.toWeekViewEvent());
            }

            weekView.notifyDatasetChanged();
            loadingIndicatorView.hide();
        }
    }
}