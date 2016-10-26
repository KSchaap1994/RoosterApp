package io.github.kschaap1994.roosterapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.kschaap1994.roosterapp.R;
import io.github.kschaap1994.roosterapp.activity.LocationActivity;

/**
 * Created by Kevin on 21-10-2016.
 */

public class EventDetailFragment extends Fragment implements View.OnTouchListener {

    @BindView(R.id.event_name)
    public TextView nameTextView;
    @BindView(R.id.start_time)
    public TextView startTimeTextView;
    @BindView(R.id.end_time)
    public TextView endTimeTextView;
    @BindView(R.id.location)
    public TextView locationTextView;

    private float downX, downY, upX, upY;
    private final int MIN_DISTANCE = 80;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    public static EventDetailFragment getInstance(final String name, final Calendar startTime,
                                                  final Calendar endTime, final String location) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putSerializable("startTime", startTime);
        args.putSerializable("endTime", endTime);
        args.putString("location", location);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            final Bundle bundle = getArguments();
            final String name = bundle.getString("name");
            final Calendar startTime = (Calendar) bundle.getSerializable("startTime");
            final Calendar endTime = (Calendar) bundle.getSerializable("endTime");
            final String location = bundle.getString("location");

            nameTextView.setText(name);
            startTimeTextView.setText(formatTime(startTime));
            endTimeTextView.setText(formatTime(endTime));
            locationTextView.setText(location);

            getActivity().setTitle(name);

            view.setOnTouchListener(this);
        }

        return view;
    }

    private String formatTime(final Calendar time) {
        final int hour = time.get(Calendar.HOUR_OF_DAY);
        final int minute = time.get(Calendar.MINUTE);

        return String.format("%s:%s", (hour >= 10) ? hour : "0" + hour,
                (minute >= 10) ? minute : "0" + minute);
    }

    @OnClick(R.id.location)
    public void viewLocation() {
        final Intent intent = new Intent(getActivity(), LocationActivity.class);
        final String location = locationTextView.getText().subSequence(0, 3).toString();
        intent.putExtra("location", location);

        startActivity(intent);
    }

    private void nextEvent() {
        Toast.makeText(getActivity(), "next event",
                Toast.LENGTH_SHORT).show();
    }

    private void previousEvent() {
        Toast.makeText(getActivity(), "previous event",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();

                return true;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // Handle horizontal right swipe
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        if (deltaX > 0) {
                            nextEvent();
                            return true;
                        }
                        if (deltaX < 0) {
                            previousEvent();
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
        }
        return false;
    }
}