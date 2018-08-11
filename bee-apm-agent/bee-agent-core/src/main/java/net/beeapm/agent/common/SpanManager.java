package net.beeapm.agent.common;

import net.beeapm.agent.model.Span;

import java.util.Stack;

public class SpanManager {
    private static final ThreadLocal<Stack<Span>> threadLocalSpan = new ThreadLocal<Stack<Span>>();

    private static Span createSpan(String spanType){
        Stack<Span> stack = threadLocalSpan.get();
        if(stack == null){
            stack = new Stack();
            threadLocalSpan.set(stack);
        }
        String pId,gId;
        if(stack.isEmpty()){
            pId = BeeTraceContext.getPId();
            if(pId == null){
                pId = "nvl";
                BeeTraceContext.setPId(pId);
            }
            gId = BeeTraceContext.getGId();
            if(gId == null){
                gId = IdHepler.id();
                BeeTraceContext.setGId(gId);
            }
        }else{
            Span parentSpan = stack.peek();
            pId = parentSpan.getId();
            gId = parentSpan.getGid();
            BeeTraceContext.setPId(pId);
        }
        Span span = new Span(spanType);
        span.setId(IdHepler.id()).setPid(pId).setGid(gId);
        return span;
    }

    /**
     * 创建入口span，并放入堆栈中，用于Advice::enter或Handler::before开始的span创建，需要在Advice::exit或Handler::after处调用getExitSpan
     * </br> createEntrySpan和getExitSpan它们是一对的，要配合一起使用，谁也不能缺了谁，不能单独使用。
     * @param spanType
     * @return
     */
    public static Span createEntrySpan(String spanType){
        Span span = createSpan(spanType);
        Stack<Span> stack = threadLocalSpan.get();
        stack.push(span);
        return span;
    }

    /**
     * 获取出口span，并出堆栈，用于Advice::exit或Handler::after处，事先需要在Advice::enter或Handler::before开始时调用createEntrySpan创建入口span
     * </br> createEntrySpan和getExitSpan它们是一对的，要配合一起使用，谁也不能缺了谁，不能单独使用。
     * @param
     * @return
     */
    public static Span getExitSpan(){
        Stack<Span> stack = threadLocalSpan.get();
        if(stack == null || stack.isEmpty()){
            BeeTraceContext.clearAll();
            return null;
        }
        return stack.pop();
    }

    /**
     * 获取当前的span，必须在createEntrySpan之后getExitSpan之前。
     * @return
     */
    public static Span getCurrentSpan(){
        Stack<Span> stack = threadLocalSpan.get();
        if(stack == null || stack.isEmpty()){
            return null;
        }
        return stack.peek();
    }

    /**
     * 创建本地span，不入堆栈，不能和getExitSpan配合使用。
     * @param spanType
     * @return
     */
    public static Span createLocalSpan(String spanType){
        return createSpan(spanType);
    }


}
