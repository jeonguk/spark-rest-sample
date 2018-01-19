package com.jeonguk.error;

public class ResponseError {

    private String msg;

    public ResponseError() {
        super();
    }

    public ResponseError(String msg) {
        super();
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}