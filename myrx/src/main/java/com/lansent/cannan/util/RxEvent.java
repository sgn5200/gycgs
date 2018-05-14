package com.lansent.cannan.util;


import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Cannan on 2017/7/28 0028.
 * rx 实现类似EventBus 的功能
 * 可代替广播等通讯
 */

public class RxEvent {
    private Subject<Object> bus;
    private HashMap<String,CompositeDisposable> subHashMap;
//    private Disposable dis;

    private RxEvent(){
        bus =  PublishSubject.create().toSerialized();
    }

    //内部类
    private static class RxBusInstance {
        private static final RxEvent rxBus = new RxEvent();
    }

    /**
     * 单例模式RxBus
     */
    public static RxEvent getInstance() {
        return RxBusInstance.rxBus;
    }

    /**
     * 发送消息
     *
     * @param t
     */
    public   void post(Object t) {
        bus.onNext(t);
    }


    /**
     * 返回指定类型的带背压的Flowable实例
     * @param <T>
     * @return    Flowable
     */
    public <T>Flowable<T> getObservable(Class<T> type){
        return bus.toFlowable(BackpressureStrategy.BUFFER).ofType(type);
    }

    /**
     * 一个默认的订阅方法
     * 主线程观察，工作线程执行
     * @param <T>
     * @param type
     * @param next
     * @param error
     * @return
     */
    public <T> void register(Class<T> type, Consumer<T> next, Consumer<Throwable> error){
        Disposable dis = getObservable(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next, error);
        addSubscription(type,dis);
    }

    /**
     * 判断是否有订阅者
     * @return
     */
    public boolean hasSubscribers() {
        return bus.hasObservers();
    }

    /**
	 * 取消订阅，防内存泄漏
     */
    public void unRegister(Object o){
        if(subHashMap == null ){
            return;
        }
        String key = o.getClass().getName();
        if (!subHashMap.containsKey(key)){
            return;
        }
        if (subHashMap.get(key) != null) {
            subHashMap.get(key).dispose();
        }
        subHashMap.remove(key);
    }



    /**
     * 保存订阅后的disposable
     * @param o
     * @param disposable
     */
    public void addSubscription(Object o, Disposable disposable) {
        if (subHashMap == null) {
            subHashMap = new HashMap<>();
        }
        String key = o.getClass().getName();
        if (subHashMap.get(key) != null) {
            subHashMap.get(key).add(disposable);
        } else {
            //一次性容器,可以持有多个并提供 添加和移除。
            CompositeDisposable disposables = new CompositeDisposable();
            disposables.add(disposable);
            subHashMap.put(key, disposables);
        }
    }





}
