package io.github.kschaap1994.roosterapp.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Kevin on 25-10-2016.
 * <p>
 * Custom wrapper that supports more usage than the traditional Toast#makeText
 * <p>
 * Source: http://stackoverflow.com/questions/2220560/can-an-android-toast-be-longer-than-toast-length-long
 */

public final class Toaster {
    private String text;
    private int duration;
    private Context context;
    private int gravity;

    /**
     * Constructs a new instance of Toaster
     *
     * @param context the android context.
     */

    private Toaster(final Context context) {
        this.context = context;
    }

    public static Toaster fromContext(final Context context) {
        return new Toaster(context);
    }

    /**
     * Set the location at which the notification should appear on the screen.
     *
     * @see android.view.Gravity
     */

    public Toaster setGravity(final int gravity) {
        this.gravity = gravity;
        return this;
    }

    /**
     * Sets the text of the notification
     */

    public Toaster setText(final String text) {
        this.text = text;
        return this;
    }

    /**
     * Sets the duration in milliseconds
     */

    public Toaster setDuration(final int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Displays the notification.
     */

    public void show() {
        final Toast t = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        t.setGravity(gravity | Gravity.CENTER_HORIZONTAL, 0, 0);

        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                t.show();
            }

            @Override
            public void onFinish() {
                t.show();
            }
        }.start();
    }
}
