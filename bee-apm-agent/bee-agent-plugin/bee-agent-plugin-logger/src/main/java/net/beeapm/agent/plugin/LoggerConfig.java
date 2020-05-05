package net.beeapm.agent.plugin;

import net.beeapm.agent.config.AbstractBeeConfig;
import net.beeapm.agent.config.BeeConfigFactory;
import net.beeapm.agent.config.ConfigUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class LoggerConfig extends AbstractBeeConfig {
    private static LoggerConfig config;
    public static int LEVEL_TRACE = 0;
    public static int LEVEL_DEBUG = LEVEL_TRACE + 1;
    public static int LEVEL_INFO = LEVEL_DEBUG + 1;
    public static int LEVEL_WARN = LEVEL_INFO + 1;
    public static int LEVEL_ERROR = LEVEL_WARN + 1;
    public static int LEVEL_FATAL = LEVEL_ERROR + 1;
    private Boolean enable;
    private Boolean errorRate;
    private String defLevel;
    private List<LoggerPoint> points;
    private Map<String, Integer> levelMap;
    private List<String> loggerClass;

    public static LoggerConfig me() {
        if (config == null) {
            synchronized (LoggerConfig.class) {
                if (config == null) {
                    config = new LoggerConfig();
                    BeeConfigFactory.me().registryConfig("logger", config);
                }
            }
        }
        return config;
    }

    @Override
    public void clear() {
        config = null;
    }

    private LoggerConfig() {
        initConfig();
    }

    @Override
    public void initConfig() {
        if (levelMap == null) {
            levelMap = new HashMap<String, Integer>();
            levelMap.put("trace", LEVEL_TRACE);
            levelMap.put("debug", LEVEL_DEBUG);
            levelMap.put("info", LEVEL_INFO);
            levelMap.put("warn", LEVEL_WARN);
            levelMap.put("error", LEVEL_ERROR);
            levelMap.put("fatal", LEVEL_FATAL);
        }
        defLevel = ConfigUtils.me().getStr("plugins.logger.defLevel", "debug");
        initLoggerPoints();
        enable = ConfigUtils.me().getBoolean("plugins.logger.enable", true);
        errorRate = ConfigUtils.me().getBoolean("plugins.logger.errorRate", false);

    }

    public int level(String level) {
        return levelMap.get(level);
    }

    private void initLoggerPoints() {
        if (points == null) {
            points = new ArrayList<LoggerPoint>();
        } else {
            points.clear();
        }
        List<String> pointsList = ConfigUtils.me().getList("plugins.logger.points");
        if (pointsList == null || pointsList.isEmpty()) {
            return;
        }
        for (int i = 0; i < pointsList.size(); i++) {
            String item = pointsList.get(i);
            if (StringUtils.isNotBlank(item)) {
                //没有日志级别，使用默认级别
                if (!item.contains("|")) {
                    item = item + "|" + defLevel;
                }
                String[] array = StringUtils.split(item, "|");
                Integer nLevel = levelMap.get(array[1]);
                //为null时，日志级别配置配置错误，为error级别
                if (nLevel == null) {
                    nLevel = 4;
                }
                LoggerPoint point = new LoggerPoint(array[0], nLevel);
                points.add(point);
            }
        }
        //排序，使匹配度高的在前面
        Collections.sort(points, new Comparator<LoggerPoint>() {
            @Override
            public int compare(LoggerPoint a, LoggerPoint b) {
                return b.point.compareTo(a.point);
            }
        });

    }

    public boolean checkLevel(String point, String level) {
        int nLevel = levelMap.get(level);
        int size = points.size();
        for (int i = 0; i < size; i++) {
            LoggerPoint lp = points.get(i);
            if (point.startsWith(lp.point)) {
                if (nLevel >= lp.level) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isEnable() {
        return enable;
    }

    public Boolean errorRate() {
        return errorRate;
    }

    private class LoggerPoint {
        public String point;
        public int level;

        public LoggerPoint(String point, int level) {
            this.point = point;
            this.level = level;
        }
    }

    public List<String> getLoggerClass() {
        if (loggerClass != null) {
            return loggerClass;
        }
        List configLoggerClass = ConfigUtils.me().getList("plugins.logger.loggerClass");
        Set<String> loggerClassSet = new HashSet<String>();
        loggerClassSet.add("org.apache.log4j.Logger");
        loggerClassSet.add("org.apache.log4j.Category");
        loggerClassSet.add("org.apache.log4j.spi.NOPLogger");
        loggerClassSet.add("org.apache.logging.log4j.spi.AbstractLogger");
        loggerClassSet.add("ch.qos.logback.classic.Logger");
        loggerClassSet.add("org.slf4j.helpers.NOPLogger");
        loggerClassSet.add("org.apache.logging.slf4j.Log4jLogger");
        if (configLoggerClass != null && configLoggerClass.size() > 0) {
            loggerClassSet.addAll(configLoggerClass);
        }
        loggerClass = new ArrayList<String>(loggerClassSet);
        return loggerClass;
    }


}
