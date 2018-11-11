package net.beeapm.ui.es;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class EsQueryStringMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(EsQueryStringMap.class);
    private static Map<String,String> queryMap = new HashMap<>();
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
            StringBuilder sb = new StringBuilder();
            String id = null;
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                if(line.startsWith("//")){
                    continue;
                }
                if(line.startsWith("#####")){
                    if(id != null && sb.length() > 0){
                        LOGGER.debug("key={},queryString:\n{}",id,sb.toString());
                        queryMap.put(id,sb.toString());
                    }
                    id = line.split("_")[1];
                    sb.setLength(0);
                    continue;
                }
                if(line.length() == 0){
                    continue;
                }
                sb.append(line).append("\n");
            }
            if(id != null && sb.length() > 0){
                LOGGER.debug("key={},queryString:\n{}",id,sb.toString());
                queryMap.put(id,sb.toString());
            }
        }catch (Exception e){
            LOGGER.error("",e);
        }
    }

    public String getQueryString(String id){
        return queryMap.get(id);
    }
}
