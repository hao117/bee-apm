package net.beeapm.agent.boot;

import net.beeapm.agent.common.BeeAgentJarUtils;
import net.beeapm.agent.plugin.MethodSpendPlugin;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

/**
 * Created by yuan
 */
public class BeeAgent {
    public static void premain(String arguments, Instrumentation inst) {
        System.out.println("\n---------------------------------this is an bytebuddy sample ---------------------------------------");

        final MethodSpendPlugin methodSpendPlugin = new MethodSpendPlugin();

        BeeAgentJarUtils.getAgentJarFile();

        AgentBuilder.Transformer transformer =  new AgentBuilder.Transformer()  {
            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                    TypeDescription typeDescription,
                                                    ClassLoader classLoader, JavaModule javaModule) {
                String className = typeDescription.getCanonicalName();
                System.out.println("++++++++ class name = " + className);
                //...
//                builder = builder.method(methodSpendPlugin.buildMethodsMatcher())//匹配任意方法
//                                 .intercept(Advice.to(methodSpendPlugin.adviceClass()));
                builder = builder.visit(Advice.to(methodSpendPlugin.adviceClass()).on(methodSpendPlugin.buildMethodsMatcher()));

                return builder;
            }
        };

        AgentBuilder agentBuilder = new AgentBuilder.Default();
        agentBuilder = agentBuilder.ignore(ElementMatchers.nameStartsWith("net.beeapm.agent."))
                .type(methodSpendPlugin.buildTypesMatcher())
                .transform(transformer);

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