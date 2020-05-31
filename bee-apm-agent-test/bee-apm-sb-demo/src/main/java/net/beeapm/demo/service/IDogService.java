package net.beeapm.demo.service;

import net.beeapm.demo.model.RequestVo;
import net.beeapm.demo.model.ResultVo;

/**
 * @author yuan
 * @date 2020/01/12
 */
public interface IDogService {
    /**
     * sayWangWang
     * @param vo
     * @return
     */
    ResultVo sayWangWang(RequestVo vo);

    /**
     * sayGoodbye
     * @param vo
     * @return
     */
    ResultVo sayGoodbyeDog(RequestVo vo);

    /**
     * sayHello
     * @param vo
     * @return
     */
    ResultVo sayHelloDog(RequestVo vo);

    /**
     * blackDog
     * @param vo
     * @return
     */
    ResultVo blackDog(RequestVo vo);
}
