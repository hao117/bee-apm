package net.beeapm.ui.service;

import net.beeapm.ui.model.vo.TableVo;

import java.util.Map;

/**
 * @author yuan
 * @date 2020-01-31
 */
public interface IAppInfoService {
    /**
     * 应用列表
     * @param params
     * @return
     */
    TableVo list(Map<String, Object> params);
}
