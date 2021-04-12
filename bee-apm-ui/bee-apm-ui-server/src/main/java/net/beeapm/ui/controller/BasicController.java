package net.beeapm.ui.controller;

import net.beeapm.ui.common.BeeConst;
import net.beeapm.ui.model.param.login.LoginParam;
import net.beeapm.ui.model.result.ApiResult;
import net.beeapm.ui.model.result.login.LoginResult;
import net.beeapm.ui.model.result.login.UserInfoResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuanlong.chen
 * @date 2021/04/12
 */
@Controller
@RequestMapping("api/basic/")
public class BasicController {
    @RequestMapping("/login")
    @ResponseBody
    public ApiResult<LoginResult> login(@RequestBody LoginParam loginParam) {
        ApiResult<LoginResult> result = new ApiResult<>();
        LoginResult loginResult = new LoginResult();
        result.setCode(BeeConst.SUCCESS);
        loginResult.setRealName("BeeAdmin");
        loginResult.setUserId(1L);
        loginResult.setToken("fakeToken1");
        loginResult.setUsername("admin");
        List<Map<String, Object>> roles = new ArrayList<>();
        Map<String, Object> role = new HashMap<>(6);
        role.put("roleName", "Super Admin");
        role.put("value", "super");
        roles.add(role);
        loginResult.setRoles(roles);
        result.setResult(loginResult);
        return result;
    }

    @RequestMapping("/getUserInfoById")
    @ResponseBody
    public ApiResult<UserInfoResult> getUserInfoById(@RequestParam("userId") Long userId) {
        ApiResult<UserInfoResult> result = new ApiResult<>();
        UserInfoResult loginResult = new UserInfoResult();
        result.setCode(BeeConst.SUCCESS);
        loginResult.setRealName("BeeAdmin");
        loginResult.setUserId(userId);
        loginResult.setUsername("admin");
        List<Map<String, Object>> roles = new ArrayList<>();
        Map<String, Object> role = new HashMap<>(6);
        role.put("roleName", "Super Admin");
        role.put("value", "super");
        roles.add(role);
        loginResult.setRoles(roles);
        result.setResult(loginResult);
        return result;
    }
}
