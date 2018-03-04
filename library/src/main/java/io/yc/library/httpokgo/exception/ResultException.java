package io.yc.library.httpokgo.exception;

/**
 * Created by Administrator on 2018-02-26.
 */

public class ResultException extends RuntimeException {


    private int code;                               //HTTP status code
    private String message;


    public ResultException(int code, String msg) {

        this.code = code;
        this.message = msg;

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
