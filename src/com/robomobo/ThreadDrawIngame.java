package com.robomobo;

import android.content.res.Resources;
import android.graphics.*;
import android.view.SurfaceHolder;
import com.robomobo.R;

/**
 * Created by Roman on 10.01.14.
 */
public class ThreadDrawIngame extends Thread
{
    private boolean runFlag = false;
    private SurfaceHolder surfaceHolder;
    private Bitmap picture;
    private Matrix matrix;
    private long prevTime;

    public ThreadDrawIngame(SurfaceHolder surfaceHolder, Resources resources)
    {
        this.surfaceHolder = surfaceHolder;

        picture = BitmapFactory.decodeResource(resources, R.drawable.icon);
        matrix = new Matrix();
        matrix.postScale(3.0f, 3.0f);
        matrix.postTranslate(100.0f, 100.0f);
        prevTime = System.currentTimeMillis();
    }

    public void setRunning(boolean run)
    {
        runFlag = run;
    }

    @Override
    public void run()
    {
        Canvas canvas;
        while (runFlag)
        {
            long now = System.currentTimeMillis();
            long elapsedTime = now - prevTime;
            if (elapsedTime > 30)
            {
                prevTime = now;
                matrix.preRotate(2.0f, picture.getWidth() / 2, picture.getHeight() / 2);
            }
            canvas = null;
            try
            {
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder)
                {
                    canvas.drawColor(Color.BLACK);
                    canvas.drawBitmap(picture, matrix, null);
                    /*
                      _____                       _______ _     _                   _    _
                     |  __ \                     |__   __| |   (_)                 | |  | |
                     | |  | |_ __ __ ___      __    | |  | |__  _ _ __   __ _ ___  | |__| | ___ _ __ ___
                     | |  | | '__/ _` \ \ /\ / /    | |  | '_ \| | '_ \ / _` / __| |  __  |/ _ \ '__/ _ \
                     | |__| | | | (_| |\ V  V /     | |  | | | | | | | | (_| \__ \ | |  | |  __/ | |  __/
                     |_____/|_|  \__,_| \_/\_/      |_|  |_| |_|_|_| |_|\__, |___/ |_|  |_|\___|_|  \___|
                                                                         __/ |
                                                                        |___/
                     */
                }
            }
            finally
            {
                if (canvas != null)
                {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
