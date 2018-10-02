package net.beeapm.ui.service;

import net.beeapm.ui.model.KeyValue;
import net.beeapm.ui.model.TwoKeyValue;
import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.TableVo;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RequestServiceImpl implements IRequestService {
    @Override
    public TableVo list(Map<String, Object> params) {
        TableVo res = new TableVo();
        res.setCode("0");
        res.setPageNum(Integer.parseInt(params.get("pageNum").toString()));
        res.setPageTotal(170);
        List<Object> rows = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Map item = new HashMap();
            item.put("id", (res.getPageNum()-1)*10 + i + 1);
            item.put("gId", "6666666666666");
            item.put("ip","192.168.1."+(i+1));
            item.put("url","http://www.beeapm.net/"+i);
            item.put("server","beeweb"+(i+1));
            item.put("group","beeweb");
            rows.add(item);
        }
        res.setRows(rows);
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
