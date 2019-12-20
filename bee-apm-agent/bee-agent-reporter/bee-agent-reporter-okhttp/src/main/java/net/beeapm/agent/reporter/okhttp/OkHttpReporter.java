package net.beeapm.agent.reporter.okhttp;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.reporter.AbstractReporter;

import java.util.List;

/**
 * @author yuan
 * @date 2018/08/30
 */
@BeePlugin(type = BeePluginType.REPORTER, name = "kafka")
public class OkHttpReporter extends AbstractReporter {
    public String name = "okhttp";

    @Override
    public int report(Span span) {
        OkHttpHelper.getInstance().post(JSON.toJSONString(span));
        return 1;
    }

    @Override
    public int report(List<Span> list) {
        OkHttpHelper.getInstance().post(JSON.toJSONString(list));
        return list.size();
    }

    @Override
    public int init() {
        return 0;
    }
}
