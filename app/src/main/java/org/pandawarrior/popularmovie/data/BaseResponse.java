package org.pandawarrior.popularmovie.data;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * Popular Movie App
 * Created by jtlie on 8/15/2016.
 */

public class BaseResponse<T> {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private T t;

    public int getId() {
        return id;
    }

    @NotNull
    public T getData() {
        return t;
    }

}
