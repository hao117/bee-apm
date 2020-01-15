package net.beeapm.ui.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import io.searchbox.core.SearchResult;
import net.beeapm.ui.common.BeeConst;
import net.beeapm.ui.common.BeeUtils;
import net.beeapm.ui.common.DateUtils;
import net.beeapm.ui.common.EsIndicesPrefix;
import net.beeapm.ui.es.EsJestClient;
import net.beeapm.ui.es.EsQueryStringMap;
import net.beeapm.ui.model.TopologyEdge;
import net.beeapm.ui.model.vo.BaseVo;
import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.ResultVo;
import net.beeapm.ui.model.vo.TableVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author yuan
 * @date 2018-10-01
 */
@Service
public class RequestServiceImpl implements IRequestService {
    private Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);
    private static final long HALF_HOUR = 30 * 60 * 1000;
    private static final int VAL_404 = 404;

    @Override
    public TableVo list(Map<String, Object> params) {
        TableVo res = new TableVo();
        res.setCode("0");
        res.setPageNum(Integer.parseInt(params.get("pageNum").toString()));
        params.put("from", (res.getPageNum() - 1) * BeeConst.PAGE_SIZE);
        params.put("size", BeeConst.PAGE_SIZE);
        if (params.get("sort") == null || params.get("sort").toString().isEmpty()) {
            params.put("sort", "time");
        }
        List<Object> rows = new ArrayList<>();
        try {
            Long total = queryList(params, EsIndicesPrefix.REQUEST, "PageDataList", res, rows);
            if (total < 0) {
                return res;
            }
            res.setPageTotal(total);
            res.setRows(rows);
        } catch (Exception e) {
            logger.error("", e);
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
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
                    args.put(entry.getKey(), entry.getValue().toString());
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
            String[] indices = BeeUtils.getIndices(EsIndicesPrefix.REQUEST, params);
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

            for (int i = 0; i < buckets.size(); i++) {
                JSONObject bucket = buckets.getJSONObject(i);
                Map<String, Object> row = new HashMap<>();
                row.put("time", bucket.getString("key_as_string"));
                row.put("请求量", bucket.getLongValue("doc_count"));
                rows.add(row);
            }
            res.setCode("0");
            res.setRows(rows);
        } catch (Exception e) {
            logger.error("", e);
        }
        return res;
    }

    @Override
    public ResultVo callTree(Map<String, Object> params) {
        logger.debug("请求参数:{}", JSON.toJSONString(params));
        ResultVo res = new ResultVo();
        try {
            Date time = DateUtils.parseDate((String) params.get("time"), "yyyy-MM-dd'T'HH:mm:ssZ");
            Map<String, Object> args = new HashMap<>(8);
            args.put("beginTime", DateUtils.format(new Date(time.getTime() - HALF_HOUR), "yyyy-MM-dd HH:mm"));
            args.put("endTime", DateUtils.format(new Date(time.getTime() + HALF_HOUR), "yyyy-MM-dd HH:mm"));
            args.put("gid", params.get("gid"));
            logger.debug("查询参数:{}", JSON.toJSONString(args));
            List<Object> rows = new ArrayList<>();
            Long total = queryList(args, EsIndicesPrefix.REQUEST, "QueryList", res, rows);
            if (total < 0) {
                return res;
            }
            total = queryList(args, EsIndicesPrefix.PROCESS, "QueryList", res, rows);
            if (total < 0) {
                return res;
            }
            logger.debug("查询结果:{}", JSON.toJSONString(rows));
            buildTree(rows);
            res.setCode("0");
            res.setResult(rows);
            logger.debug("返回结果:{}", JSON.toJSONString(res));
        } catch (Exception e) {
            logger.error("", e);
        }

        return res;
    }

    private Long queryList(Map<String, Object> args, String indexPrefix, String mapId, BaseVo res, List<Object> rows) throws Exception {
        SearchResult searchResult = EsJestClient.inst().search(args, mapId, indexPrefix);
        if (VAL_404 == searchResult.getResponseCode()) {
            res.setCode("-1");
            res.setMsg(searchResult.getErrorMessage());
            return -1L;
        }
        Long total = searchResult.getTotal();
        JSONArray hits = (JSONArray) JSONPath.eval(JSON.parseObject(searchResult.getJsonString()), "$.hits.hits");
        for (int i = 0; i < hits.size(); i++) {
            JSONObject hit = hits.getJSONObject(i);
            JSONObject row = hit.getJSONObject("_source");
            rows.add(row);
        }
        return total;
    }

    private void parseItem(JSONObject item) {
        String type = item.getString("type");
        if (type.equals("req")) {
            item.put("text", JSONPath.eval(item, "$.tags.url"));
        } else {
            item.put("text", JSONPath.eval(item, "$.tags.clazz") + "." + JSONPath.eval(item, "$.tags.method"));
        }
    }

    private void buildTree(List<Object> rows) {
        int len = rows.size();
        for (int i = 0; i < len; i++) {
            JSONObject item1 = (JSONObject) rows.get(i);
            parseItem(item1);
            String pid1 = item1.getString("pid");
            if (pid1.equals("nvl")) {
                continue;
            }
            //找parent
            for (int j = 0; j < len; j++) {
                if (i == j) {
                    continue;
                }
                JSONObject item2 = (JSONObject) rows.get(j);
                String id2 = item2.getString("id");
                if (pid1.equals(id2)) {
                    List<Object> list = item2.getJSONArray("children");
                    if (list == null) {
                        list = new JSONArray();
                        item2.put("children", list);
                        item2.put("opened", true);
                    }
                    item1.put("_d", null);
                    list.add(item1);
                    break;
                }
            }
        }
        for (int i = len - 1; i >= 0; i--) {
            JSONObject row = (JSONObject) rows.get(i);
            if (row.containsKey("_d")) {
                rows.remove(i);
            }
        }
    }

    @Override
    public ResultVo topology(Map<String, Object> params) {
        logger.debug("请求参数:{}", JSON.toJSONString(params));
        ResultVo res = new ResultVo();
        try {
            Date time = DateUtils.parseDate((String) params.get("time"), "yyyy-MM-dd'T'HH:mm:ssZ");
            Map<String, Object> args = new HashMap<>(8);
            args.put("beginTime", DateUtils.format(new Date(time.getTime() - HALF_HOUR), "yyyy-MM-dd HH:mm"));
            args.put("endTime", DateUtils.format(new Date(time.getTime() + HALF_HOUR), "yyyy-MM-dd HH:mm"));
            args.put("gid", params.get("gid"));
            logger.debug("查询参数:{}", JSON.toJSONString(args));
            List<Object> rows = new ArrayList<>();
            Long total = queryList(args, EsIndicesPrefix.TOPOLOGY, "QueryList", res, rows);
            if (total < 0) {
                return res;
            }
            logger.debug("查询结果:{}", JSON.toJSONString(rows));
            res.setCode("0");
            res.setResult(buildTopology(rows));
            logger.debug("返回结果:{}", JSON.toJSONString(res));
        } catch (Exception e) {
            logger.error("查询拓扑关系失败", e);
        }

        return res;
    }

    private Map<String, Object> buildTopology(List<Object> rows) {
        int len = rows.size();
        HashMap<String, TopologyEdge> edgeMap = new HashMap<>(16);
        Set<String> nodeSet = new HashSet<>();
        for (int i = 0; i < len; i++) {
            JSONObject item = (JSONObject) rows.get(i);
            String to = item.getString("app");
            String from = JSONPath.eval(item, "$.tags.from").toString();
            nodeSet.add(to);
            nodeSet.add(from);
            String key = from + "_" + to;
            TopologyEdge edge = edgeMap.get(key);
            if (edge == null) {
                edge = new TopologyEdge(0);
                edgeMap.put(key, edge);
            }
            edge.setFrom(from);
            edge.setTo(to);
            edge.setTimes(edge.getTimes() + 1);
        }

        List<Object> nodes = new ArrayList<>();
        Iterator<String> nodeIterator = nodeSet.iterator();
        while (nodeIterator.hasNext()) {
            String name = nodeIterator.next();
            Map<String, String> node = new HashMap<>(16);
            node.put("id", name);
            //node.put("shape", "circle");
//            if(name.equals("nvl")){
//                node.put("label", "start");
//                node.put("color", "#C2FABC");
//            }else{
            node.put("label", name);

            nodes.add(node);
        }
        for (Map.Entry<String, TopologyEdge> entry : edgeMap.entrySet()) {
            TopologyEdge edge = entry.getValue();
        }
        Map<String, Object> result = new HashMap<>(4);
        result.put("nodes", nodes);
        result.put("edges", edgeMap.values());
        return result;
    }
}
