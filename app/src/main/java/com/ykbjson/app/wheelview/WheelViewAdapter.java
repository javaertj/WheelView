package com.ykbjson.app.wheelview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ykbjson.lib.wheelview.SimpleAdapterHolder;
import com.ykbjson.lib.wheelview.WheelAdapter;

import java.util.List;

public class WheelViewAdapter extends WheelAdapter<WheelItem> {
        private int mSelectPosition;

        /**
         * @param context 上下文
         * @param data    数据源
         */
        public WheelViewAdapter(Context context, List data) {
            super(context, data, R.layout.include_wheel_view_item);
        }

        @Override
        public void onHandleScroll(int selectPosition) {
            mSelectPosition = selectPosition;
            notifyDataSetChanged();
        }

        @Override
        public void covertView(SimpleAdapterHolder holder, int position, List<WheelItem> dataSource, WheelItem item) {
            float scale = 1f;
            if (mSelectPosition == position) {
                scale = 1.2f;
            }
            TextView tvName = holder.getView(R.id.tv_name);
            tvName.setText(item.getName());
            ImageView imageView = holder.getView(R.id.iv_avatar);
            ImageLoader.getInstance().displayImage(item.getAvatarUrl(), imageView);

            ViewHelper.setScaleX(holder.getmConvertView().findViewById(R.id.layout_content), scale);
            ViewHelper.setScaleY(holder.getmConvertView().findViewById(R.id.layout_content), scale);
        }
    }