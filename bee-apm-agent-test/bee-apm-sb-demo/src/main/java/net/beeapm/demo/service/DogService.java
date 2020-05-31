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

import java.util.List;

/**
 * @author yuan
 * @date 2020/01/12
 */
@Service
public class DogService implements IDogService {
    private static final Logger log = LoggerFactory.getLogger(DogService.class);
    @Autowired
    private UserMapper userMapper;
    @Override
    public ResultVo sayWangWang(RequestVo vo) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("name","a");
        List<User> list = userMapper.selectList(wrapper);
        log.debug("query-list={}", JSON.toJSONString(list));
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    public ResultVo sayGoodbyeDog(RequestVo vo) {
        User user = userMapper.selectById(2);
        log.debug("query-one={}", JSON.toJSONString(user));
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    public ResultVo sayHelloDog(RequestVo vo) {
        User user = userMapper.selectById(5);
        log.debug("query-one={}", JSON.toJSONString(user));
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo blackDog(RequestVo vo) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        Page<User> page = new Page<>(1,5);
        Page<User> userList = userMapper.selectPage(page,wrapper);
        log.debug("query-page={}", JSON.toJSONString(userList));
        User user = userMapper.selectById(5);
        log.debug("query-one={}", JSON.toJSONString(user));
        return OkHttpUtils.instance().post(vo);
    }
}
