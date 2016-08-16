package org.pandawarrior.popularmovie.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.pandawarrior.popularmovie.R;

/**
 * Popular Movie App
 * Created by jtlie on 8/16/2016.
 * from: https://developer.android.com/topic/libraries/data-binding/index.html
 */

public class CustomBindingAdapter {
    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String url){
        Picasso.with(imageView.getContext()).load(url).error(R.drawable.placeholder_grey).into(imageView);
    }
}
