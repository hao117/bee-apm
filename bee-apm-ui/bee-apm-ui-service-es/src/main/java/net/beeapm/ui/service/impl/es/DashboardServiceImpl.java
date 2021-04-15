package net.beeapm.ui.service.impl.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.searchbox.core.SearchResult;
import net.beeapm.ui.common.BeeUtils;
import net.beeapm.ui.common.DateUtils;
import net.beeapm.ui.common.EsIndicesPrefix;
import net.beeapm.ui.es.EsJestClient;
import net.beeapm.ui.es.EsMapper;
import net.beeapm.ui.es.EsQueryStringMap;
import net.beeapm.ui.es.EsUtils;
import net.beeapm.ui.model.KeyValue;
import net.beeapm.ui.model.result.ApiResult;
import net.beeapm.ui.model.result.dashboard.NameValue;
import net.beeapm.ui.model.result.dashboard.SummaryResult;
import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.ResultVo;
import net.beeapm.ui.service.IDashboardService;
import org.frameworkset.elasticsearch.boot.BBossESStarter;
import org.frameworkset.elasticsearch.entity.suggest.TermRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardServiceImpl implements IDashboardService {
    private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);


    @Override
    public ResultVo getRequestBarData(Map<String, Object> params) {
        ResultVo res = new ResultVo();
        try {
            SearchResult result = EsJestClient.inst().search(params, "RequestBar", "bee-request-");
            if (404 == result.getResponseCode()) {
                res.setCode("-1");
                res.setResult(new ArrayList<>());
                return res;
            }
            JSONObject jsonObject = JSON.parseObject(result.getJsonString());
            JSONArray buckets = (JSONArray) JSONPath.eval(jsonObject, "$.aggregations.req_bar.buckets");
            if (buckets == null || buckets.size() == 0) {
                res.setCode("0");
                res.setResult(new ArrayList<>());
                return res;
            }
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < buckets.size(); i++) {
                JSONObject obj = buckets.getJSONObject(i);
                Map<String, Object> item = new HashMap<>();
                item.put("区间", obj.getString("key"));
                item.put("请求数量", obj.getLongValue("doc_count"));
                list.add(item);
            }
            res.setCode("0");
            res.setResult(list);
        } catch (Exception e) {
            logger.error("", e);
        }
        return res;
    }

    @Override
    public ChartVo getRequestLineData(Map<String, Object> params) {
        ChartVo res = new ChartVo();
        try {
            Long beginTime = BeeUtils.getBeginTime(params);
            Long endTime = BeeUtils.getEndTime(params);
            Map<String, String> args = new HashMap<>();
            args.put("beginTime", beginTime.toString());
            args.put("endTime", endTime.toString());
            String[] timeInfo = BeeUtils.parseDateInterval(new Date(beginTime), new Date(endTime));
            logger.debug("timeInfo============>{}", JSON.toJSONString(timeInfo));
            args.put("interval", timeInfo[0]);
            args.put("format", timeInfo[1]);
            args.put("timeZone", timeInfo[2]);
            String queryString = EsQueryStringMap.me().getQueryString("RequestLine", args);
            String[] indices = BeeUtils.getIndices("bee-request-", params);
            SearchResult result = EsJestClient.inst().search(indices, null, queryString);

            if (404 == result.getResponseCode()) {
                res.setCode("-1");
                res.setRows(new ArrayList<>());
                return res;
            }
            JSONObject jsonObject = JSON.parseObject(result.getJsonString());
            JSONArray buckets = (JSONArray) JSONPath.eval(jsonObject, "$.aggregations.req_qs.buckets");
            if (buckets == null || buckets.size() == 0) {
                res.setCode("0");
                res.setRows(new ArrayList<>());
                return res;
            }
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < buckets.size(); i++) {
                JSONObject obj = buckets.getJSONObject(i);
                Map<String, Object> item = new HashMap<>();
                item.put("time", obj.getString("key_as_string"));
                JSONArray reqBuckets = (JSONArray) JSONPath.eval(obj, "$.req_aggs.buckets");
                for (int j = 0; j < reqBuckets.size(); j++) {
                    JSONObject reqObj = reqBuckets.getJSONObject(j);
                    item.put(reqObj.getString("key"), reqObj.getLongValue("doc_count"));
                }
                list.add(item);
            }
            res.setCode("0");
            res.setRows(list);
        } catch (Exception e) {
            logger.error("", e);
        }
        return res;
    }

    @Override
    public Long queryInstCount(Map<String, Object> params) {
        Long count = 0L;
        try {
            params.put("endTime", DateUtils.endDate((String) params.get("endTime")));
            SearchResult result = EsJestClient.inst().search(params, "instCount", EsIndicesPrefix.HEARTBEAT);
            if (404 == result.getResponseCode()) {
                return 0L;
            }
            count = result.getJsonObject().getAsJsonObject("aggregations").getAsJsonObject("inst_count").getAsJsonPrimitive("value").getAsLong();
        } catch (Exception e) {
            logger.error("", e);
        }
        return count;
    }

    @Override
    public Long queryAllCount(Map<String, Object> params) {
        return queryCount("bee-*-", params);
    }

    private Long queryCount(String indexPrefix, Map<String, Object> params) {
        Long count = 0L;
        try {
            SearchResult result = EsJestClient.inst().search(params, "count", indexPrefix);
            if (404 == result.getResponseCode()) {
                return 0L;
            }
            count = result.getTotal();
        } catch (Exception e) {
            logger.error("", e);
        }
        return count;
    }

    @Override
    public Long queryRequestCount(Map<String, Object> params) {
        return queryCount("bee-request-", params);
    }

    @Override
    public Long queryErrorCount(Map<String, Object> params) {
        return queryCount("bee-error-", params);
    }

    @Override
    public ApiResult<List<NameValue>> queryErrorPieData(Map<String, Object> params) {
        try {
            String path = EsUtils.buildSearchPath("bee-error-");
            TermRestResponse response = EsUtils.getClient(EsMapper.DASHBOARD).termSuggest(path, "error-pie", null);
            List<Map<String, Object>> buckets = (List<Map<String, Object>>) response.getAggBuckets("errorCount");
            logger.debug("查询结果:{}", JSON.toJSONString(response));
            List<NameValue> list = Lists.newArrayList();
            Long total = 0L;
            if (buckets != null) {
                for (Map<String, Object> item : buckets) {
                    NameValue value = NameValue.builder()
                            .name(item.get("key").toString())
                            .value(item.get("doc_count"))
                            .build();
                    total += Long.parseLong(item.get("doc_count").toString());
                    list.add(value);
                }
                Long hitTotal = Long.parseLong(response.getSearchHits().getTotal().toString());
                list.add(NameValue.builder().name("其它").value(hitTotal - total).build());
            }
            list.sort((o1, o2) -> (int) (Long.parseLong(o2.getValue().toString()) - Long.parseLong(o1.getValue().toString())));
            return ApiResult.success(list);
        } catch (Exception e) {
            logger.error("", e);
        }
        return ApiResult.fail("查询失败");
    }

    @Override
    public ApiResult<Map<String, List<Object>>> queryErrorLineData(Map<String, Object> params) {
        try {
            String path = EsUtils.buildSearchPath("bee-error-");
            TermRestResponse response = EsUtils.getClient(EsMapper.DASHBOARD).termSuggest(path, "error-line", null);
            List<Map<String, Object>> buckets = (List<Map<String, Object>>) response.getAggBuckets("app_group");
            Map<String, List<Object>> resultMap = Maps.newLinkedHashMap();
            if (buckets != null) {
                for (Map<String, Object> item : buckets) {
                    String name = item.get("key").toString();
                    List<Map<String, Object>> timeGroupBuckets = Lists.newArrayList();
                    Map<String, Object> timeGroup = (Map<String, Object>) item.get("time_group");
                    if (timeGroup != null) {
                        timeGroupBuckets = (List<Map<String, Object>>) timeGroup.get("buckets");
                    }
                    List<Object> lineDatalist = Lists.newArrayList();
                    for (int i = 0; timeGroupBuckets != null && i < timeGroupBuckets.size(); i++) {
                        lineDatalist.add(timeGroupBuckets.get(i).get("doc_count"));
                    }
                    resultMap.put(name, lineDatalist);
                }
            }
            logger.debug("查询结果:{}", JSON.toJSONString(resultMap));
            return ApiResult.success(resultMap);
        } catch (Exception e) {
            logger.error("", e);
        }
        return ApiResult.fail("查询失败");
    }

    @Override
    public ApiResult<List<SummaryResult>> summary() {
        Long errorCount = EsUtils.countAll("bee-error-");
        Long reqCount = EsUtils.countAll("bee-server-");
        Long instCount = EsUtils.countAll("bee-heartbeat-");
        Long allLogCount = EsUtils.countAll("bee-*-");

        SummaryResult errorSummary = new SummaryResult();
        errorSummary.setTitle("异常量");
        errorSummary.setIcon("mdi:bell-circle-outline");
        errorSummary.setValue(errorCount);
        errorSummary.setColor("red");
        errorSummary.setAction("今天");

        SummaryResult reqSummary = new SummaryResult();
        reqSummary.setTitle("请求量");
        reqSummary.setIcon("mdi:exit-to-app");
        reqSummary.setValue(reqCount);
        reqSummary.setColor("green");
        reqSummary.setAction("今天");

        SummaryResult instSummary = new SummaryResult();
        instSummary.setTitle("应用数");
        instSummary.setIcon("mdi:apps");
        instSummary.setValue(instCount);
        instSummary.setColor("blue");
        instSummary.setAction("今天");

        SummaryResult allLogSummary = new SummaryResult();
        allLogSummary.setTitle("日志总量");
        allLogSummary.setIcon("mdi:format-indent-increase");
        allLogSummary.setValue(allLogCount);
        allLogSummary.setColor("purple");
        allLogSummary.setAction("今天");

        List<SummaryResult> list = Lists.newArrayList();
        list.add(errorSummary);
        list.add(reqSummary);
        list.add(instSummary);
        list.add(allLogSummary);

        return ApiResult.success(list);
    }
}
