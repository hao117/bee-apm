package net.beeapm.agent.boot;

import net.beeapm.agent.common.BeeAgentJarUtils;
import net.beeapm.agent.common.Heartbeat;
import net.beeapm.agent.common.IdHepler;
import net.beeapm.agent.log.BeeLog;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.FieldDefine;
import net.beeapm.agent.plugin.AbstractPlugin;
import net.beeapm.agent.plugin.InterceptPoint;
import net.beeapm.agent.plugin.PluginLoder;
import net.beeapm.agent.reporter.ReporterFactory;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;
import java.util.List;

/**
 * Created by yuan
 */
public class BeeAgent {
    public static void premain(String arguments, Instrumentation inst) {
        BeeLog.write("\n---------------------------------Welcome BeeAPM ---------------------------------------");
        BeeLog.log("start......");
        BeeAgentJarUtils.getAgentJarDirPath();
        IdHepler.init();
        BootPluginFactory.init();
        ReporterFactory.init();
        Heartbeat.start();//心跳

        List<AbstractPlugin> plugins = PluginLoder.loadPlugins();

        AgentBuilder agentBuilder = new AgentBuilder.Default().ignore(ElementMatchers.nameStartsWith("net.beeapm.agent."));

        for(int i = 0; i < plugins.size(); i++) {
            final AbstractPlugin plugin = plugins.get(i);
            InterceptPoint[] interceptPoints = plugin.buildInterceptPoint();
            for (int j = 0; j < interceptPoints.length; j++) {
                final InterceptPoint interceptPoint = interceptPoints[j];
                AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
                    private final LogImpl log = LogManager.getLog("Transform");
                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                            TypeDescription typeDescription,
                                                            ClassLoader classLoader, JavaModule javaModule) {
                        String className = typeDescription.getCanonicalName();
                        log.error("class name : " + className);
                        builder = builder.visit(Advice.to(plugin.interceptorAdviceClass()).on(interceptPoint.buildMethodsMatcher()));
                        FieldDefine[] fields = plugin.buildFieldDefine();
                        if(fields != null && fields.length > 0){
                            for(int x = 0; x < fields.length; x++){
                                builder = builder.defineField(fields[x].name,fields[x].type,fields[x].modifiers);
                            }
                        }
                        return builder;
                    }
                };
                agentBuilder = agentBuilder.type(interceptPoint.buildTypesMatcher()).transform(transformer).asDecorator();
            }
        }


        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            private final LogImpl log = LogManager.getLog("TransformListener");
            @Override
            public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {
                WeavingClassLog.INSTANCE.log(typeDescription,dynamicType);
            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {
            }

            @Override
            public void onError(String s, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {
                log.error("",throwable);
            }

            @Override
            public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }
        };
        agentBuilder.with(listener).installOn(inst);
        //agentBuilder.with(listener).with(AgentBuilder.Listener.StreamWriting.toSystemError()).installOn(inst);
    }


}