package net.beeapm.ui.service;

import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.ResultVo;
import net.beeapm.ui.model.vo.TableVo;

import java.util.Map;

public interface IRequestService {
    TableVo list(Map<String,Object> params);
    ChartVo chart(Map<String,Object> params);

    /**
     * 调用链查询
     * @param params
     * @return
     */
    ResultVo callTree(Map<String,Object> params);
}
