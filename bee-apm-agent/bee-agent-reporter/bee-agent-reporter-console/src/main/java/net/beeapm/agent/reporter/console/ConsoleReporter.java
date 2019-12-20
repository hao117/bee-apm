package net.beeapm.agent.reporter.console;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.reporter.AbstractReporter;

import java.util.List;


/**
 * @author yuan
 * @date 2018/08/05
 */
@BeePlugin(type = BeePluginType.REPORTER, name = "console")
public class ConsoleReporter extends AbstractReporter {
    public String name = "console";

    @Override
    public int report(Span span) {
        System.out.println(JSON.toJSONString(span));
        return 0;
    }

    @Override
    public int report(List<Span> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            report(list.get(i));
        }
        return size;
    }

    @Override
    public int init() {
        return 0;
    }
}
