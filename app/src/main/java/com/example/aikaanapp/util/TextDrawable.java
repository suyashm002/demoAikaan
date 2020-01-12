package com.example.aikaanapp.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class TextDrawable extends Drawable {

    private final String mText;
    private final Paint mPaint;

    public TextDrawable(String text) {

        this.mText = text;

        this.mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(22f);
        mPaint.setAntiAlias(true);
        mPaint.setFakeBoldText(true);
        mPaint.setShadowLayer(6f, 0, 0, Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawText(mText, 0, 0, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}