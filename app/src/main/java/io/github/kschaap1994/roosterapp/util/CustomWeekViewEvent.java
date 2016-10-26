package io.github.kschaap1994.roosterapp.util;

import com.alamkanak.weekview.WeekViewEvent;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Kevin on 26-10-2016.
 */

public class CustomWeekViewEvent extends WeekViewEvent implements Serializable {

    private String name;
    private Calendar startTime;
    private Calendar endTime;
    public String location;
    private boolean allDay;
    private long id;

    public CustomWeekViewEvent() {
        super();
    }

    @Override
    public Calendar getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    @Override
    public Calendar getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean isAllDay() {
        return allDay;
    }

    @Override
    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }
}