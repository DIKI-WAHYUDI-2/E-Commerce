package com.example.e_commerce.data.retrofit;

import com.example.e_commerce.BuildConfig;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfigAuthenticated {

    public static ApiService getApiService(final String username, final String password) {
        HttpLoggingInterceptor loggingInterceptor;

        if (BuildConfig.DEBUG) {
            loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        // Tambahkan header Authorization
                        String credentials = Credentials.basic(username, password);
                        Request modifiedRequest = originalRequest.newBuilder()
                                .header("Authorization", credentials)
                                .build();

                        return chain.proceed(modifiedRequest);
                    }
                })
                .build();

        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://192.168.1.117:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(ApiService.class);
    }
}
