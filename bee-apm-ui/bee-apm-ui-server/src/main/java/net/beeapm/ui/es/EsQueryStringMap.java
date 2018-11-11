package net.beeapm.ui.es;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class EsQueryStringMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(EsQueryStringMap.class);
    private static Map<String, List> queryMap = new HashMap<>();
    static EsQueryStringMap map;
    public static EsQueryStringMap me(){
        if(map == null){
            synchronized (EsQueryStringMap.class){
                if(map == null){
                    map = new EsQueryStringMap();
                }
            }
        }
        return map;
    }

    public EsQueryStringMap(){
        init();
    }

    private void init(){
        try {
            InputStream in = this.getClass().getResourceAsStream("/es_query_string.map");
            List<String> list = new ArrayList<>();
            String id = null;
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                if(line.startsWith("//")){
                    continue;
                }
                line = line.trim();
                if(line.length() == 0){
                    continue;
                }
                if(line.startsWith("#####")){
                    if(id != null && list.size() > 0){
                        if(LOGGER.isDebugEnabled()) {
                            LOGGER.debug("key={},queryString:\n{}", id, JSON.toJSON(list));
                        }
                        queryMap.put(id,list);
                    }
                    id = line.split("_")[1];
                    list = new ArrayList<>();
                    continue;
                }
                list.add(line);
            }
            if(id != null && list.size() > 0){
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("key={},queryString:\n{}", id, JSON.toJSON(list));
                }
                queryMap.put(id,list);
            }
        }catch (Exception e){
            LOGGER.error("",e);
        }
    }

    public String getQueryString(String id, Map<String,String> param){
        List<String> list = queryMap.get(id);
        if(list == null || list.isEmpty()){
            return null;
        }
        if(param != null && !param.isEmpty()) {
            for (Entry<String, String> entry : param.entrySet()) {
                int i = 0;
                for (String item : list) {
                    String key = "{{" + entry + "}}";
                    if (item.contains(key)) {
                        item = item.replace(key, entry.getValue());
                        if (item.startsWith("@if")) {
                            item = item.substring(4);
                        }
                        list.set(i, item);
                    }
                    i++;
                }
            }
        }
        String queryString = String.join(" ",list.toArray(new String[list.size()]));
        LOGGER.debug("queryString = {}",queryString);
        return queryString;
    }

}
