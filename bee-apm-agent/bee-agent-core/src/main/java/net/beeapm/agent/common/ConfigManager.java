package net.beeapm.agent.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public class ConfigManager {
    private static JSONObject jsonConfig;
    private static ConfigManager configManager;
    public synchronized static ConfigManager inst(){
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(BeeAgentJarUtils.getAgentJarDirPath() + "/config.json"));// 读取NAMEID对应值
            String tmp = null;
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }finally {
            if(br != null){
                try {
                    br.close();
                }catch (Exception e){

                }
            }
        }
        jsonConfig = JSON.parseObject(sb.toString());
        configManager = new ConfigManager();
        return configManager;
    }

    public List<String> getList(String path){
        return (List<String>)JSONPath.eval(configManager,"$."+path);
    }
}
