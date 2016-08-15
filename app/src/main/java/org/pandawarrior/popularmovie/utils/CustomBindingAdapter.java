package org.pandawarrior.popularmovie.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Popular Movie App
 * Created by jtlie on 8/16/2016.
 */

public class CustomBindingAdapter {
    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String url){
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }
}
