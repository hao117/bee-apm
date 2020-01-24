package net.beeapm.ui.service;

import net.beeapm.ui.model.vo.ResultVo;

import java.util.Map;

public interface ICommonService {
    ResultVo queryGroupList(Map<String,Object> param);

    ResultVo queryById(Map<String,Object> param);
}
