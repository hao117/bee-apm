package net.beeapm.ui.service;

import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.TableVo;

import java.util.Map;

public interface ILoggerService {
    TableVo list(Map<String, Object> params);
    ChartVo chart(Map<String, Object> params);
}
