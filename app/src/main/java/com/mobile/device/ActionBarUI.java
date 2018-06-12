package com.mobile.device;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

@SuppressWarnings("unused")
/**
 * 带有ActionBar的Activity基类
 */
public abstract class ActionBarUI extends AppCompatActivity {
    private static final int ACTIONBAR_DEFAULT_DP = 50;
    private static Integer actionBarHeight;
    private LinearLayout rootView;
    private FrameLayout actionBarView;
    private FrameLayout contentView;


    /*定义模板方法*/

    /**
     * 定义模板方法，根据流程创建Activity
     * @param savedInstanceState 保存的状态
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(actionBarHeight == null)
        {
            actionBarHeight =
                    (int) (getResources().getDisplayMetrics().density
                            * ACTIONBAR_DEFAULT_DP
                            + 0.5f);
        }

        ActionBarUICreator creator = new ActionBarUICreator();

        onBaseUICreate(creator);

        if(!creator.validate())
        {
            throw new RuntimeException("ActivityCreater缺少必要的属性！");
        }

        if(!creator.hasToolBar()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        LayoutInflater inflater = getLayoutInflater();

        rootView = new LinearLayout(this);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setBackgroundColor(Color.TRANSPARENT);

        Integer actionBarID = creator.getActionBarID();
        if(actionBarID != null)
        {
            actionBarView = new FrameLayout(this);
            actionBarView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    actionBarHeight
            ));
            actionBarView.addView(inflater.inflate(actionBarID,null));
            rootView.addView(actionBarView);
        }

        contentView = new FrameLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        params.weight = 1;
        contentView.setLayoutParams(params);
        contentView.addView(inflater.inflate(creator.getLayoutID(), null));
        contentView.setBackgroundColor(Color.TRANSPARENT);
        rootView.addView(contentView);

        setContentView(rootView);
        onViewInit(savedInstanceState);
    }



    /**
     * 通过BaseUIFragment创建者设置BaseUIFragment创建参数
     * @param creator BaseUI创建者
     */
    protected abstract void onBaseUICreate(ActionBarUICreator creator);

    /**
     * 进行BaseUI上视图的初始化
     * @param savedInstanceState 保存的内部数据
     */
    protected abstract void onViewInit(Bundle savedInstanceState);




    /*工具方法，提供视图的提取*/

    /**
     * 获得根视图
     * @return 根视图
     */
    public final ViewGroup getRootView() {
        return rootView;
    }

    /**
     * 获得ActionBar容器
     * @return actionBar容器
     */
    public final ViewGroup getActionBarView() {
        return actionBarView;
    }

    /**
     * 获得内容容器
     * @return 内容容器
     */
    public final ViewGroup getContentView() {
        return contentView;
    }
}