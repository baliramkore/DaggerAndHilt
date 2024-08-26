package com.rbk.daggerandhilt;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StackOverFLowAPI {

    @GET("/2.3/questions?order=desc&sort=activity&site=stackoverflow")
    Call<QuestionsListResponseSchema> lastActiveQuestion(@Query("pagesize")Integer pageSize);

}
