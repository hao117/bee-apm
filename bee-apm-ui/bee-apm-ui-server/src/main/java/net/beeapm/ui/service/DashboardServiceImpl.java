package net.beeapm.ui.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import io.searchbox.core.SearchResult;
import net.beeapm.ui.common.BeeUtils;
import net.beeapm.ui.common.DateUtils;
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

import java.util.*;

@Service
public class DashboardServiceImpl implements IDashboardService {
    private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);
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
            logger.error("",e);
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
            logger.error("",e);
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
            logger.error("",e);
        }
        return res;
    }

    @Override
    public ResultVo queryErrorLineData(Map<String, Object> params) {
        ResultVo res = new ResultVo();
        try {
            Long beginTime = BeeUtils.getBeginTime(params);
            Long endTime = BeeUtils.getEndTime(params);
            Map<String, String> args = new HashMap<>();
            args.put("beginTime", beginTime.toString());
            args.put("endTime", endTime.toString());
            String[] timeInfo = BeeUtils.parseDateInterval(new Date(beginTime),new Date(endTime));
            logger.debug("timeInfo============>{}",JSON.toJSONString(timeInfo));
            args.put("interval", timeInfo[0]);
            args.put("format", timeInfo[1]);
            args.put("timeZone", timeInfo[2]);
            String queryString = EsQueryStringMap.me().getQueryString("ErrorLine", args);
            String[] indices = BeeUtils.getIndices("bee-error-", params);
            SearchResult result = EsJestClient.inst().search(indices,null,queryString);
            if(404 == result.getResponseCode()){
                res.setCode("-1");
                res.setResult(new ArrayList<>());
                return res;
            }
            JSONObject jsonObject = JSON.parseObject(result.getJsonString());
            JSONArray buckets = (JSONArray)JSONPath.eval(jsonObject,"$.aggregations.app_group.buckets");
            if(buckets == null || buckets.size() == 0){
                res.setCode("0");
                res.setResult(new ArrayList<>());
                return res;
            }
            List<String> appList = new ArrayList<>();
            Map<String,Map<String,Integer>> appsMap = new HashMap<>();
            Map<String,Integer> appDateNumMap = new HashMap<>();
            String beginDateStr = "9999-12-31 23:59:59";
            for(int i = 0; i < buckets.size(); i++){
                JSONObject item = buckets.getJSONObject(i);
                String appName = item.getString("key");
                appList.add(appName);
                JSONArray timeGroup = (JSONArray)JSONPath.eval(item,"$.time_group.buckets");
                Map<String,Integer> appTimeMap = new HashMap<>();
                appsMap.put(appName,appTimeMap);
                for(int j = 0; j < timeGroup.size(); j++){
                    JSONObject tItem = timeGroup.getJSONObject(j);
                    String tmpDate = tItem.getString("key_as_string");
                    appTimeMap.put(tmpDate,tItem.getInteger("doc_count"));
                    appDateNumMap.put(appName+tmpDate,tItem.getInteger("doc_count"));
                    if(beginDateStr.compareTo(tmpDate) > 0){
                        beginDateStr = tmpDate;
                    }
                }
            }
            Date beginDate = DateUtils.parseDate(beginDateStr,timeInfo[1]);
            List<Map<String,Object>> rowList = new ArrayList<>();
            while (!appDateNumMap.isEmpty()) {
                Map<String,Object> row = new HashMap<>();
                row.put("time",beginDateStr);
                for (int i = 0; i < appList.size(); i++) {
                    String appName = appList.get(i);
                    Integer num = appDateNumMap.remove(appName+beginDateStr);
                    if(num == null){
                        num = 0;
                    }
                    row.put(appName,num);
                }
                rowList.add(row);
                beginDate = BeeUtils.nextDateTime(beginDate,timeInfo[0]);
                beginDateStr = DateUtils.format(beginDate,timeInfo[1]);
            }
            Map resMap = new HashMap();
            appList.add(0,"time");
            resMap.put("columns",appList);
            resMap.put("rows",rowList);
            res.setResult(resMap);
            res.setCode("0");
        }catch (Exception e){
            logger.error("",e);
        }
        return res;
    }
}
