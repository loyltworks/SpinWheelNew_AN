package com.loyaltyworks.loyltyspinwheel;

import android.graphics.Bitmap;

/** Created by Sujeet on 13/06/2023. */

public class WheelItem {

    public int color;
    public Bitmap bitmap;
    public String text;

    public WheelItem(int color, Bitmap bitmap) {
        this.color = color;
        this.bitmap = bitmap;
    }

    public WheelItem(int color, Bitmap bitmap, String text) {
        this.color = color;
        this.bitmap = bitmap;
        this.text = text;
    }

    public WheelItem(int color,String text) {
        this.color = color;
        this.text = text;
    }

}
