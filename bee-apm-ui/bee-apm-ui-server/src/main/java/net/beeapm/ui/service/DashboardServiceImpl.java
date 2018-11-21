package net.beeapm.ui.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.google.gson.JsonObject;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.MetricAggregation;
import net.beeapm.ui.common.BeeUtils;
import net.beeapm.ui.es.EsJestClient;
import net.beeapm.ui.es.EsQueryStringMap;
import net.beeapm.ui.model.KeyValue;
import net.beeapm.ui.model.SevenKey;
import net.beeapm.ui.model.TwoKeyValue;
import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.ResultVo;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements IDashboardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardServiceImpl.class);
    public Map<String,Object> getRequestBarData(Map<String,String> params){
        Map<String,Object> res = new HashMap<>();
        res.put("code",0);
        List list = new ArrayList();
        list.add(new TwoKeyValue("区间","0~200", "请求数量",RandomUtils.nextInt(1001,5000)));
        list.add(new TwoKeyValue("区间","200~500", "请求数量",RandomUtils.nextInt(1001,5000)));
        list.add(new TwoKeyValue("区间","500~1000", "请求数量",RandomUtils.nextInt(1001,5000)));
        list.add(new TwoKeyValue("区间","1000~2000", "请求数量",RandomUtils.nextInt(1001,5000)));
        list.add(new TwoKeyValue("区间","2000~5000", "请求数量",RandomUtils.nextInt(1001,5000)));
        list.add(new TwoKeyValue("区间","5000~*", "请求数量",RandomUtils.nextInt(1001,5000)));
        res.put("rows",list);
        return res;
    }

    @Override
    public ChartVo getRequestLineData(Map<String, String> params) {
        ChartVo res = new ChartVo();
        res.setCode("0");
        List list = new ArrayList();
        for(int i = 0; i < 10; i++) {
            list.add(new SevenKey(i+1, "time", "0~200", "200~500", "500~1000", "1000~2000", "2000~3000", "3000~*"));
        }
        res.setRows(list);
        return res;
    }

    @Override
    public Long queryInstCount(Map<String, Object> params) {
        Long count = 0L;
        try {
            SearchResult result = EsJestClient.inst().search(params, "instCount", "bee-heartbeat-");
            if(404 == result.getResponseCode()){
                return 0L;
            }
            count = result.getJsonObject().getAsJsonObject("aggregations").getAsJsonObject("inst_count").getAsJsonPrimitive("value").getAsLong();
        }catch (Exception e){
            LOGGER.error("",e);
        }
        return count;
    }

    @Override
    public Long queryAllCount(Map<String, Object> params) {
        return queryCount("bee-*-",params);
    }

    private Long queryCount(String indexPrefix,Map<String, Object> params) {
        Long count = 0L;
        try {
            SearchResult result = EsJestClient.inst().search(params, "count", indexPrefix);
            if(404 == result.getResponseCode()){
                return 0L;
            }
            count = result.getTotal();
        }catch (Exception e){
            LOGGER.error("",e);
        }
        return count;
    }

    @Override
    public Long queryRequestCount(Map<String, Object> params) {
        return queryCount("bee-request-",params);
    }

    @Override
    public Long queryErrorCount(Map<String, Object> params) {
        return queryCount("bee-error-",params);
    }

    @Override
    public ResultVo queryErrorPieData(Map<String, Object> params) {
        ResultVo res = new ResultVo();
        try {
            SearchResult result = EsJestClient.inst().search(params, "errorPie", "bee-error-");
            if(404 == result.getResponseCode()){
                res.setCode("-1");
                res.setResult(new ArrayList<>());
                return res;
            }
            JSONObject jsonObject = JSON.parseObject(result.getJsonString());
            JSONArray buckets = (JSONArray)JSONPath.eval(jsonObject,"$.aggregations.errorCount.buckets");
            if(buckets == null || buckets.size() == 0){
                res.setCode("0");
                res.setResult(new ArrayList<>());
                return res;
            }
            List<KeyValue> list = new ArrayList<>();
            for(int i = 0; i < buckets.size(); i++){
                JSONObject item = buckets.getJSONObject(i);
                list.add(new KeyValue(item.getString("key"),item.getInteger("doc_count")));
            }
            res.setCode("0");
            res.setResult(list);
        }catch (Exception e){
            LOGGER.error("",e);
        }
        return res;
    }
}
