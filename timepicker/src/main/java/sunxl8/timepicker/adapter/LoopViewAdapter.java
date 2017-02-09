/*
 * Copyright (C) 2016 venshine.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sunxl8.timepicker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collection;
import java.util.List;

import sunxl8.timepicker.LoopViewConfig;
import sunxl8.timepicker.widget.LoopItem;


/**
 * Created by sunxl8 on 2016/12/6.
 */
public class LoopViewAdapter extends BaseAdapter {

    private List<String> mList = null;

    private boolean mLoop = false;

    private int mWheelSize = LoopViewConfig.LOOP_ITEM_SIZE;

    private int mCurrentPositon = -1;

    private Context mContext;

    public LoopViewAdapter(Context context) {
        mContext = context;
    }

    /**
     * 设置当前刻度
     *
     * @param position
     */
    public final void setCurrentPosition(int position) {
        mCurrentPositon = position;
    }

    @Override
    public final int getCount() {
        if (mLoop) {
            return Integer.MAX_VALUE;
        }
        return !isEmpty(mList) ? (mList.size() + mWheelSize - 1) : 0;
    }

    @Override
    public final long getItemId(int position) {
        return !isEmpty(mList) ? position % mList.size() : position;
    }

    @Override
    public final String getItem(int position) {
        return !isEmpty(mList) ? mList.get(position % mList.size()) : null;
    }

    @Override
    public boolean isEnabled(int position) {
        if (mLoop) {
            if (position % mList.size() == mCurrentPositon) {
                return true;
            }
        } else {
            if (position == (mCurrentPositon + mWheelSize / 2)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        if (mLoop) {
            position = position % mList.size();
        } else {
            if (position < mWheelSize / 2) {
                position = -1;
            } else if (position >= mWheelSize / 2 + mList.size()) {
                position = -1;
            } else {
                position = position - mWheelSize / 2;
            }
        }
        View view;
        if (position == -1) {
            view = bindView(0, convertView, parent);
        } else {
            view = bindView(position, convertView, parent);
        }
        if (!mLoop) {
            if (position == -1) {
                view.setVisibility(View.INVISIBLE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    public final LoopViewAdapter setClickable(boolean clickable) {
        super.notifyDataSetChanged();
        return this;
    }

    public final LoopViewAdapter setLoop(boolean loop) {
        if (loop != mLoop) {
            mLoop = loop;
            super.notifyDataSetChanged();
        }
        return this;
    }

    public final LoopViewAdapter setWheelSize(int wheelSize) {
        mWheelSize = wheelSize;
        super.notifyDataSetChanged();
        return this;
    }

    public final LoopViewAdapter setData(List<String> list) {
        mList = list;
        super.notifyDataSetChanged();
        return this;
    }

    private View bindView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = new LoopItem(mContext);
        }
        LoopItem loopItem = (LoopItem) convertView;
        String item = getItem(position);
        if (loopItem instanceof CharSequence) {
            loopItem.setText((CharSequence) item);
        } else {
            loopItem.setText(item.toString());
        }
        return convertView;
    };

    /**
     * 数据已改变，重绘可见区域
     */
    @Override
    @Deprecated
    public final void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * 数据失效，重绘控件
     */
    @Override
    @Deprecated
    public final void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }

    private <V> boolean isEmpty(Collection<V> c) {
        return (c == null || c.size() == 0);
    }
}
