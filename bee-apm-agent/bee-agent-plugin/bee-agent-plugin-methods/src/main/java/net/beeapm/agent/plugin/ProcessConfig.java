package net.beeapm.agent.plugin;

import net.beeapm.agent.config.AbstractBeeConfig;
import net.beeapm.agent.config.BeeConfigFactory;
import net.beeapm.agent.config.ConfigUtils;

import java.util.*;
import java.util.regex.Pattern;

public class ProcessConfig extends AbstractBeeConfig {
    private static ProcessConfig config;
    private Boolean enableParam;
    private Boolean enable;
    private Boolean enableMethodSign;
    private Set<String> excludeParamTypePrefix = new HashSet<String>();
    private long spend;
    private Boolean enableError;
    private Set<String> includeErrorPointPrefix = new HashSet<String>();
    private Set<String> excludeErrorPointPrefix = new HashSet<String>();
    private List<Pattern> excludePointMatchesList = new ArrayList<Pattern>();
    private List<Pattern> includePointMatchesList = new ArrayList<Pattern>();

    public static ProcessConfig me() {
        if (config == null) {
            synchronized (ProcessConfig.class) {
                if (config == null) {
                    config = new ProcessConfig();
                    BeeConfigFactory.me().registryConfig("process", config);
                }
            }
        }
        return config;
    }

    @Override
    public void clear() {
        config = null;
    }

    private ProcessConfig() {
        initConfig();
    }

    @Override
    public void initConfig() {
        excludeParamTypePrefix.clear();
        includeErrorPointPrefix.clear();
        excludeErrorPointPrefix.clear();

        enableParam = ConfigUtils.me().getBoolean("plugins.process.param.enable", true);
        enable = ConfigUtils.me().getBoolean("plugins.process.enable", true);
        enableMethodSign = ConfigUtils.me().getBoolean("plugins.process.enableMethodSign", false);
        List<String> excludeParamTypePrefixList = ConfigUtils.me().getList("plugins.process.param.excludeTypePrefix");
        if (excludeParamTypePrefixList != null && !excludeParamTypePrefixList.isEmpty()) {
            excludeParamTypePrefix.addAll(excludeParamTypePrefixList);
        }

        enableError = ConfigUtils.me().getBoolean("plugins.process.error.enable", true);
        List<String> includeErrorPointPrefixList = ConfigUtils.me().getList("plugins.process.error.includeErrorPointPrefix");
        List<String> excludeErrorPointPrefixList = ConfigUtils.me().getList("plugins.process.error.excludeErrorPointPrefix");
        if (includeErrorPointPrefixList != null && !includeErrorPointPrefixList.isEmpty()) {
            includeErrorPointPrefix.addAll(includeErrorPointPrefixList);
        }
        if (excludeErrorPointPrefixList != null && !excludeErrorPointPrefixList.isEmpty()) {
            excludeErrorPointPrefix.addAll(excludeErrorPointPrefixList);
        }
        spend = ConfigUtils.me().getInt("plugins.process.spend", -1);

        List<String> excludeParamPointList = ConfigUtils.me().getList("plugins.process.param.excludePointMatches");
        if (excludeParamPointList != null && excludeParamPointList.size() > 0) {
            int size = excludeParamPointList.size();
            for (int i = 0; i < size; i++) {
                excludePointMatchesList.add(Pattern.compile(excludeParamPointList.get(i)));
            }
        }
        List<String> includeParamPointList = ConfigUtils.me().getList("plugins.process.param.includePointMatches");
        if (includeParamPointList != null && includeParamPointList.size() > 0) {
            int size = includeParamPointList.size();
            for (int i = 0; i < size; i++) {
                includePointMatchesList.add(Pattern.compile(includeParamPointList.get(i)));
            }
        }

    }

    public boolean isEnableParam() {
        return enableParam;
    }

    public boolean isEnable() {
        return enable;
    }

    public boolean isEnableError() {
        return enableError;
    }

    public boolean isEnableMethodSign() {
        return enableMethodSign;
    }

    public boolean isExcludeParamType(Class clazz) {
        String name = clazz.getName();
        Iterator<String> it = excludeParamTypePrefix.iterator();
        while (it.hasNext()) {
            String prefix = it.next();
            if (name.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public long getSpend() {
        return spend;
    }

    public boolean checkErrorPoint(String point) {
        if (!enableError) {
            return false;
        }
        //黑名单，优先级别最高
        Iterator<String> exclude = excludeErrorPointPrefix.iterator();
        while (exclude.hasNext()) {
            String prefix = exclude.next();
            if (point.startsWith(prefix)) {
                return false;
            }
        }
        //白名单
        Iterator<String> include = includeErrorPointPrefix.iterator();
        while (include.hasNext()) {
            String prefix = include.next();
            if (point.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断采集点是否采集参数，true进行采集，false不采集
     *
     * @param point
     * @return
     */
    public boolean checkParamPoint(String point) {
        int excludeSize = excludePointMatchesList.size();
        for (int i = 0; i < excludeSize; i++) {
            if (excludePointMatchesList.get(i).matcher(point).matches()) {
                return false;
            }
        }
        int includeSize = includePointMatchesList.size();
        if (includeSize == 0) {//没有配置采集范围，默认全部采集
            return true;
        }
        //有配置采集范围，只采集配置的范围
        for (int i = 0; i < includeSize; i++) {
            if (includePointMatchesList.get(i).matcher(point).matches()) {
                return true;
            }
        }
        //不在采集范围的不采集
        return false;
    }
}
