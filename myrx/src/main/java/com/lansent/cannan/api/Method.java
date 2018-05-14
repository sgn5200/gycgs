package com.lansent.cannan.api;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description    : 构建请求时需传入请求的方法，必须为注解指定的该类型
 *                        http请求方法参数
 * CreateAuthor: Cannan
 * Create time   : 2017/9/30 0030     上午 10:41
 */

public interface Method {

 /**
  * get 请求方式
  */
 int GET = 0;

 /**
  * post 请求方式
  */
    int POST = 1;
 /**
  * put 请求方式
  */
    int PUT = 2;

 /**
  * delete 请求方式
  */
    int DELETE = 4;

 /**
  * 通过注解限制请求方式，目前只添加4种
  */
    @IntDef({GET,POST,PUT,DELETE})
    @Retention(RetentionPolicy.SOURCE)
    @interface METHOD{}
}
