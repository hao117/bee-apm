package net.beeapm.ui.service.impl.es;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.searchbox.core.SearchResult;
import net.beeapm.ui.common.DateUtils;
import net.beeapm.ui.common.EsIndicesPrefix;
import net.beeapm.ui.common.JsonUtils;
import net.beeapm.ui.es.EsJestClient;
import net.beeapm.ui.es.EsMapper;
import net.beeapm.ui.es.EsUtils;
import net.beeapm.ui.model.result.ApiResult;
import net.beeapm.ui.model.result.dashboard.NameValue;
import net.beeapm.ui.model.result.dashboard.SummaryResult;
import net.beeapm.ui.service.IDashboardService;
import org.frameworkset.elasticsearch.entity.MapRestResponse;
import org.frameworkset.elasticsearch.entity.suggest.TermRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardServiceImpl implements IDashboardService {
    private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

    @Override
    public ApiResult<List<NameValue>> getRequestBarData(Map<String, Object> params) {
        String path = EsUtils.buildSearchPath("bee-server-");
        MapRestResponse response = EsUtils.getClient(EsMapper.DASHBOARD).search(path, "request-bar", params);
        logger.debug("查询结果 = {}", JsonUtils.toJSONString(response));
        List<Map<String, Object>> buckets = (List<Map<String, Object>>) response.getAggBuckets("req_bar");
        List<NameValue> list = Lists.newArrayList();
        if (buckets != null) {
            for (Map<String, Object> item : buckets) {
                NameValue value = NameValue.builder()
                        .name(item.get("key").toString())
                        .value(item.get("doc_count"))
                        .build();
                list.add(value);
            }
        }
        logger.debug("返回结果 = {}", JsonUtils.toJSONString(list));
        return ApiResult.success(list);
    }

    @Override
    public ApiResult<Map<String, List<Integer>>> getRequestLineData(Map<String, Object> params) {
        try {
            String path = EsUtils.buildSearchPath("bee-server-");
            Map<String, Object> qryParam = Maps.newHashMap();
            qryParam.put("minTime", DateUtils.setTime(new Date(), 0, 0, 0, 0).getTime());
            qryParam.put("maxTime", DateUtils.setTime(new Date(), 23, 59, 59, 999).getTime());
            TermRestResponse response = EsUtils.getClient(EsMapper.DASHBOARD).termSuggest(path, "request-line", qryParam);
            List<Map<String, Object>> buckets = (List<Map<String, Object>>) response.getAggBuckets("app_group");
            logger.debug("查询结果:{}", JsonUtils.toJSONString(buckets));
            Map<String, List<Integer>> resultMap = Maps.newLinkedHashMap();
            if (buckets != null) {
                for (Map<String, Object> item : buckets) {
                    String name = item.get("key").toString();
                    List<Map<String, Object>> timeGroupBuckets = Lists.newArrayList();
                    Map<String, Object> timeGroup = (Map<String, Object>) item.get("time_group");
                    if (timeGroup != null) {
                        timeGroupBuckets = (List<Map<String, Object>>) timeGroup.get("buckets");
                    }
                    List<Integer> lineDatalist = Lists.newArrayList();
                    for (int i = 0; timeGroupBuckets != null && i < timeGroupBuckets.size(); i++) {
                        lineDatalist.add((Integer) timeGroupBuckets.get(i).get("doc_count"));
                    }
                    resultMap.put(name, lineDatalist);
                }
            }
            logger.debug("返回结果:{}", JsonUtils.toJSONString(resultMap));
            return ApiResult.success(resultMap);
        } catch (Exception e) {
            logger.error("", e);
        }
        return ApiResult.fail("查询失败");
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
            logger.debug("查询结果:{}", JsonUtils.toJSONString(buckets));
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
            logger.debug("返回结果:{}", JsonUtils.toJSONString(response));
            return ApiResult.success(list);
        } catch (Exception e) {
            logger.error("", e);
        }
        return ApiResult.fail("查询失败");
    }

    @Override
    public ApiResult<Map<String, List<Integer>>> queryErrorLineData(Map<String, Object> params) {
        try {
            String path = EsUtils.buildSearchPath("bee-error-");
            Map<String, Object> qryParam = Maps.newHashMap();
            qryParam.put("minTime", DateUtils.setTime(new Date(), 0, 0, 0, 0).getTime());
            qryParam.put("maxTime", DateUtils.setTime(new Date(), 23, 59, 59, 999).getTime());
            TermRestResponse response = EsUtils.getClient(EsMapper.DASHBOARD).termSuggest(path, "error-line", qryParam);
            List<Map<String, Object>> buckets = (List<Map<String, Object>>) response.getAggBuckets("app_group");
            logger.debug("查询结果:{}", JsonUtils.toJSONString(buckets));
            Map<String, List<Integer>> resultMap = Maps.newLinkedHashMap();
            if (buckets != null) {
                for (Map<String, Object> item : buckets) {
                    String name = item.get("key").toString();
                    List<Map<String, Object>> timeGroupBuckets = Lists.newArrayList();
                    Map<String, Object> timeGroup = (Map<String, Object>) item.get("time_group");
                    if (timeGroup != null) {
                        timeGroupBuckets = (List<Map<String, Object>>) timeGroup.get("buckets");
                    }
                    List<Integer> lineDatalist = Lists.newArrayList();
                    for (int i = 0; timeGroupBuckets != null && i < timeGroupBuckets.size(); i++) {
                        lineDatalist.add((Integer) timeGroupBuckets.get(i).get("doc_count"));
                    }
                    resultMap.put(name, lineDatalist);
                }
            }
            logger.debug("返回结果:{}", JsonUtils.toJSONString(resultMap));
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
