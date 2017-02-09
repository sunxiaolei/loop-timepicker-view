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
package sunxl8.timepicker.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

import sunxl8.timepicker.LoopViewConfig;
import sunxl8.timepicker.adapter.LoopViewAdapter;
import sunxl8.timepicker.graphics.LoopViewDrawable;
import sunxl8.timepicker.style.LoopViewStyle;

/**
 * Created by sunxl8 on 2016/12/6.
 */
public class LoopView extends ListView implements ILoopView {

    public static final String TAG = "loopview";
    public static final int WHEEL_SCROLL_HANDLER_WHAT = 100;
    public static final int WHEEL_SCROLL_DELAY_DURATION = 200;
    private static final boolean canLoop = false;

    private int mItemH = 0; // 每一项高度
    private List<String> mList = null;   // 滚轮数据列表
    private int mCurrentPositon = -1;    // 记录滚轮当前位置
    private int mSelection = 0; // 选中位置

    private LoopViewStyle mStyle = new LoopViewStyle();  // 滚轮样式

    private LoopViewAdapter mWheelAdapter;

    private OnWheelItemSelectedListener mOnWheelItemSelectedListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHEEL_SCROLL_HANDLER_WHAT) {
                if (mOnWheelItemSelectedListener != null) {
                    mOnWheelItemSelectedListener.onItemSelected
                            (getCurrentPosition(), getSelectionItem());
                }
            }
        }
    };

    private OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                View itemView = getChildAt(0);
                if (itemView != null) {
                    float deltaY = itemView.getY();
                    if (deltaY == 0 || mItemH == 0) {
                        return;
                    }
                    if (Math.abs(deltaY) < mItemH / 2) {
                        int d = getSmoothDistance(deltaY);
                        smoothScrollBy(d, 0);
                    } else {
                        int d = getSmoothDistance(mItemH + deltaY);
                        smoothScrollBy(d, 0);
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int
                visibleItemCount, int totalItemCount) {
            if (visibleItemCount != 0) {
                refreshCurrentPosition(false);
            }
        }
    };

    public LoopView(Context context) {
        super(context);
    }

    public LoopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置滚轮滑动停止时事件，监听滚轮选中项
     *
     * @param onWheelItemSelectedListener
     */
    public void setOnWheelItemSelectedListener(OnWheelItemSelectedListener
                                                       onWheelItemSelectedListener) {
        mOnWheelItemSelectedListener = onWheelItemSelectedListener;
    }

    public void init() {
        init(new LoopViewStyle());
    }

    /**
     * 初始化
     */
    public void init(LoopViewStyle style) {
        this.mStyle = style;
        setTag(TAG);
        setVerticalScrollBarEnabled(false);
        setScrollingCacheEnabled(false);
        setCacheColorHint(Color.TRANSPARENT);
        setFadingEdgeLength(0);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setDividerHeight(0);
        setOnScrollListener(mOnScrollListener);
        setOnTouchListener(mTouchListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNestedScrollingEnabled(true);
        }
        addOnGlobalLayoutListener();
    }


    private void addOnGlobalLayoutListener() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                if (getChildCount() > 0 && mItemH == 0) {
                    mItemH = getChildAt(0).getHeight();
                    if (mItemH != 0) {
                        ViewGroup.LayoutParams params = getLayoutParams();
                        params.height = mItemH * mStyle.itemSize;
                        refreshVisibleItems(getFirstVisiblePosition(),
                                getCurrentPosition() + mStyle.itemSize / 2,
                                mStyle.itemSize / 2);
                        setBackground();
                    } else {

                    }
                }
            }
        });
    }

    /**
     * 设置背景
     */
    private void setBackground() {
        Drawable drawable = new LoopViewDrawable(getWidth(), mItemH * mStyle.itemSize, mStyle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    /**
     * 获取滚轮位置
     *
     * @return
     */
    public int getSelection() {
        return mSelection;
    }

    /**
     * 设置滚轮位置
     *
     * @param selection
     */
    public void setSelection(final int selection) {
        mSelection = selection;
        setVisibility(View.INVISIBLE);
        LoopView.this.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoopView.super.setSelection(getRealPosition(selection));
                refreshCurrentPosition(false);
                setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    /**
     * 获得滚轮当前真实位置
     *
     * @param positon
     * @return
     */
    private int getRealPosition(int positon) {
        if (isEmpty(mList)) {
            return 0;
        }
        if (canLoop) {
            int d = Integer.MAX_VALUE / 2 / mList.size();
            return positon + d * mList.size() - mStyle.itemSize / 2;
        }
        return positon;
    }

    /**
     * 获取当前滚轮位置
     *
     * @return
     */
    public int getCurrentPosition() {
        return mCurrentPositon;
    }

    /**
     * 获取当前滚轮位置的数据
     *
     * @return
     */
    public String getSelectionItem() {
        int position = getCurrentPosition();
        position = position < 0 ? 0 : position;
        if (mList != null && mList.size() > position) {
            return mList.get(position);
        } else if (mList != null) {
            return mList.get(mList.size() - 1);
        }
        return "";
    }

    /**
     * 设置滚轮数据源适配器
     *
     * @param adapter
     */
    public void setLoopAdapter(LoopViewAdapter adapter) {
        super.setAdapter(adapter);
        mWheelAdapter = adapter;
        mWheelAdapter.setData(mList).setWheelSize(mStyle.itemSize).setLoop(canLoop);
    }

    /**
     * 设置滚轮数据
     *
     * @param list
     */
    public void setDateList(List<String> list) {
        if (isEmpty(list)) {

        }
        mList = list;
        if (mWheelAdapter != null) {
            mWheelAdapter.setData(list);
        }
    }

    /**
     * 获得滚轮数据总数
     *
     * @return
     */
    public int getLoopCount() {
        return !isEmpty(mList) ? mList.size() : 0;
    }

//    public void setStyle(LoopViewStyle style){
//        this.mStyle = style;
//    }

    /**
     * 平滑的滚动距离
     *
     * @param scrollDistance
     * @return
     */
    private int getSmoothDistance(float scrollDistance) {
        if (Math.abs(scrollDistance) <= 2) {
            return (int) scrollDistance;
        } else if (Math.abs(scrollDistance) < 12) {
            return scrollDistance > 0 ? 2 : -2;
        } else {
            return (int) (scrollDistance / 6);
        }
    }

    /**
     * 刷新当前位置
     *
     * @param join
     */
    private void refreshCurrentPosition(boolean join) {
        if (getChildAt(0) == null || mItemH == 0) {
            return;
        }
        int firstPosition = getFirstVisiblePosition();
        if (canLoop && firstPosition == 0) {
            return;
        }
        int position = 0;
        if (Math.abs(getChildAt(0).getY()) <= mItemH / 2) {
            position = firstPosition;
        } else {
            position = firstPosition + 1;
        }
        refreshVisibleItems(firstPosition, position + mStyle.itemSize / 2,
                mStyle.itemSize / 2);
        if (canLoop) {
            position = (position + mStyle.itemSize / 2) % getLoopCount();
        }
        if (position == mCurrentPositon && !join) {
            return;
        }
        mCurrentPositon = position;
        mWheelAdapter.setCurrentPosition(position);
        mHandler.removeMessages(WHEEL_SCROLL_HANDLER_WHAT);
        mHandler.sendEmptyMessageDelayed(WHEEL_SCROLL_HANDLER_WHAT, WHEEL_SCROLL_DELAY_DURATION);
    }

    /**
     * 刷新可见滚动列表
     *
     * @param firstPosition
     * @param curPosition
     * @param offset
     */
    private void refreshVisibleItems(int firstPosition, int curPosition, int
            offset) {
        for (int i = curPosition - offset; i <= curPosition + offset; i++) {
            View itemView = getChildAt(i - firstPosition);
            if (itemView == null) {
                continue;
            }
            TextView textView = (TextView) itemView.findViewWithTag(LoopViewConfig.LOOP_ITEM_TEXT_TAG);
            refreshTextView(i, curPosition, itemView, textView);
        }
    }

    /**
     * 刷新文本
     *
     * @param position
     * @param curPosition
     * @param itemView
     * @param textView
     */
    private void refreshTextView(int position, int curPosition, View
            itemView, TextView textView) {
        if (curPosition == position) { // 选中
            int textColor = mStyle.selectedTextColor;
            float defTextSize = mStyle.textSize;
            float textSize = mStyle.selectedTextSize;
            setTextView(itemView, textView, textColor, textSize, 1.0f);
        } else {    // 未选中
            int textColor = mStyle.textColor;
            float textSize = mStyle.textSize;
            int delta = Math.abs(position - curPosition);
            float alpha = (float) Math.pow(mStyle.textAlpha, delta);
            setTextView(itemView, textView, textColor, textSize, alpha);
        }
    }

    /**
     * 设置TextView
     *
     * @param itemView
     * @param textView
     * @param textColor
     * @param textSize
     * @param textAlpha
     */
    private void setTextView(View itemView, TextView textView, int textColor, float textSize, float textAlpha) {
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        itemView.setAlpha(textAlpha);
    }

    private <V> boolean isEmpty(Collection<V> c) {
        return (c == null || c.size() == 0);
    }

    public interface OnWheelItemSelectedListener {
        void onItemSelected(int position, String data);
    }

}
