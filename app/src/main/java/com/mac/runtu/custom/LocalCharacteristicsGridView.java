package com.mac.runtu.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * TODO: document your custom view class.
 */
public class LocalCharacteristicsGridView extends GridView {
    public LocalCharacteristicsGridView(Context context) {
        super(context);
    }

    public LocalCharacteristicsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LocalCharacteristicsGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LocalCharacteristicsGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
