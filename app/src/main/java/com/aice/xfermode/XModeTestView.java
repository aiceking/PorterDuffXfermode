package com.aice.xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class XModeTestView extends View {
    private static final PorterDuff.Mode[] sModes = {
            PorterDuff.Mode.CLEAR,      // 清空所有，要闭硬件加速，否则无效
            PorterDuff.Mode.SRC,        // 显示前都图像，不显示后者
            PorterDuff.Mode.DST,        // 显示后者图像，不显示前者
            PorterDuff.Mode.SRC_OVER,   // 后者叠于前者
            PorterDuff.Mode.DST_OVER,   // 前者叠于后者
            PorterDuff.Mode.SRC_IN,     // 显示相交的区域，但图像为后者
            PorterDuff.Mode.DST_IN,     // 显示相交的区域，但图像为前者
            PorterDuff.Mode.SRC_OUT,    // 显示后者不重叠的图像
            PorterDuff.Mode.DST_OUT,    // 显示前者不重叠的图像
            PorterDuff.Mode.SRC_ATOP,   // 显示前者图像，与后者重合的图像
            PorterDuff.Mode.DST_ATOP,   // 显示后者图像，与前者重合的图像
            PorterDuff.Mode.XOR,        // 显示持有不重合的图像
            PorterDuff.Mode.DARKEN,     // 后者叠于前者上，后者与前者重叠的部份透明。要闭硬件加速，否则无效
            PorterDuff.Mode.LIGHTEN,    // 前者叠于前者，前者与后者重叠部份透明。要闭硬件加速，否则无效
            PorterDuff.Mode.MULTIPLY,   // 显示重合的图像，且颜色会合拼
            PorterDuff.Mode.SCREEN    // 显示持有图像，重合的会变白
    };
    private Paint mPaint;
    private Bitmap mSrcBitmap;
    private Bitmap mDstBitmap;
    private Canvas mSrcCanvas;
    private Canvas mDstCanvas;
    private int mIndex;

    public XModeTestView(Context context) {
        this(context, null);
    }

    public XModeTestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XModeTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initBitmap();
        int width = getWidth();
        int height = getHeight();
        int layerId = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(mSrcBitmap, width / 4f, height / 4f, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(sModes[mIndex]));
        canvas.drawBitmap(mDstBitmap, width / 2.5f, height / 2.5f, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    private void initBitmap() {
        if (mSrcBitmap == null) {
            int width = getWidth() / 5;
            mSrcBitmap = Bitmap.createBitmap(width * 2, width * 2, Bitmap.Config.ARGB_8888);
            mDstBitmap = Bitmap.createBitmap(width * 2, width * 2, Bitmap.Config.ARGB_8888);
            mSrcCanvas = new Canvas(mSrcBitmap);
            mPaint.setColor(Color.RED);
            mSrcCanvas.drawCircle(width, width, width, mPaint);
            mDstCanvas = new Canvas(mDstBitmap);
            mPaint.setColor(Color.GREEN);
            mDstCanvas.drawRect(0, 0, width * 2, width * 2, mPaint);
        }
    }

    public String getXMode() {
        return sModes[mIndex].name();
    }

    public void changeXMode() {
        mIndex++;
        if (mIndex == sModes.length) {
            mIndex = 0;
        }
        invalidate();
    }

}
