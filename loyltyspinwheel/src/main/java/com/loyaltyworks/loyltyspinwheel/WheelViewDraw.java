package com.loyaltyworks.loyltyspinwheel;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import java.util.List;

/** Created by Sujeet on 13/06/2023. */

final class WheelViewDraw extends View {
    private RectF range = new RectF();  // Draw rounded rectangle using RectF
    private Paint archPaint, textPaint;
    private int padding, radius, center, mWheelBackground, mImagePadding;
    private List<WheelItem> mWheelItems;
    private OnSpinWheelTargetReach mOnSpinWheelTargetReach;
    private OnRotationListener onRotationListener;
    private float textSize;

    public WheelViewDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WheelViewDraw(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initComponents() {
        Log.d("fbrbf","fvvrfv : " + textSize);
        //arc paint object
        archPaint = new Paint();
        archPaint.setAntiAlias(true);
        archPaint.setDither(true);
        //text paint object
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setLetterSpacing(0.2f);
        textPaint.setTextSize(textSize);
        //rect rang of the arc
        range = new RectF(padding, padding, padding + radius, padding + radius);
    }

    /**
     * To Get the angele of the target
     * return Number of angle
     */
    private float getAngleOfIndexTarget(int target) {
        return (360 / mWheelItems.size()) * target;
    }

    /** Function to set wheel background */
    public void setWheelBackgoundWheel(int wheelBackground) {
        mWheelBackground = wheelBackground;
        invalidate();
    }

    /** Function to set wheel text size */
    public void setWheelTextSize(float txtSize) {
        textSize = txtSize;
        invalidate();
    }

    /** Function to set image padding */
    public void setItemsImagePadding(int imagePadding) {
        mImagePadding = imagePadding;
        invalidate();
    }

    /** Function to set wheel listener (target reach listener) */
    public void setWheelListener(OnSpinWheelTargetReach onSpinWheelTargetReach) {
        mOnSpinWheelTargetReach = onSpinWheelTargetReach;
    }

    /** Function to add wheels items */
    public void addWheelItems(List<WheelItem> wheelItems) {
        mWheelItems = wheelItems;
        invalidate();
    }

    /** Function to draw wheel background */
    private void drawWheelBackground(Canvas canvas) {
        Paint backgroundPainter = new Paint();
        backgroundPainter.setAntiAlias(true);
        backgroundPainter.setDither(true);
        backgroundPainter.setColor(mWheelBackground);
        canvas.drawCircle(center, center, center, backgroundPainter);
    }

    /**
     * Function to draw image in the center of arc
     *  canvas :   Canvas to draw
     *  tempAngle : Temporary angle
     *  bitmap :   Bitmap to draw
     */
    private void drawImage(Canvas canvas, float tempAngle, Bitmap bitmap) {
        //get every arc img width and angle
        int imgWidth = (radius / mWheelItems.size()) - mImagePadding;
        float angle = (float) ((tempAngle + 360 / mWheelItems.size() / 2) * Math.PI / 180);
        //calculate x and y
        int x = (int) (center + radius / 2 / 2 * Math.cos(angle));
        int y = (int) (center + radius / 2 / 2 * Math.sin(angle));
        //create arc to draw
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2);
        //rotate main bitmap
        float px = rect.exactCenterX();
        float py = rect.exactCenterY();
        Matrix matrix = new Matrix();
        matrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
        matrix.postRotate(tempAngle + 120);
        matrix.postTranslate(px, py);
        canvas.drawBitmap(bitmap, matrix, new Paint( Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG ));
        Log.d("sadsdsddssd" , bitmap.getWidth() + " : "+bitmap.getHeight());
        matrix.reset();
    }


    /**
     * Function to draw text below image
     * @param canvas     Canvas to draw
     * @param tempAngle  Temporary angle
     * @param sweepAngle current index angle
     * @param text       string to show
     */
    private void drawText(Canvas canvas, float tempAngle, float sweepAngle, String text) {
        Path path = new Path();
        path.addArc(range, tempAngle, sweepAngle);
        float textWidth = textPaint.measureText(text);
        int hOffset = (int) (radius * Math.PI / mWheelItems.size() / 2 - textWidth / 2);
        int vOffset = (radius / 2 / 3) - 3;
        canvas.drawTextOnPath(text, path, hOffset, vOffset, textPaint);
    }

    /**
     * Function to rotate wheel to target
     *  target  : target number
     */
    public void rotateWheelToTarget(int target) {

        float wheelItemCenter = 270 - getAngleOfIndexTarget(target) + (360 / mWheelItems.size()) / 2;
        int DEFAULT_ROTATION_TIME = 9000;                                                               /*** Time duration to rotate ***/
        animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(DEFAULT_ROTATION_TIME)
                .rotation((360 * 15) + wheelItemCenter)                                                 /*** speed to rotate ***/
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mOnSpinWheelTargetReach != null) {
                            mOnSpinWheelTargetReach.onReachTarget();
                        }
                        if (onRotationListener != null) {
                            onRotationListener.onFinishRotation();
                        }
                        clearAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }


    /**
     * Function to rotate to zero angle
     * target : target to reach
     */
    public void resetRotationLocationToZeroAngle(final int target) {
        animate().setDuration(0)
                .rotation(0).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rotateWheelToTarget(target);
                        clearAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawWheelBackground(canvas);
        initComponents();

        float tempAngle = 0;
        float sweepAngle = 360 / mWheelItems.size();

        for (int i = 0; i < mWheelItems.size(); i++) {
            Log.d("fbfhbrhr","colorCode : " + mWheelItems.get(i).color);
            archPaint.setColor(mWheelItems.get(i).color);
            canvas.drawArc(range, tempAngle, sweepAngle, true, archPaint);
//            drawImage(canvas, tempAngle, mWheelItems.get(i).bitmap);
            drawText(canvas, tempAngle, sweepAngle, mWheelItems.get(i).text == null ? "" : mWheelItems.get(i).text);
            tempAngle += sweepAngle;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        int DEFAULT_PADDING = 5;
        padding = getPaddingLeft() == 0 ? DEFAULT_PADDING : getPaddingLeft();
        radius = width - padding * 2;
        center = width / 2;
        setMeasuredDimension(width, width);
    }

    public void setOnRotationListener(OnRotationListener onRotationListener) {
        this.onRotationListener = onRotationListener;
    }
}
