package io.github.kschaap1994.roosterapp.api.impl;

import java.util.List;

import io.github.kschaap1994.roosterapp.api.model.TimeTable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Kevin on 17-10-2016.
 */

public interface Schedule {

    @GET("timetables/{studentSets}")
    Call<List<TimeTable>> timeTables(
            @Path("studentSets") String studentSet);
}
