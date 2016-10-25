package io.github.kschaap1994.roosterapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import io.github.kschaap1994.roosterapp.api.impl.Schedule;
import io.github.kschaap1994.roosterapp.api.model.TimeTable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kevin on 17-10-2016.
 */

public class ScheduleService {
    private final String API_URL = "https://roosterapi.000webhostapp.com/api/";
    private final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Retrofit retrofit;
    private Gson gson;
    private List<TimeTable> timeTables;

    public ScheduleService(final String studentSet) {
        // Set date format
        gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();

        // Create the REST Adapter which points to the RoosterAPI
        retrofit = new Retrofit.Builder().baseUrl(API_URL).
                addConverterFactory(GsonConverterFactory.create(gson)).build();

        // Attach the model class to it
        final Schedule schedule = retrofit.create(Schedule.class);

        // Prepare to make the request to the API
        final Call<List<TimeTable>> call = schedule.timeTables(studentSet);

        try {
            // Make the request
            timeTables = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<TimeTable> getTimeTables() {
        return timeTables;
    }
}
