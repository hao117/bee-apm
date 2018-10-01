package net.beeapm.ui.service;

import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.TableVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return null;
    }
}
