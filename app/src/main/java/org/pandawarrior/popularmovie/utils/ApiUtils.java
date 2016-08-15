package org.pandawarrior.popularmovie.utils;

import android.support.annotation.IntDef;

import org.pandawarrior.popularmovie.ApiService;
import org.pandawarrior.popularmovie.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Popular Movie App
 * Created by jtlie on 8/16/2016.
 */

public class ApiUtils {
    @IntDef({API_POPULAR, API_TOP_RATED})
    public @interface ApiFilter {}

    public static final int API_TOP_RATED = 0;
    public static final int API_POPULAR = 1;

    private static HttpLoggingInterceptor loggingInterceptor() {
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static OkHttpClient getInterceptor(){
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        if (chain.request().method().equals("GET")){
                            HttpUrl url = chain
                                    .request()
                                    .url()
                                    .newBuilder()
                                    .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                                    .build();
                            return chain.proceed(chain.request().newBuilder().url(url).build());
                        }
                        return chain.proceed(chain.request());
                    }
                }).build();
    }

    public static ApiService initApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org")
                .client(getInterceptor())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }
}
