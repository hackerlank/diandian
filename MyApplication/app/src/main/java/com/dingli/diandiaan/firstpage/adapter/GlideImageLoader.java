package com.dingli.diandiaan.firstpage.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by dingliyuangong on 2017/5/11.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
         Glide.with(context).load(path).into(imageView);
    }
}
