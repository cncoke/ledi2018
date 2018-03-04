package cn.yc.ledi.bean;

/**
 * Created by Administrator on 2018-02-22.
 */

public class TestTiku<T>  {
    private int status;
    private T result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "TestTiku{" +
                "status=" + status +
                ", result=" + result +
                '}';
    }
}
