package de.caroliwo.hawoe_rallye;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DownloadJSONRetrofit {

    //GET Task /api/task/byGroup?group={group}
    @GET("task/byGroup")
    Call<TaskAPI> getTasks(@Query("group") int groupID);

}
