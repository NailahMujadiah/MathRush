package com.example.mathrush;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {
    @GET
    Call<ResponseBody> getAnswer(@Url String fullUrl);
}
