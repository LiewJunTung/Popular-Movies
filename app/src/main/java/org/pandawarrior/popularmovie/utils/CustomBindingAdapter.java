package org.pandawarrior.popularmovie.utils;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
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

    @BindingAdapter("bind:youtubeUrl")
    public static void loadYoutube(final Button button, final String key){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
            }
        });
    }

    @BindingAdapter("bind:webUrl")
    public static void loadWebsite(final Button button, final String url){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }

    @BindingAdapter("bind:share")
    public static void shareMovie(final Button button, final String webUrl){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("text/plain");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_SUBJECT, button.getContext().getString(R.string.text_share_subject));
                intent.putExtra(Intent.EXTRA_TEXT, button.getContext().getString(R.string.text_share_content, webUrl));
                button.getContext().startActivity(Intent.createChooser(intent, button.getContext().getString(R.string.text_how_to_share)));
            }
        });
    }
}
