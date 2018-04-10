package com.ykbjson.lib.wheelview;
/**
 * 包名：com.ykbjson.lib.wheelview
 * 描述：滚轮滚动回调
 * 创建者：yankebin
 * 日期：2018/4/10
 */
public interface OnScrollCallback {

        void onHandleScroll(int selectPosition);

        void onHandleIdle(WheelListView wheelListView, int selectPosition);
    }