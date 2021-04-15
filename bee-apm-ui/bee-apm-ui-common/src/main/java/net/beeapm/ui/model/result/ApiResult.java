package net.beeapm.ui.model.result;

import net.beeapm.ui.common.BeeConst;

import java.io.Serializable;

/**
 * 返回值对象
 *
 * @author yuan
 */
public class ApiResult<T> implements Serializable {
    /**
     * 结果编码,0成功,其它失败
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;
    /**
     * 返回值对象
     */
    private T result;
    /**
     * 结果类型
     * success,error,warning
     */
    private String type = "success";

    public static final Integer CODE_SUCCESS = 0;
    public static final Integer CODE_FAILED = -1;

    public Integer getCode() {
        return code;
    }

    public static <T> ApiResult<T> success(T result) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setCode(CODE_SUCCESS);
        apiResult.setResult(result);
        return apiResult;
    }

    public static <T> ApiResult<T> fail(String message) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setCode(CODE_FAILED);
        apiResult.setMessage(message);
        return apiResult;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
