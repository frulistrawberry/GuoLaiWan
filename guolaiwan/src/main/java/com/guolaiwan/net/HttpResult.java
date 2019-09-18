package com.guolaiwan.net;

/**
 * 作者: 陈冠希
 * 日期: 2018/3/20
 * 描述:
 */
public class HttpResult<T> {
    private int status;
    private String message;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess(){
        return status == 200;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
