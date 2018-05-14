package com.lansent.cannan.data;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Cannan on 2017/7/26 0026.
 * 持久化保存配置信息
 */

public class ShareUtils {
    private ShareUtils(){}

    private static SharedPreferences sp;

    //配置xml文件名
    public static ShareUtils getSp(String fileName,Context context){
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return new ShareUtils();
    }

    /**
     * 保存boolean类型的配置信息
     * @param key
     * @param v
     */
    public  void saveBoolConfig(String key,boolean v){
        sp.edit().putBoolean(key,v).apply();
    }

    /**
     * 获取boolean类型的配置信息，默认值false
     * @param key
     * @return
     */
    public boolean getBoolConfig(String key){
        return sp.getBoolean(key,false);
    }

    /**
     * 保存int类型的配置信息
     * @param key
     * @param v
     */
    public  void saveIntConfig(String key,int v){
        sp.edit().putInt(key,v).apply();
    }

    /**
     * 获取int类型的配置信息
     * @param key
     * @return  默认值为0
     */
    public int getIntConfig(String key){
        return sp.getInt(key,0);
    }


    /**
     * 保存String类型的配置信息
     * @param key
     * @param value
     */
    public void saveStrConfig(String key, String value) {
        sp.edit().putString(key, value) .apply();
    }

    /**
     * 获取自定义的String类型的配置信息，自定义默认返回值
     * @param key
     * @return
     */
    public String getStrConfig(String key) {
        return sp.getString(key, "");
    }


    public  void remove(String key){
        sp.edit().remove(key).apply();
    }

    public  void remove(){
        sp.edit().clear().apply();
    }

    public  boolean conhtains(String key){
       return sp.contains(key);
    }


    /**
     * 配置信息保存的key
     * interface默认public ,final 修饰
     */
    public interface Config{
        String fileName = "share_file";
        /**
         * 用户账号
         */
        String KEY_USER_ACCOUNT = "KEY_USER_ACCOUNT";
        String LoginUsername ="LoginUsername";
        String LoginPassword ="LoginPassword";
        /**
         * 第一次启动
         */
        String lunch="lunch";

        String etPropertyName="etPropertyName";//物业名称
        String tvRegionName="tvRegionName";//小区名称
        String tvRegionCode="tvRegionCode";//小区编码

        String AVATAR_PATH="avatar_path";//拍照后上传路径

        String RID="residentid";    //已经有人居住后放回
        String LID="LiviID";          //已经有人居住后放回
        String AutoLogin="autoLogin" ;//自动登录
        String COOKIE="cookie" ;//自动登录
    }

}
