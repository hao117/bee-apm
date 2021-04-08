package net.beeapm.ui.model.result;

import java.io.Serializable;

/**
 * 返回值对象
 * @author yuan
 */
public class BaseResult implements Serializable {
    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}