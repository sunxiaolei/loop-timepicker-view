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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import sunxl8.timepicker.LoopViewConfig;

/**
 * Created by sunxl8 on 2016/12/6.
 */
public class LoopItem extends FrameLayout {

    private TextView mText;

    public LoopItem(Context context) {
        super(context);
        init();
    }

    public LoopItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoopItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        LinearLayout layout = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(getContext(),
                LoopViewConfig.LOOP_ITEM_HEIGHT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(LoopViewConfig.LOOP_ITEM_PADDING, LoopViewConfig.LOOP_ITEM_PADDING, LoopViewConfig.LOOP_ITEM_PADDING, LoopViewConfig.LOOP_ITEM_PADDING);
        layout.setGravity(Gravity.CENTER);
        addView(layout, layoutParams);

        mText = new TextView(getContext());
        mText.setTag(LoopViewConfig.LOOP_ITEM_TEXT_TAG);
        mText.setEllipsize(TextUtils.TruncateAt.END);
        mText.setSingleLine();
        mText.setIncludeFontPadding(false);
        mText.setGravity(Gravity.CENTER);
        mText.setTextColor(Color.BLACK);
        LayoutParams textParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.addView(mText, textParams);
    }

    /**
     * 设置文本
     *
     * @param text
     */
    public void setText(CharSequence text) {
        mText.setText(text);
    }

    /**
     * 设置图片资源
     *
     * @param resId
     */
//    public void setImage(int resId) {
//        mImage.setVisibility(View.VISIBLE);
//        mImage.setImageResource(resId);
//    }

    public int dip2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public int sp2px(Context context, float sp) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }


}
