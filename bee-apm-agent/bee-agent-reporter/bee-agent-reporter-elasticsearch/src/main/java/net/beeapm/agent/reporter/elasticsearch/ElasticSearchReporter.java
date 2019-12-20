package net.beeapm.agent.reporter.elasticsearch;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.reporter.AbstractReporter;

import java.util.List;

/**
 * @author yuan
 * @date 2018/08/13
 */
@BeePlugin(type = BeePluginType.REPORTER, name = "elasticsearch")
public class ElasticSearchReporter extends AbstractReporter {
    @Override
    public int report(Span span) {
        return 0;
    }

    @Override
    public int report(List<Span> span) {
        return 0;
    }

    @Override
    public int init() {
        return 0;
    }
}
