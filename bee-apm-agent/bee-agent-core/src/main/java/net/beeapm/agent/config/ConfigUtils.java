package net.beeapm.agent.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import net.beeapm.agent.common.BeeThreadFactory;
import net.beeapm.agent.common.BeeUtils;
import net.beeapm.agent.common.SysPropKey;
import net.beeapm.agent.log.BeeLogUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 配置文件读取
 *
 * @author yuan
 */
public class ConfigUtils {
    private long configLastTime = 0;
    private String configPath;
    private JSONObject config;
    private static ConfigUtils instance;
    private static final String THREAD_NAME = "config-listener";
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1, new BeeThreadFactory(THREAD_NAME));

    public static ConfigUtils me() {
        if (instance == null) {
            synchronized (ConfigUtils.class) {
                if (instance == null) {
                    instance = new ConfigUtils();
                }
            }
        }
        return instance;
    }

    private void configFileModifyListener() {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(loadConfig()){
                    BeeConfigFactory.me().refresh();
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }


    public ConfigUtils() {
        initConfigPath();
        loadConfig();
        configFileModifyListener();
    }

    private void initConfigPath() {
        configPath = System.getProperty(SysPropKey.BEE_CONFIG);
        if (BeeUtils.isBlank(configPath)) {
            configPath = BeeUtils.getJarDirPath() + "/config.yml";
        }
    }

    private boolean loadConfig() {
        FileInputStream fis = null;
        boolean isReload = false;
        try {
            Yaml yaml = new Yaml();
            File configFile = new File(configPath);
            long lastTime = configFile.lastModified();
            if (lastTime > configLastTime) {
                configLastTime = configFile.lastModified();
                fis = new FileInputStream(configFile);
                Map map = yaml.load(fis);
                try {
                    readWriteLock.writeLock().lock();
                    config = JSON.parseObject(JSON.toJSONString(map));
                    isReload = true;
                } finally {
                    readWriteLock.writeLock().unlock();
                }
            }
        } catch (Exception e) {
            BeeLogUtil.log("配置文件加载失败" + configPath, e);
        } finally {
            BeeUtils.close(fis);
        }
        return isReload;
    }

    private Object parseValue(String key) {
        try {
            readWriteLock.readLock().lock();
            return JSONPath.eval(config, "$." + key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public Object getVal(String key) {
        return parseValue(key);
    }

    public String getStr(String key) {
        Object obj = parseValue(key);
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    public Integer getInt(String key) {
        return (Integer) parseValue(key);
    }

    public Long getLong(String key) {
        Object val = getVal(key);
        if (val != null) {
            return Long.parseLong(val.toString());
        }
        return null;
    }

    public String getStr(String key, String def) {
        String val = getStr(key);
        if (val == null) {
            return def;
        }
        return val;
    }

    public Integer getInt(String key, Integer def) {
        Integer val = getInt(key);
        if (val == null) {
            return def;
        }
        return val;
    }

    public Long getLong(String key, Long def) {
        Long val = getLong(key);
        if (val == null) {
            return def;
        }
        return val;
    }

    public Boolean getBoolean(String key) {
        Boolean b = (Boolean) parseValue(key);
        return b;
    }

    public Boolean getBoolean(String key, Boolean def) {
        Boolean b = getBoolean(key);
        if (b == null) {
            return def;
        }
        return b;
    }

    public List<String> getList(String key) {
        return (List<String>) parseValue(key);
    }


}
