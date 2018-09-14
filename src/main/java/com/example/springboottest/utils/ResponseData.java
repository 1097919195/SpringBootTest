/** */
package com.example.springboottest.utils;


import java.io.Serializable;


/**
 * @功能: request请求返回的数据
 * @项目名:kyloanServer
 * @作者:wangjz
 * @日期:2016年3月21日下午2:26:34
 */
public class ResponseData<T> implements Serializable {
    /**  */
    private static final long serialVersionUID = -6146242610931501695L;

    /**
     * 数据
     */
    private T data;
    /**
     * 返回的信息状态
     */
    private String message;
    /**
     * 请求状态：0表示成功，其他数字表示失败
     */
    private int state;
    /**
     * 请求状态：0表示成功，其他数字表示失败
     */
    private int tokenState;


    public int getTokenState() {
        return tokenState;
    }


    public void setTokenState(int tokenState) {
        this.tokenState = tokenState;
    }


    public int getState() {
        return state;
    }


    public void setState(int state) {
        this.state = state;
    }


    public ResponseData() {
        super();


    }

    /**
     * 构造函数
     *
     * @param data
     * @param message
     */
    public ResponseData(T data) {
        super();
        this.data = data;

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


}
