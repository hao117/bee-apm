package net.beeapm.agent.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import net.beeapm.agent.common.BeeAgentJarUtils;
import net.beeapm.agent.common.BeeUtils;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

public class BeeConfig {
    private static LogImpl log = LogManager.getLog(BeeConfig.class.getSimpleName());
    private static JSONObject config;
    private static BeeConfig beeCfg;
    public static BeeConfig inst(){
        if(beeCfg == null){
            synchronized (BeeConfig.class){
                if(beeCfg == null){
                    beeCfg = new BeeConfig();
                }
            }
        }
        return beeCfg;
    }

    public BeeConfig(){
        init();
    }

    private void init(){
        String path = System.getProperty("bee.config");
        if(StringUtils.isBlank(path)){
            path = BeeAgentJarUtils.getAgentJarDirPath() + "/config.yml";
        }
        FileInputStream fis = null;
        try {
            Yaml yaml = new Yaml();
            fis = new FileInputStream(new File(path));
            Map map = yaml.load(fis);
            config = JSON.parseObject(JSON.toJSONString(map));
        }catch (Exception e){
            log.error("",e);
        }finally {
            BeeUtils.close(fis);
        }
    }

    public String getStr(String key){
        return (String) JSONPath.eval(config,"$."+key);
    }

    public Integer getInt(String key){
        return (Integer) JSONPath.eval(config,"$."+key);
    }

    public Long getLong(String key){
        return (Long) JSONPath.eval(config,"$."+key);
    }

    public String getStr(String key, String def){
        String val = getStr(key);
        if(val == null){
            return def;
        }
        return val;
    }

    public Integer getInt(String key, Integer def){
        Integer val = getInt(key);
        if(val == null){
            return def;
        }
        return val;
    }

    public Long getLong(String key, Long def){
        Long val = getLong(key);
        if(val == null){
            return def;
        }
        return val;
    }

    public List<String> getList(String key){
        return (List<String>) JSONPath.eval(config,"$."+key);
    }

}
