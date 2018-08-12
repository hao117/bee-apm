package net.beeapm.agent.boot;

import net.beeapm.agent.common.BeeAgentJarUtils;
import net.beeapm.agent.plugin.IPlugin;
import net.beeapm.agent.plugin.InterceptPoint;
import net.beeapm.agent.plugin.PluginLoder;
import net.beeapm.agent.transmit.TransmitterFactory;
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
        System.out.println("\n---------------------------------this is an bytebuddy sample ---------------------------------------");

        BeeAgentJarUtils.getAgentJarDirPath();
        TransmitterFactory.init();

        List<IPlugin> plugins = PluginLoder.loadPlugins();

        AgentBuilder agentBuilder = new AgentBuilder.Default().ignore(ElementMatchers.nameStartsWith("net.beeapm.agent."));

        for(int i = 0; i < plugins.size(); i++) {
            final IPlugin plugin = plugins.get(i);
            InterceptPoint[] interceptPoints = plugin.buildInterceptPoint();
            for (int j = 0; j < interceptPoints.length; j++) {
                final InterceptPoint interceptPoint = interceptPoints[j];
                AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                            TypeDescription typeDescription,
                                                            ClassLoader classLoader, JavaModule javaModule) {
                        String className = typeDescription.getCanonicalName();
                        System.out.println("++++++++ class name = " + className);
                        //...
//                      builder = builder.method(methodSpendPlugin.buildMethodsMatcher())//匹配任意方法
//                                 .intercept(Advice.to(methodSpendPlugin.interceptorAdviceClass()));
                        builder = builder.visit(Advice.to(plugin.interceptorAdviceClass()).on(interceptPoint.buildMethodsMatcher()));
                        //builder = builder.visit(Advice.to(plugin.interceptorAdviceClass(),ClassFileLocator.ForClassLoader.of(classLoader)).on(interceptPoint.buildMethodsMatcher()));
                        return builder;
                    }
                };
                agentBuilder = agentBuilder.type(interceptPoint.buildTypesMatcher()).transform(transformer);
            }
        }


        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
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

            }

            @Override
            public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }
        };

        agentBuilder.with(listener).installOn(inst);
    }


}