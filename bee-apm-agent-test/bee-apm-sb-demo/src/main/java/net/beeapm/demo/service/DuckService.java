package net.beeapm.demo.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.beeapm.demo.common.OkHttpUtils;
import net.beeapm.demo.entity.User;
import net.beeapm.demo.mapper.UserMapper;
import net.beeapm.demo.model.RequestVo;
import net.beeapm.demo.model.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yuan
 * @date 2020/01/12
 */
@Service
public class DuckService implements IDuckService {
    private static final Logger log = LoggerFactory.getLogger(DogService.class);
    @Autowired
    private UserMapper userMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo sayGaGa(RequestVo vo) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Page<User> page = new Page<>(1,3);
        Page<User> userList = userMapper.selectPage(page,wrapper);
        log.debug("query-page={}", JSON.toJSONString(userList));
        User user = userMapper.selectById(4);
        log.debug("query-one={}", JSON.toJSONString(user));
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo twoFoot(RequestVo vo) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Page<User> page = new Page<>(1,6);
        Page<User> userList = userMapper.selectPage(page,wrapper);
        log.debug("query-page={}", JSON.toJSONString(userList));
        User user = userMapper.selectById(3);
        log.debug("query-one={}", JSON.toJSONString(user));
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo twoWing(RequestVo vo) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Page<User> page = new Page<>(1,7);
        Page<User> userList = userMapper.selectPage(page,wrapper);
        log.debug("query-page={}", JSON.toJSONString(userList));
        User user = userMapper.selectById(2);
        log.debug("query-one={}", JSON.toJSONString(user));
        user = userMapper.selectById(5);
        log.debug("query-one={}", JSON.toJSONString(user));
        user = userMapper.selectById(7);
        log.debug("query-one={}", JSON.toJSONString(user));
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    public ResultVo whiteDuck(RequestVo vo) {
        return OkHttpUtils.instance().post(vo);
    }
}
