package org.pandawarrior.popularmovie.data;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * Popular Movie App
 * Created by jtlie on 8/15/2016.
 */

public class PagedResponse<T> {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private T t;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    @NotNull
    public T getData() {
        return t;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
