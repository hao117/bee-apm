package net.beeapm.demo.service;

import net.beeapm.demo.model.RequestVo;
import net.beeapm.demo.model.ResultVo;

/**
 * @author yuanlong.chen
 * @date 2020/01/12
 */
public interface IDuckService {
    /**
     * sayGaGa
     * @param vo
     * @return
     */
    ResultVo sayGaGa(RequestVo vo);

    /**
     * twoFoot
     * @param vo
     * @return
     */
    ResultVo twoFoot(RequestVo vo);

    /**
     * twoWing
     * @param vo
     * @return
     */
    ResultVo twoWing(RequestVo vo);

    /**
     * whiteDuck
     * @param vo
     * @return
     */
    ResultVo whiteDuck(RequestVo vo);
}
