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
package sunxl8.timepicker.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import sunxl8.timepicker.style.LoopViewStyle;

/**
 * Created by sunxl8 on 2016/12/6.
 */
public class LoopViewDrawable extends Drawable {

    protected int mWidth;
    protected int mHeight;
    protected LoopViewStyle mStyle;

    private Paint mBgPaint;
    private Paint mSelectedBgPaint;

    public LoopViewDrawable(int width, int height, LoopViewStyle style) {
        mWidth = width;
        mHeight = height;
        mStyle = style;
        init();
    }

    private void init() {
        mBgPaint = new Paint();
        mBgPaint.setColor(mStyle.backgroundColor);

        mSelectedBgPaint = new Paint();
        mSelectedBgPaint.setColor(mStyle.selectedBackgroundColor);
    }

    @Override
    public void draw(Canvas canvas) {
        //选中栏与其他背景
        canvas.drawRect(0, 0, mWidth, mHeight * ((mStyle.itemSize + 1) / 2 - 1) / mStyle.itemSize, mBgPaint);
        canvas.drawRect(0, mHeight * ((mStyle.itemSize + 1) / 2 - 1) / mStyle.itemSize, mWidth,
                mHeight * (mStyle.itemSize + 1) / 2 / mStyle.itemSize, mSelectedBgPaint);
        canvas.drawRect(0, mHeight * (mStyle.itemSize + 1) / 2 / mStyle.itemSize, mWidth, mHeight, mBgPaint);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
