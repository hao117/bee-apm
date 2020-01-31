package net.beeapm.ui.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import io.searchbox.core.SearchResult;
import net.beeapm.ui.common.BeeConst;
import net.beeapm.ui.common.EsIndicesPrefix;
import net.beeapm.ui.es.EsJestClient;
import net.beeapm.ui.model.vo.TableVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * @author yuan
 * @date 2020-01-31
 */
@Service
public class AppInfoServiceImpl implements IAppInfoService {
    private Logger logger = LoggerFactory.getLogger(AppInfoServiceImpl.class);

    @Override
    public TableVo list(Map<String, Object> params) {
        TableVo res = new TableVo();
        res.setCode("0");
        res.setPageNum(Integer.parseInt(params.get("pageNum").toString()));
        params.put("from", (res.getPageNum() - 1) * BeeConst.PAGE_SIZE);
        params.put("size", BeeConst.PAGE_SIZE);
        params.put("sort", "time");
        List<Object> rows = new ArrayList<>();
        try {
            SearchResult searchResult = EsJestClient.inst().search(params, "PageDataList", EsIndicesPrefix.HEARTBEAT);
            if (BeeConst.CODE_404 == searchResult.getResponseCode()) {
                res.setCode("-1");
                res.setMsg(searchResult.getErrorMessage());
                return res;
            }
            Long total = searchResult.getTotal();
            res.setPageTotal(total);
            JSONObject jsonObject = JSON.parseObject(searchResult.getJsonString());
            JSONArray hits = (JSONArray) JSONPath.eval(jsonObject, "$.hits.hits");
            for (int i = 0; i < hits.size(); i++) {
                JSONObject hit = hits.getJSONObject(i);
                JSONObject row = hit.getJSONObject("_source");
                rows.add(row);
            }
            res.setRows(rows);
        } catch (Exception e) {
            logger.error("", e);
        }
        return res;
    }
}
