package net.beeapm.ui.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import io.searchbox.core.SearchResult;
import net.beeapm.ui.common.BeeConst;
import net.beeapm.ui.common.EsIndicesPrefix;
import net.beeapm.ui.es.EsJestClient;
import net.beeapm.ui.model.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl implements ICommonService {
    private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Override
    public ResultVo queryGroupList(Map<String, Object> params) {
        ResultVo res = new ResultVo();
        try {
            SearchResult searchResult = EsJestClient.inst().search(params, "GroupList", EsIndicesPrefix.HEARTBEAT);
            if (BeeConst.CODE_404 == searchResult.getResponseCode()) {
                res.setCode("-1");
                res.setResult(new ArrayList<>());
                return res;
            }
            JSONObject jsonObject = JSON.parseObject(searchResult.getJsonString());
            JSONArray buckets = (JSONArray) JSONPath.eval(jsonObject, "$.aggregations.group_list.buckets");
            if (buckets == null || buckets.size() == 0) {
                res.setCode("0");
                res.setResult(new ArrayList<>());
                return res;
            }
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < buckets.size(); i++) {
                JSONObject bucket = buckets.getJSONObject(i);
                Map<String, String> item = new HashMap<>();
                item.put("name", bucket.getString("key"));
                item.put("value", bucket.getString("key"));
                list.add(item);
            }
            Map<String, String> item = new HashMap<>();
            item.put("name", "全部");
            item.put("value", "");
            list.add(0, item);
            res.setCode("0");
            res.setResult(list);
        } catch (Exception e) {
            logger.error("", e);
        }
        return res;
    }

    @Override
    public ResultVo queryById(Map<String, Object> param) {
        ResultVo res = new ResultVo();
        try {
            String indexName = (String) param.remove("index");
            SearchResult searchResult = EsJestClient.inst().search(param, "QueryById", indexName + "-");
            if (BeeConst.CODE_404 == searchResult.getResponseCode()) {
                res.setCode("-1");
                res.setResult(new ArrayList<>());
                return res;
            }
            JSONObject jsonObject = JSON.parseObject(searchResult.getJsonString());
            res.setCode("0");
            res.setResult(JSONPath.eval(jsonObject, "$.hits.hits[0]._source"));
            if (logger.isDebugEnabled()) {
                logger.debug("query-result:", JSON.toJSONString(res));
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return res;
    }
}
