package com.ykbjson.lib.wheelview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 包名：com.ykbjson.lib.wheelview
 * 描述：类似WheelView的ListView
 * 创建者：yankebin
 * 日期：2018/4/10
 */
public class WheelListView extends ListView {
    private static final String TAG = "WheelListView";
    private static final int POSITION_OFFSET_PREFIX = 30;
    private final Object SCROLL_LOCK = new Object();

    private int topY;
    private int middleY;
    private int bottomY;
    private int selectPosition;
    private boolean fromTouch;
    private WheelAdapter wheelAdapter;
    private OnScrollCallback callback;


    public WheelListView(Context context) {
        this(context, null);
    }

    public WheelListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //必需的两个属性
        setClipChildren(false);
        setClipToPadding(false);
    }

    public void setAdapter(WheelAdapter adapter) {
        super.setAdapter(adapter);
        wheelAdapter = adapter;
        setCallback(adapter);
    }

    private void setCallback(OnScrollCallback callback) {
        this.callback = callback;
    }

    /**
     * 初始化
     *
     * @param selectView
     */
    protected void setUp(View selectView, View rootView) {
        if (null == selectView || null == rootView) {
            return;
        }
        //让listview真正的显示区域的顶部与悬浮框顶部对其
        setPadding(0, selectView.getTop() - POSITION_OFFSET_PREFIX, 0,
                rootView.getBottom() - selectView.getBottom() + POSITION_OFFSET_PREFIX);

        //获取悬浮框在屏幕的绝对位置
        int location[] = new int[2];
        selectView.getLocationOnScreen(location);

        topY = location[1];
        middleY = topY + selectView.getMeasuredHeight() / 2;
        bottomY = topY + selectView.getMeasuredHeight();

        setUpScroll();
    }

    /**
     * 设置滚动监听
     */
    private void setUpScroll() {
        setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (null == callback) {
                    return;
                }
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (!fromTouch) {
                        return;
                    }
                    fromTouch = false;
                    Log.d(TAG, "SCROLL_STATE_IDLE");
                    //adapter实现了callback接口
                    callback.onHandleIdle(WheelListView.this, selectPosition);
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    fromTouch = true;
                    Log.d(TAG, "SCROLL_STATE_TOUCH_SCROLL");
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (null == callback) {
                    return;
                }
                if (!fromTouch) {
                    return;
                }
                synchronized (SCROLL_LOCK) {
                    handleScroll(firstVisibleItem);
                }
            }
        });
    }

    /**
     * 处理滚动
     *
     * @param firstVisibleItem
     */
    private void handleScroll(int firstVisibleItem) {
        //取出与中线距离最近的item
        int tempD = -1;
        //本次滑动计算出的position
        int tempP = -1;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int location2[] = new int[2];
            child.getLocationOnScreen(location2);
            int childBottom = location2[1] + child.getMeasuredHeight();
            int childTop = location2[1];
            Log.d(TAG, "bottomY : " + bottomY + " topY : " + topY + " middleY : " + middleY +
                    " childBottom : " + childBottom + " childTop : " + childTop);
            //如果当前item的底部低于悬浮框的顶部或者当前item的顶部高于悬浮框的底部，不做操作（都是相对屏幕的绝对位置）
            if (childBottom < topY || childTop > bottomY) {
                continue;
            }
            //item高度中间线
            int childMiddleY = childBottom - child.getMeasuredHeight() / 2;
            //当前item真正的position
            int position = firstVisibleItem + i;
            //比较item高度中间线和悬浮框高度中间线
            int distance = Math.abs(middleY - childMiddleY);
            //找出item高度中间线和悬浮框高度中间线值最接近的一个item
            if (tempD == -1) {
                tempD = distance;
                tempP = position;
            } else if (tempD > distance) {
                tempD = distance;
                tempP = position;
            }
            //这种方式也可以实现，但是总感觉有问题
//            if (childTop >= topY && childBottom <= bottomY) {
//                selectPosition = position;
//            } else if (childTop < topY) {
//                if (childBottom < middleY) {
//                    selectPosition = position + 1;
//                } else {
//                    selectPosition = position;
//                }
//            } else {
//                if (childTop < middleY) {
//                    selectPosition = position - 1;
//                } else {
//                    selectPosition = position;
//                }
//            }
//            SLog.d("position: " + position);
//            break;
        }
        //数据修正
        if (tempP < 0) {
            tempP = 0;
        } else if (tempP > wheelAdapter.getCount() - 1) {
            tempP = wheelAdapter.getCount() - 1;
        }
        //防止多次notify同一个position
        if (selectPosition == tempP) {
            return;
        }
        //当前应该在悬浮框内的item位置
        selectPosition = tempP;
        callback.onHandleScroll(selectPosition);
    }
}
