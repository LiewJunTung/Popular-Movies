package org.pandawarrior.popularmovie.ui.main;

import android.view.View;

/**
 * Popular Movie App
 * Created by jtlie on 8/16/2016.
 */

public interface MovieClickHandler<T> {
    void onItemClick(T t);
}
