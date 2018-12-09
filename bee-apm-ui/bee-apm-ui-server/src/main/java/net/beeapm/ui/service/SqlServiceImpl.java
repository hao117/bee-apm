package net.beeapm.ui.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import io.searchbox.core.SearchResult;
import net.beeapm.ui.common.BeeConst;
import net.beeapm.ui.common.BeeUtils;
import net.beeapm.ui.common.EsIndicesPrefix;
import net.beeapm.ui.es.EsJestClient;
import net.beeapm.ui.es.EsQueryStringMap;
import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.TableVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SqlServiceImpl implements ISqlService {
    private Logger logger = LoggerFactory.getLogger(SqlServiceImpl.class);
    @Override
    public TableVo list(Map<String, Object> params) {
        TableVo res = new TableVo();
        res.setCode("0");
        res.setPageNum(Integer.parseInt(params.get("pageNum").toString()));
        params.put("from",(res.getPageNum() - 1) * BeeConst.PAGE_SIZE);
        params.put("size",BeeConst.PAGE_SIZE);
        List<Object> rows = new ArrayList<>();
        try {
            SearchResult searchResult = EsJestClient.inst().search(params, "PageDataList", EsIndicesPrefix.SQL);
            if(404 == searchResult.getResponseCode()){
                res.setCode("-1");
                res.setMsg(searchResult.getErrorMessage());
                return res;
            }
            Long total = searchResult.getTotal();
            res.setPageTotal(total);
            JSONObject jsonObject = JSON.parseObject(searchResult.getJsonString());
            JSONArray hits = (JSONArray) JSONPath.eval(jsonObject,"$.hits.hits");
            for(int i = 0; i < hits.size(); i++){
                JSONObject hit = hits.getJSONObject(i);
                JSONObject row = hit.getJSONObject("_source");
                rows.add(row);
            }
            res.setRows(rows);
        }catch (Exception e){
            logger.error("",e);
        }
        return res;
    }

    @Override
    public ChartVo chart(Map<String, Object> params) {
        ChartVo res = new ChartVo();
        List<Object> rows = new ArrayList<>();
        try {
            Long beginTime = BeeUtils.getBeginTime(params);
            Long endTime = BeeUtils.getEndTime(params);
            Map<String, String> args = new HashMap<>();
            for(Map.Entry<String,Object> entry : params.entrySet()){
                if(entry.getValue() != null && !entry.getValue().toString().isEmpty()){
                    args.put(entry.getKey(),entry.getValue().toString());
                }
            }
            args.put("beginTime", beginTime.toString());
            args.put("endTime", endTime.toString());
            String[] timeInfo = BeeUtils.parseDateInterval(new Date(beginTime), new Date(endTime));
            logger.debug("timeInfo============>{}", JSON.toJSONString(timeInfo));
            args.put("interval", timeInfo[0]);
            args.put("format", timeInfo[1]);
            args.put("timeZone", timeInfo[2]);
            String queryString = EsQueryStringMap.me().getQueryString("BarDataList", args);
            String[] indices = BeeUtils.getIndices(EsIndicesPrefix.SQL, params);
            SearchResult result = EsJestClient.inst().search(indices, null, queryString);
            if (404 == result.getResponseCode()) {
                res.setCode("-1");
                res.setRows(rows);
                return res;
            }
            JSONObject jsonObject = JSON.parseObject(result.getJsonString());
            JSONArray buckets = (JSONArray) JSONPath.eval(jsonObject, "$.aggregations.bar_aggs.buckets");
            if (buckets == null || buckets.size() == 0) {
                res.setCode("0");
                res.setRows(rows);
                return res;
            }

            for(int i = 0; i < buckets.size(); i++) {
                JSONObject bucket = buckets.getJSONObject(i);
                Map<String,Object> row = new HashMap<>();
                row.put("time",bucket.getString("key_as_string"));
                row.put("请求量",bucket.getLongValue("doc_count"));
                rows.add(row);
            }
            res.setCode("0");
            res.setRows(rows);
        }catch (Exception e){
            logger.error("",e);
        }
        return res;
    }
}
