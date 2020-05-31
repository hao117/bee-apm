package net.beeapm.demo.model;

/**
 * @author yuan
 * @date 2020/01/12
 */
public class ResultVo {
    private int code;
    private int counter;
    private String msg;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResultVo OK(int counter, String msg){
        ResultVo res = new ResultVo();
        res.setMsg(msg);
        res.setCounter(counter);
        return res;
    }
}
