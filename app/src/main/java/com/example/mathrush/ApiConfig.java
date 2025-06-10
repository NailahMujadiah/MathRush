package com.example.mathrush;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiConfig {
    private static ApiConfig instance = null;
    private ApiService mathApi;

    private ApiConfig() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mathjs.org/") // base URL dummy karena kita pakai full URL
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        mathApi = retrofit.create(ApiService.class);
    }

    public static synchronized ApiConfig getInstance() {
        if (instance == null) {
            instance = new ApiConfig();
        }
        return instance;
    }

    public ApiService getMathApi() {
        return mathApi;
    }
}

