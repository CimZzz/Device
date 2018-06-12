package com.mobile.device;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.HashMap;

/**
 * Created by CimZzz on 2018/4/3.<br>
 * Description:<br>
 * 界面切换控件
 */
public class RefreshHandlerView extends FrameLayout {
    private View errorView;
    private View loadView;
    private View contentView;
    private HashMap<String,View> viewMap;

    public RefreshHandlerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        viewMap = new HashMap<>();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        String tag = (String) child.getTag();

        if(TextUtils.isEmpty(tag))
            return;

        switch (tag) {
            case "Error":
                errorView = child;
                break;
            case "Load":
                loadView = child;
                break;
            case "Content":
                contentView = child;
                break;
            default:
                viewMap.put(tag,child);
                break;
        }
        super.addView(child, index, params);
    }

    public void showContent() {
        removeAllViews();
        addView(contentView);
    }
    public void showRefresh() {
        removeAllViews();
        addView(loadView);
    }
    public void showError() {
        removeAllViews();
        addView(errorView);
    }

    public View showView(String tag) {
        View child = viewMap.get(tag);

        if(child != null) {
            removeAllViews();
            addView(child);
        }
        return child;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        errorView.setOnClickListener(l);
    }
}
