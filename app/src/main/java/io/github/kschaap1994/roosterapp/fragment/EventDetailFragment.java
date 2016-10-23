package io.github.kschaap1994.roosterapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.kschaap1994.roosterapp.R;
import io.github.kschaap1994.roosterapp.activity.LocationActivity;

/**
 * Created by Kevin on 21-10-2016.
 */

public class EventDetailFragment extends Fragment {

    @BindView(R.id.event_name)
    public TextView nameTextView;
    @BindView(R.id.start_time)
    public TextView startTimeTextView;
    @BindView(R.id.end_time)
    public TextView endTimeTextView;
    @BindView(R.id.location)
    public TextView locationTextView;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            startTimeTextView.setText(startTime.toString());
            endTimeTextView.setText(endTime.toString());
            locationTextView.setText(location);
        }
        return view;
    }

    @OnClick(R.id.location)
    public void viewLocation() {
        final Intent intent = new Intent(getActivity(), LocationActivity.class);
        final String location = locationTextView.getText().subSequence(0, 3).toString();
        intent.putExtra("location", location);
        startActivity(intent);
    }
}
