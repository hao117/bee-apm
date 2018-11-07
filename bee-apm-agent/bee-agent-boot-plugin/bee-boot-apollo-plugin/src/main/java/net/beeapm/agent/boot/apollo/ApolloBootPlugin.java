package net.beeapm.agent.boot.apollo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.beeapm.agent.boot.AbstractBootPlugin;
import net.beeapm.agent.common.BeeAgentJarUtils;
import net.beeapm.agent.common.SysPropKey;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by yuan on 2018/11/6.
 */
public class ApolloBootPlugin extends AbstractBootPlugin{
    @Override
    public void boot() {
        System.out.println("=============>ApolloBootPlugin start ..................");
        try {
            //{config_server_url}/configs/{appId}/{clusterName}/{namespaceName}?releaseKey={releaseKey}&ip={clientIp}
            String namespaceName = "config.yml";
            String configPath = System.getProperty(SysPropKey.BEE_CONFIG);
            if(configPath == null){
                configPath = BeeAgentJarUtils.getAgentJarDirPath() + "/" + namespaceName;
            }
            String env = System.getProperty("env").toLowerCase();
            String url = System.getProperty(env+"_meta");
            String appId = System.getProperty("app.id");
            String clusterName = System.getProperty("apollo.cluster");
            if(clusterName == null){
                clusterName = "default";
            }
            String ip = System.getProperty(SysPropKey.BEE_IP);
            url = url + "/configs/" + appId + "/" + clusterName + "/" + namespaceName;
            System.out.println(url);
            String result = sendHttpGet(url,ip);
            if(result != null && !result.isEmpty()){
                System.out.println(result);
                JSONObject json = JSON.parseObject(result);
                String content = json.getJSONObject("configurations").getString("content");
                //String releaseKey = json.getString("releaseKey");
                writeFile(configPath+".txt",content);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String sendHttpGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            if(param != null){
                url = url + "?" + param;
            }
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    private void writeFile(String path,String content){
        if(content == null){
            return;
        }
        FileWriter fileWriter = null;
        try{
            File file =new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            fileWriter = new FileWriter(file.getCanonicalPath(), false);
            fileWriter.write(content.toCharArray());
            fileWriter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fileWriter != null){
                try {
                    fileWriter.close();
                }catch (Exception e){
                }
            }
        }
    }
}
