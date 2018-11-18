package net.beeapm.ui.service;

import io.searchbox.core.SearchResult;
import net.beeapm.ui.common.BeeUtils;
import net.beeapm.ui.es.EsJestClient;
import net.beeapm.ui.es.EsQueryStringMap;
import net.beeapm.ui.model.SevenKey;
import net.beeapm.ui.model.TwoKeyValue;
import net.beeapm.ui.model.vo.ChartVo;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
            Map<String, String> args = new HashMap<>();
            args.put("beginTime", BeeUtils.getBeginTime(params).toString());
            args.put("endTime", BeeUtils.getEndTime(params).toString());
            String queryString = EsQueryStringMap.me().getQueryString("instCount", args);
            String[] indices = BeeUtils.getIndices("bee-heartbeat-", params);
            SearchResult result = EsJestClient.inst().search(indices, null, queryString);
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
            Map<String, String> args = new HashMap<>();
            args.put("beginTime", BeeUtils.getBeginTime(params).toString());
            args.put("endTime", BeeUtils.getEndTime(params).toString());
            String queryString = EsQueryStringMap.me().getQueryString("count", args);
            String[] indices = BeeUtils.getIndices(indexPrefix, params);
            SearchResult result = EsJestClient.inst().search(indices, null, queryString);
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
}
