package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.common.IdHepler;
import net.beeapm.agent.common.SpanType;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.transmit.TransmitterFactory;

import java.lang.reflect.Method;

/**
 * Created by yuan on 2018/7/31.
 */
public class MethodSpendHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(MethodSpendHandler.class.getSimpleName());
    @Override
    public Span before(Method m, Object[] allArgs) {
        Span span = new Span(SpanType.METHOD);
        logBeginTrace(m,span,log);
        //获取上一级方法的id，作为当前方法的pId，方法执行结束，修改回去。
        String pId = BeeTraceContext.getCurrentId();
        if(pId == null){
            pId = "nvl";
        }
        BeeTraceContext.setPId(pId);

        String gId = BeeTraceContext.getGId();
        if(gId == null){
            gId = IdHepler.id();
        }
        BeeTraceContext.setGId(gId);

        String id = IdHepler.id();
        BeeTraceContext.setCurrentId(id);

        span.setId(id).setPid(pId).setGid(gId).setValue("method",getMethodName(m)).setValue("clazz",getClassName(m));

        return span;
    }

    @Override
    public Object after(Span span, Method m, Object[] allArgs, Object result, Throwable t) {
        if(span == null){
            return result;
        }
        calculateSpend(span);
        logEndTrace(m,span,log);
        //方法执行结束，将退回到上一级方法，当前方法的pId即为上一级方法的id
        BeeTraceContext.setCurrentId(span.getPid());
        TransmitterFactory.transmit(span);
        if(span.getPid() == null || "nvl".equals(span.getPid())){
            BeeTraceContext.clearAll();
        }
        return result;
    }

}
