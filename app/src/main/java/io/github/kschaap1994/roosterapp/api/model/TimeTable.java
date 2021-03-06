package io.github.kschaap1994.roosterapp.api.model;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.annotations.Expose;

import java.util.Calendar;
import java.util.Date;

import io.github.kschaap1994.roosterapp.util.CustomWeekViewEvent;

/**
 * Created by Kevin on 17-10-2016.
 */

public class TimeTable {
    @Expose
    private final int id;

    @Expose
    private final String description;

    @Expose
    private final String tutors;

    @Expose
    private final Date startTime;

    @Expose
    private final Date endTime;

    @Expose
    private final boolean allDay;

    @Expose
    private final String location;

    @Expose
    private final String studentSets;

    public TimeTable(int id, String description, String tutors, Date startTime,
                     Date endTime, boolean allDay, String location, String studentSets) {
        this.id = id;
        this.description = description;
        this.tutors = tutors;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allDay = allDay;
        this.location = location;
        this.studentSets = studentSets;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTutors() {
        return tutors;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public String getLocation() {
        return location;
    }

    public String getStudentSets() {
        return studentSets;
    }

    public CustomWeekViewEvent toWeekViewEvent() {
        final Calendar start = Calendar.getInstance();
        start.setTime(getStartTime());

        final Calendar end = (Calendar) start.clone();
        end.setTime(getEndTime());

        final CustomWeekViewEvent event = new CustomWeekViewEvent();
        event.setName(getDescription());
        event.setStartTime(start);
        event.setEndTime(end);
        event.setLocation(getLocation());

        return event;
    }
}
