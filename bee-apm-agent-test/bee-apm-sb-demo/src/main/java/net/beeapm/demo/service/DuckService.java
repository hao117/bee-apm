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
public class DuckService implements IDuckService {
    @Override
    public ResultVo sayGaGa(RequestVo vo) {
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    public ResultVo twoFoot(RequestVo vo) {
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    public ResultVo twoWing(RequestVo vo) {
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    public ResultVo whiteDuck(RequestVo vo) {
        return OkHttpUtils.instance().post(vo);
    }
}
