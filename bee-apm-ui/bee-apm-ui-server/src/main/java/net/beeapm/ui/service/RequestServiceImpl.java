package net.beeapm.ui.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import io.searchbox.core.SearchResult;
import net.beeapm.ui.common.BeeConst;
import net.beeapm.ui.common.EsIndicesPrefix;
import net.beeapm.ui.es.EsJestClient;
import net.beeapm.ui.model.TwoKeyValue;
import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.TableVo;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RequestServiceImpl implements IRequestService {
    private Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);
    @Override
    public TableVo list(Map<String, Object> params) {
        TableVo res = new TableVo();
        res.setCode("0");
        res.setPageNum(Integer.parseInt(params.get("pageNum").toString()));
        params.put("from",(res.getPageNum() - 1) * BeeConst.PAGE_SIZE);
        params.put("size",BeeConst.PAGE_SIZE);
        if(params.get("sort") == null || params.get("sort").toString().isEmpty()){
            params.put("sort","time");
        }

        List<Object> rows = new ArrayList<>();
        /*
        for(int i = 0; i < 10; i++){
            Map item = new HashMap();
            item.put("id", "1"+StringUtils.leftPad(""+((res.getPageNum()-1)*10 + i + 1),19,'0'));
            item.put("gId", "2"+StringUtils.leftPad(""+((res.getPageNum()-1)*10 + i + 1),19,'0'));
            item.put("date",DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            item.put("spend",101);
            item.put("ip","192.168.1."+(i+1));
            item.put("url","http://www.beeapm.net/"+i);
            item.put("server","beeweb"+(i+1));
            item.put("group","beeweb");
            rows.add(item);
        }
        res.setRows(rows);
        */
        try {
            SearchResult searchResult = EsJestClient.inst().search(params, "PageDataList", EsIndicesPrefix.REQUEST);
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
        long time = new Date().getTime();
        for(int i = 0; i < 20; i++){
            rows.add(new TwoKeyValue("time", DateFormatUtils.format(new Date(time+(i*60)),"yyyy-MM-dd HH:mm"),"请求量", RandomUtils.nextInt(500,3000)));
        }
        res.setRows(rows);
        res.setCode("0");
        return res;
    }
}
