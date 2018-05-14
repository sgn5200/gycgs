package com.lansent.cannan.api;

/**
 * Description    : 为Gson 解析创造类型 父类,成员为泛型data，code，message
 * CreateAuthor: Cannan
 * Create time   : 2017/8/15 0015.
 */

public class BaseResponse<T> {
    private int code;
    private String message;

    private T data;
    private int successCode = 200;

    public T getData() {
        return data;
    }
    
    public boolean isSuccess(){
    	return code == successCode;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
    	return "[code:"+code+",message:"+message+"]";
    }
}
