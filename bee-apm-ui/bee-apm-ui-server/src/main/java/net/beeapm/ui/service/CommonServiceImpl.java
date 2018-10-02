package net.beeapm.ui.service;

import net.beeapm.ui.model.TwoKeyValue;
import net.beeapm.ui.model.vo.ResultVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl implements  ICommonService {
    @Override
    public ResultVo queryGroupList(Map<String, String> param) {
        List groupList = new ArrayList();
        groupList.add(new TwoKeyValue("name","crm前端","value","crm-web"));
        groupList.add(new TwoKeyValue("name","crm资源","value","crm-res"));
        groupList.add(new TwoKeyValue("name","crm资产","value","crm-inst"));
        ResultVo res = new ResultVo();
        res.setCode("0");
        res.setResult(groupList);
        return res;
    }
}
