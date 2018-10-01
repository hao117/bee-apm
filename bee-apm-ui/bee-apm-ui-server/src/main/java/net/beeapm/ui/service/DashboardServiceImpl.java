package net.beeapm.ui.service;

import net.beeapm.ui.model.SevenKey;
import net.beeapm.ui.model.TwoKeyValue;
import net.beeapm.ui.model.vo.ChartVo;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements IDashboardService {
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
}
