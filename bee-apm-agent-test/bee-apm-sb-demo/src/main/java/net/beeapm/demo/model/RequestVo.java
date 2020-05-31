package net.beeapm.demo.model;

/**
 * @author yuan
 * @date 2020/01/12
 */
public class RequestVo {
    private int counter;
    private String msg;
    private Object data;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
