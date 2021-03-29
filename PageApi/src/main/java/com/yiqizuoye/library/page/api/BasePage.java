package com.yiqizuoye.library.page.api;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 * 页面基类 包含两个泛型
 * DATA 需要的数据类型，当跳转到对应的page时，如果数据类型错误会在{@link PageRouter#open(Activity)} 返回 {@link PageCode#ERROR_DATA_FORMAT} 错误
 * P  page中持有的Presenter，开启页面时，自动创建，无需手动创建
 */
public abstract class BasePage<DATA, P extends BasePresenter> implements IView<DATA> {
    protected DATA mData;
    @NonNull
    public Activity mContext;
    @Nullable
    protected View mView;
    @NonNull
    protected P mPresenter;
    @NonNull
    public PageData mPageData;
    protected boolean isFinish;

    /**
     * 开启页面时，首先调用该方法 设置页面数据
     *
     * @param pageData  页面对象数据封装类 该参数保存在{@link #mPageData}
     * @param data      传递的数据，该参数已经保存在{@link #mData}
     * @param context   上下文  该参数保存在{@link #mContext}
     * @param presenter presenter
     * @param otherData 其他附加数据，比如在切换ype发时，通过{@link #onTypeChanged(Bundle)} 保存数据，同时该方法返回true，
     *                  再创建时会传递该参数。
     *                  其他需要特殊参数的场景。可以通过{@link PageRouter#setOtherData(Bundle)}传递
     */
    final void setPageData(@NonNull PageData pageData, DATA data, Activity context, P presenter, Bundle otherData) {
        this.mPageData = pageData;
        this.mData = data;
        this.mContext = context;
        this.mPresenter = presenter;
        if (presenter == null) {
            throw new RuntimeException("Presenter 创建失败：" + pageData.getPresenterClass());
        }
        mPresenter.setView(this);
        onCreate(data, otherData);
        mPresenter.onCreate(data, otherData);
    }

    /**
     * 再次打开时不创建新的Page,直接调用该方法
     *
     * @param data 新的数据
     */
    @CallSuper
    @Override
    public void onNewOpen(DATA data, @Nullable Bundle otherData) {
        this.mData = data;
        mPresenter.onNewOpen(data, otherData);
    }

    /**
     * 发送结果回调
     */
    public final void setResult(int result) {
        setResult(result, null);
    }

    /**
     * 发送结果回调
     */
    public final void setResult(int result, @Nullable Bundle data) {
        //分发回调事件
        PageManagerImpl.INSTANCE.setResult(this, result, data);
    }

    /**
     * 关闭当前界面
     */
    public final void finish() {
        if (isFinish) return;
        isFinish = true;
        onDestroy();
        mPresenter.onDestroy();
        if (mView != null) {
            //父view中移除该view
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        //从页面管理器移除
        PageManagerImpl.INSTANCE.removePage(this);
        //将finish事件通知到 页面队列管理器
        PageQueueManager.pageDataFinish(mPageData);
        mView = null;
        mData = null;
    }

    /**
     * 设置当前界面
     */
    protected final void setContentView(View view) {
        setContentView(view, null);
    }

    protected final void setContentView(@LayoutRes int layoutId) {
        setContentView(layoutId, null);
    }

    protected final void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        ViewGroup parentView = mContext.findViewById(mPageData.getParentId());
        if (parentView == null) {
            throw new IllegalArgumentException("PageParent配置的parentId有误，" + mContext.getResources().getResourceName(mPageData.getParentId()) + " 不在该" + mContext.getClass().getName() + "中");
        }
        int setIndex = mPageData.getIndex();
        view.setTag(R.id.page_router_index_key, setIndex);
        int realIndex = parentView.getChildCount();

        for (int i = parentView.getChildCount() - 1; i > -1; i--) {
            View child = parentView.getChildAt(i);
            Object tag = child.getTag(R.id.page_router_index_key);
            if (tag instanceof Integer) {
                int integer = (Integer) tag;
                if (integer == -1) {
                    integer = Integer.MAX_VALUE;
                }
                if (setIndex >= integer) {
                    break;
                }
            }
        }


        if (layoutParams == null) {
            parentView.addView(view, realIndex);
        } else {
            parentView.addView(view, realIndex, layoutParams);
        }

        mView = view;
    }

    protected final void setContentView(@LayoutRes int layoutId, ViewGroup.LayoutParams layoutParams) {
        View view = LayoutInflater.from(mContext).inflate(layoutId, null);
        if (view == null) {
            throw new IllegalArgumentException("layoutId ：" + mContext.getResources().getResourceName(layoutId) + "有误");
        }
        setContentView(view, layoutParams);
    }

    public final <T extends View> T findViewById(@IdRes int id) {
        if (mView == null) {
            throw new RuntimeException("未调用 setContentView 方法 设置布局");
        }
        return mView.findViewById(id);
    }

    public final <T extends View> T findViewWithTag(Object tag) {
        if (tag == null || mView == null) {
            return null;
        }
        return mView.findViewWithTag(tag);
    }

    /**
     * 是否拦截返回键
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 类型发送变化时，调用该方法
     *
     * @param otherData 保存的其他数据
     * @return 是否保存数据
     */
    public boolean onTypeChanged(@NonNull Bundle otherData) {
        return false;
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    /**
     * 获取页面对外提供的管道
     *
     * @return 子类重写该方法，返回对外暴露的接口实现
     * 必须通过{@link com.yiqizuoye.library.page.annotation.PagePipe} 注释，参数为获取该接口的方法名
     */
    public IPagePipe getPipe() {
        return null;
    }
}
