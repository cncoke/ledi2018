package cn.yc.ledi.bean;

/**
 * Created by Administrator on 2018-02-22.
 */

public class TestTikuSub {
    private String tikutype;
    private String num;

    public String getTikutype() {
        return tikutype;
    }

    public void setTikutype(String tikutype) {
        this.tikutype = tikutype;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "TestTiku{" +
                "tikutype='" + tikutype + '\'' +
                ", num=" + num +
                '}';
    }
}
