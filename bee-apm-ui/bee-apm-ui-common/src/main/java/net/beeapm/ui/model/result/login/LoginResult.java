package net.beeapm.ui.model.result.login;

import java.util.List;
import java.util.Map;

/**
 * @author yuanlong.chen
 * @date 2021/04/12
 */
public class LoginResult{
    private String realName;
    private Long userId;
    private String username;
    private String token;
    private String desc;
    private List<Map<String,Object>> roles;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Map<String, Object>> getRoles() {
        return roles;
    }

    public void setRoles(List<Map<String, Object>> roles) {
        this.roles = roles;
    }
}
