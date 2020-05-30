package net.beeapm.agent.boot;

import net.beeapm.agent.common.*;
import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogUtil;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.FieldDefine;
import net.beeapm.agent.plugin.AbstractPlugin;
import net.beeapm.agent.plugin.InterceptPoint;
import net.beeapm.agent.plugin.PluginLoader;
import net.beeapm.agent.plugin.handler.HandlerLoader;
import net.beeapm.agent.reporter.ReporterFactory;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.jar.JarFile;

/**
 * @author yuan
 * @date 2018-08-06
 */
public class BeeAgent {
    public static void premain(String arguments, Instrumentation inst) {
        loadSpy(inst);
        LogUtil.write("\n---------------------------------Welcome BeeAPM ---------------------------------------");
        initialize();
        List<AbstractPlugin> plugins = PluginLoader.loadPlugins();
        AgentBuilder agentBuilder = new AgentBuilder.Default().ignore(ElementMatchers.nameStartsWith("net.beeapm.agent."));
        for (int i = 0; i < plugins.size(); i++) {
            final AbstractPlugin plugin = plugins.get(i);
            InterceptPoint[] interceptPoints = plugin.buildInterceptPoint();
            for (int j = 0; j < interceptPoints.length; j++) {
                final InterceptPoint interceptPoint = interceptPoints[j];
                AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
                    private final ILog log = LogFactory.getLog("Transform");

                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                            TypeDescription typeDescription,
                                                            ClassLoader classLoader, JavaModule javaModule) {
                        String className = typeDescription.getCanonicalName();
                        log.exec("class-name={}, plugin-name={}", className, plugin.getName());
                        builder = builder.visit(Advice.to(plugin.interceptorAdviceClass()).on(interceptPoint.buildMethodsMatcher()));
                        FieldDefine[] fields = plugin.buildFieldDefine();
                        if (fields != null && fields.length > 0) {
                            for (int x = 0; x < fields.length; x++) {
                                builder = builder.defineField(fields[x].name, fields[x].type, fields[x].modifiers);
                            }
                        }
                        return builder;
                    }
                };
                agentBuilder = agentBuilder.type(interceptPoint.buildTypesMatcher()).transform(transformer).asDecorator();
            }
        }

        AgentBuilder.Listener listener = buildListener();
        agentBuilder.with(listener).installOn(inst);
    }

    public static void loadSpy(Instrumentation inst) {
        try {
            String rootPath = BeeUtils.getJarDirPath();
            inst.appendToBootstrapClassLoaderSearch(new JarFile(new File(rootPath + "/bee-agent-spy.jar")));
            LogUtil.init(rootPath);
            HandlerLoader.init(rootPath);
            LogUtil.log("load bee-agent-spy.jar successful!");
        } catch (Throwable t) {
            //初始化失败LogUtil可能无法使用，这里使用BeeUtils.write来写日志
            BeeUtils.write("load bee-agent-spy.jar failed!", t, "bee.log");
            throw new RuntimeException("load bee-agent-spy.jar failed!", t);
        }
    }


    private static AgentBuilder.Listener buildListener() {
        return new AgentBuilder.Listener() {
            private final ILog log = LogFactory.getLog("TransformListener");

            @Override
            public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {
                WeavingClassLog.INSTANCE.log(typeDescription, dynamicType);
            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {
            }

            @Override
            public void onError(String s, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {
                log.error("", throwable);
            }

            @Override
            public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }
        };
    }

    private static void initialize() {
        try {
            LogUtil.log("start......");
            BootPluginFactory.init();
            IdHelper.init();
            ReporterFactory.init();
            HeartbeatTask.start();
            JvmInfoTask.start();
            LogUtil.setEmptyHandlerLog(LogFactory.getLog("EmptyHandler"));
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    HeartbeatTask.shutdown();
                    JvmInfoTask.shutdown();
                    ReporterFactory.shutdown();
                    IdHelper.shutdown();
                    LogUtil.log("shutdown all bee tasks");
                }
            }));
        } catch (Throwable e) {
            LogUtil.log("bee agent initialization failed!", e);
            throw new RuntimeException("bee agent initialization failed", e);
        }
    }
}