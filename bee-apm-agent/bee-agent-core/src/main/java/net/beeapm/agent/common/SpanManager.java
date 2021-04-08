package net.beeapm.agent.common;

import net.beeapm.agent.model.Span;
import net.beeapm.agent.model.SpanKind;
import net.beeapm.agent.reporter.ReporterFactory;

import java.util.Stack;

public class SpanManager {
    private static final ThreadLocal<Stack<Span>> threadLocalSpan = new ThreadLocal<Stack<Span>>();


    private static Span createSpan(String spanKind) {
        Stack<Span> stack = threadLocalSpan.get();
        if (stack == null) {
            stack = new Stack();
            threadLocalSpan.set(stack);
        }
        String parentId, traceId;
        if (stack.isEmpty()) {
            parentId = BeeTraceContext.getParentId();
            if (parentId == null) {
                parentId = "0";
                BeeTraceContext.setParentId(parentId);
            }
            traceId = BeeTraceContext.getTraceId();
            if (traceId == null) {
                traceId = IdHelper.id();
                BeeTraceContext.setTraceId(traceId);
            }
        } else {
            Span parentSpan = stack.peek();
            parentId = parentSpan.getId();
            traceId = parentSpan.getTraceId();
            BeeTraceContext.setParentId(parentId);
        }
        Span span = new Span(spanKind);
        span.setId(IdHelper.id()).setParentId(parentId).setTraceId(traceId);
        return span;
    }

    /**
     * 创建入口span，并放入堆栈中，用于Advice::enter或Handler::before开始的span创建，需要在Advice::exit或Handler::after处调用getExitSpan
     * </br> createEntrySpan和getExitSpan它们是一对的，要配合一起使用，谁也不能缺了谁，不能单独使用。
     *
     * @param spanKind
     * @return
     */
    public static Span createEntrySpan(String spanKind) {
        Span span = createSpan(spanKind);
        Stack<Span> stack = threadLocalSpan.get();
        stack.push(span);
        return span;
    }

    /**
     * 获取出口span，并出堆栈，用于Advice::exit或Handler::after处，事先需要在Advice::enter或Handler::before开始时调用createEntrySpan创建入口span
     * </br> createEntrySpan和getExitSpan它们是一对的，要配合一起使用，谁也不能缺了谁，不能单独使用。
     *
     * @param
     * @return
     */
    public static Span getExitSpan() {
        Stack<Span> stack = threadLocalSpan.get();
        if (stack == null || stack.isEmpty()) {
            BeeTraceContext.clearAll();
            return null;
        }
        return stack.pop();
    }

    /**
     * 获取当前的span，必须在createEntrySpan之后getExitSpan之前。
     *
     * @return
     */
    public static Span getCurrentSpan() {
        Stack<Span> stack = threadLocalSpan.get();
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }

    /**
     * 创建本地span，不入堆栈，不能和getExitSpan配合使用。
     *
     * @param spanKind
     * @return
     */
    public static Span createLocalSpan(String spanKind) {
        return createSpan(spanKind);
    }

    /**
     * 拓扑关系span
     *
     * @param fromApp
     * @param toApp
     */
    public static void createTopologySpan(String fromApp, String toApp) {
        if (fromApp == null || fromApp.isEmpty()) {
            fromApp = "nvl";
        }
        Span span = new Span(SpanKind.TOPOLOGY);
        span.setTraceId(BeeTraceContext.getTraceId());
        span.addAttribute(AttrKey.REQUEST_CLIENT, fromApp);
        span.addAttribute(AttrKey.REQUEST_SERVER, toApp);
        span.setId(IdHelper.id());
        ReporterFactory.report(span);
    }

}
