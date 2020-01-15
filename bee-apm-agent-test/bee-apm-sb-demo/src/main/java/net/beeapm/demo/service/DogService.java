package net.beeapm.demo.service;

import net.beeapm.demo.common.OkHttpUtils;
import net.beeapm.demo.model.RequestVo;
import net.beeapm.demo.model.ResultVo;
import org.springframework.stereotype.Service;

/**
 * @author yuanlong.chen
 * @date 2020/01/12
 */
@Service
public class DogService implements IDogService {
    @Override
    public ResultVo sayWangWang(RequestVo vo) {
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    public ResultVo sayGoodbyeDog(RequestVo vo) {
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    public ResultVo sayHelloDog(RequestVo vo) {
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    public ResultVo blackDog(RequestVo vo) {
        return OkHttpUtils.instance().post(vo);
    }
}
