package com.lansent.cannan.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lansent.cannan.util.Log;


/**
 * Description    :抽象Fragment，子类实现initView，getLayout三个方法。
 * 需要注意initView在父类的onCreate生命周期中调用。
 * 全局持有TAG，无需重定义。
 * 该类打印了所有生命周期函数
 * 该类添加了一系列公共方法：页面跳转，键盘显示隐藏，view绑定，toast定义
 * CreateAuthor: Cannan
 * Create time   : 2017/9/26 0026.  下午 4:53
 */
public abstract class AbsBaseFragment extends Fragment {

    public AbsBaseActivity baseActivity;
    private View root;
     public String TAG=getClass().getSimpleName();

    public boolean init;
    /**
     * @return Layout布局id，初始化
     */
    public abstract
    @LayoutRes
    int getLayout();

    public abstract void initViews();

    /*******************lifecycle start************************/
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        Log.i(TAG);
        baseActivity = (AbsBaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.i(TAG);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(getLayout(), null, false);
        initViews();
        Log.i(TAG);

        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG);

    }
    /*******************lifecycle end************************/



    /**
     * 绑定ID获取视图
     *
     * @param id
     * @param <T>泛型 传入什么返回什么
     * @return T
     */
    public <T extends View> T getView(@IdRes int id) {
        T view = (T) root.findViewById(id);
        return view;
    }

    /**
     * 给指定ID的视图添加点击事件
     *
     * @param listener
     * @param ids
     */
    public void initListener(View.OnClickListener listener, int... ids) {
        for (int id : ids) {
            getView(id).setOnClickListener(listener);
        }
    }

    /**
     * 给指定ID的视图添加点击事件
     * @param listener
     * @param views
     */
    public void initListener(View.OnClickListener listener, View... views) {
        baseActivity.initListener(listener,views);
    }

    public void closeInputKeyboard(View v) {
        baseActivity.closeInputKeyboard(v);
    }

    public void openInputKeyboard(View v){
        baseActivity.openInputKeyboard(v);
    }

    public void lunchActivity(Class<?> className, @Nullable Bundle bundle, boolean isFinish) {
        baseActivity.lunchActivity(className,bundle,isFinish);
    }
    public void lunchActivityResult(Class<?> className, int code, @Nullable Bundle bundle){
        baseActivity.lunchActivityForResult(className,code,bundle);
    }

    protected void showToast(String msg) {
        baseActivity.showToast(msg);
    }

    protected void showToast(int id) {
        baseActivity.showToast(id);
    }

}
