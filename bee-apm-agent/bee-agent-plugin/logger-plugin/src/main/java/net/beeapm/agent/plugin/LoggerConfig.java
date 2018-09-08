package net.beeapm.agent.plugin;

import net.beeapm.agent.config.AbstractBeeConfig;
import net.beeapm.agent.config.BeeConfigFactory;
import net.beeapm.agent.config.ConfigUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class LoggerConfig extends AbstractBeeConfig {
    private static LoggerConfig config;
    private Boolean enable;
    private String defLevel;
    private List<LoggerPoint> points;
    private Map<String,Integer> levelMap;

    public static LoggerConfig me(){
        if(config == null){
            synchronized (LoggerConfig.class){
                if(config == null){
                    config = new LoggerConfig();
                    BeeConfigFactory.me().registryConfig("logger",config);
                }
            }
        }
        return config;
    }

    private LoggerConfig(){
        initConfig();
    }

    @Override
    public void initConfig() {
        if(levelMap == null){
            levelMap = new HashMap<String, Integer>();
            levelMap.put("trace",0);
            levelMap.put("debug",1);
            levelMap.put("info",2);
            levelMap.put("warn",3);
            levelMap.put("error",4);
            levelMap.put("fatal",5);
        }
        defLevel = ConfigUtils.me().getStr("plugins.logger.defLevel","debug");
        initLoggerPoints();
        enable = ConfigUtils.me().getBoolean("plugins.logger.enable",true);
    }

    private void initLoggerPoints(){
        if(points == null){
            points = new ArrayList<LoggerPoint>();
        }else{
            points.clear();
        }
        List<String> pointsList = ConfigUtils.me().getList("plugins.logger.points");
        if(pointsList == null || pointsList.isEmpty()){
            return;
        }
        for(int i = 0; i < pointsList.size(); i++){
            String item = pointsList.get(i);
            if(StringUtils.isNotBlank(item)){
                if(!item.contains("|")){   //没有日志级别，使用默认级别
                    item = item + "|" + defLevel;
                }
                String[] array = StringUtils.split(item,"|");
                Integer nLevel = levelMap.get(array[1]);
                if(nLevel == null){ //为null时，日志级别配置配置错误，为error级别
                    nLevel = 4;
                }
                LoggerPoint point = new LoggerPoint(array[0],nLevel);
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

    public boolean checkLevel(String point,String level){
        int nLevel = levelMap.get(level);
        int size = points.size();
        for(int i = 0; i < size; i++){
            LoggerPoint lp = points.get(i);
            if(point.startsWith(lp.point)){
                if(nLevel >= lp.level ){
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isEnable() {
        return enable;
    }

    private class LoggerPoint{
        public String point;
        public int level;
        public LoggerPoint(String point,int level){
            this.point = point;
            this.level = level;
        }
    }


}
