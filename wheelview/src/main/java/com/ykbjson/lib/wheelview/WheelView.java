package com.ykbjson.lib.wheelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 包名：com.ykbjson.lib.wheelview
 * 描述：滚轮视图
 * 创建者：yankebin
 * 日期：2018/4/10
 */
public class WheelView extends FrameLayout {
    private static final String TAG = "WheelView";
    private int floatLayoutResId;
    private WheelListView wheelListView;
    private View mFloatView;
    private int wheelItemTopPadding;
    private int wheelItemBottomPadding;

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WheelView);
        floatLayoutResId = typedArray.getResourceId(R.styleable.WheelView_wheel_float_layout, -1);
        wheelItemTopPadding = typedArray.getDimensionPixelSize(R.styleable.WheelView_wheel_item_top_padding, 0);
        wheelItemBottomPadding = typedArray.getDimensionPixelSize(R.styleable.WheelView_wheel_item_bottom_padding, 0);
        typedArray.recycle();
        Log.d(TAG, "floatLayoutResId : " + floatLayoutResId);
        if (-1 == floatLayoutResId) {
            throw new IllegalArgumentException("WheelView_wheel_float_layout is invalid");
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView(getContext());
    }

    /**
     * 初始化视图
     *
     * @param context
     */
    private void initView(Context context) {
        //listview
        wheelListView = new WheelListView(context);
        wheelListView.setBackgroundColor(Color.TRANSPARENT);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        params.gravity = Gravity.CENTER;
        addView(wheelListView, params);

        //悬浮视图
        mFloatView = LayoutInflater.from(context).inflate(floatLayoutResId, this, false);
        params = new FrameLayout.LayoutParams(-1, -2);
        params.gravity = Gravity.CENTER;
        addView(mFloatView, params);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            wheelListView.setUp(mFloatView, wheelItemTopPadding, wheelItemBottomPadding);
        }
    }

    public void setAdapter(WheelAdapter adapter) {
        wheelListView.setAdapter(adapter);
    }

    public WheelListView getWheelListView() {
        return wheelListView;
    }
}
