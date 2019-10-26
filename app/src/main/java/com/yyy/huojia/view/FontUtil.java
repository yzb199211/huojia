package com.yyy.huojia.view;

import android.content.Context;
import android.graphics.Typeface;


public class FontUtil {
    public static Typeface getTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
    }

}
