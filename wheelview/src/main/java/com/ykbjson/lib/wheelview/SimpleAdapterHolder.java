package com.ykbjson.lib.wheelview;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 包名：com.ykbjson.lib.wheelview
 * 描述：简单万能adapter ViewHolder
 * 创建者：yankebin
 * 日期：2018/4/10
 */
public class SimpleAdapterHolder {

	private int mPosition;
	private View mConvertView;
	SparseArray<View> mMembers;

	public View getmConvertView() {
		return mConvertView;
	}

	public SimpleAdapterHolder() {

	}

	private SimpleAdapterHolder(ViewGroup parent, int layoutId, int position) {
		this.mPosition = position;
		this.mMembers = new SparseArray<>();
		mConvertView = LayoutInflater.from(parent.getContext()).inflate(
				layoutId, parent, false);

		mConvertView.setTag(this);
	}

	public static SimpleAdapterHolder get(View convertView, ViewGroup parent,
			int layoutId, int position) {
		if (convertView == null) {
			return new SimpleAdapterHolder(parent, layoutId, position);
		} else {
			return (SimpleAdapterHolder) convertView.getTag();
		}
	}


	public <T extends View> T getView(int viewId) {
		View view = mMembers.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mMembers.put(viewId, view);
		}
		return (T) view;
	}
}