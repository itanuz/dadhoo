package com.dadhoo.activities.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;




/**
 * This class extend RelativeLayout adding a grey border
 * @author gaecarme
 *
 */
public class RelativeLayoutOutlined extends RelativeLayout {

    public RelativeLayoutOutlined(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        setWillNotDraw(false) ;
    }
    public RelativeLayoutOutlined(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        setWillNotDraw(false) ;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        /*
        Paint fillPaint = new Paint();
        fillPaint.setARGB(255, 0, 255, 0);
        fillPaint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(fillPaint) ;
        */

        Paint strokePaint = new Paint();
        strokePaint.setARGB(255, 128, 128, 128);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(2);  
        Rect r = canvas.getClipBounds() ;
        Rect outline = new Rect( 1,1,r.right-1, r.bottom-1) ;
        canvas.drawRect(outline, strokePaint) ;
    }

}
