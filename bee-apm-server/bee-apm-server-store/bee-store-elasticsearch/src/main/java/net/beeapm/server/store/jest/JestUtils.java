package net.beeapm.server.store.jest;

import com.alibaba.fastjson.JSONObject;
import io.searchbox.action.BulkableAction;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import net.beeapm.server.core.common.ConfigHolder;
import net.beeapm.server.core.store.StoreFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class JestUtils {
    private static Logger logger = LoggerFactory.getLogger(StoreFactory.class);
    static JestClient jestClient ;
    static JestUtils inst;
    private static String KEY_INDEX_PREFIX = "bee.store.elasticsearch.indices";
    private static String KEY_CONFIG_PREFIX = "bee.store.elasticsearch.config";
    private static String DEFAULT_INDEX_NAME;
    private static Properties INDEX_MAP;

    public static JestUtils inst(){
        if(jestClient == null){
            synchronized (JestUtils.class){
                if(jestClient == null){
                    inst = new JestUtils();
                    initJestClient();
                }
            }
        }
        return inst;
    }

    private static void initJestClient(){
        INDEX_MAP = ConfigHolder.getProperties(KEY_INDEX_PREFIX,true);
        Properties esConfig = ConfigHolder.getProperties(KEY_CONFIG_PREFIX,true);
        DEFAULT_INDEX_NAME = INDEX_MAP.getProperty("default","bee-default");
        String[] urls = esConfig.getProperty("url").split(",");
        HttpClientConfig.Builder builder = new  HttpClientConfig.Builder(Arrays.asList(urls));
        String userName = esConfig.getProperty("username");
        String password = esConfig.getProperty("password");
        if(StringUtils.isNotBlank(userName)){
            builder = builder.defaultCredentials(userName,password);
        }
        builder = buildConfig(builder,esConfig);
//        builder = builder.connTimeout(ConfigHolder.getPropInt(prefix,"connTimeout",60000))
//                .readTimeout(ConfigHolder.getPropInt(prefix,"readTimeout",60000))
//                .multiThreaded(true)
//                .defaultMaxTotalConnectionPerRoute(ConfigHolder.getPropInt(prefix,"defaultMaxTotalConnectionPerRoute",2));
//        //Per default this implementation will create no more than 2 concurrent connections per given route
//        //.defaultMaxTotalConnectionPerRoute(<YOUR_DESIRED_LEVEL_OF_CONCURRENCY_PER_ROUTE>)
//        // and no more 20 connections in total
//        //.maxTotalConnection(<YOUR_DESIRED_LEVEL_OF_CONCURRENCY_TOTAL>)
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(builder.build());
        jestClient = factory.getObject();

    }

    private static HttpClientConfig.Builder buildConfig(HttpClientConfig.Builder builder,Properties esConfig){
        esConfig.remove("username");
        esConfig.remove("password");
        esConfig.remove("url");
        Iterator iterator = esConfig.entrySet().iterator();
        while (iterator.hasNext()){
            try {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
                Method method = builder.getClass().getMethod(entry.getKey(), entry.getValue().getClass());
                if(method != null){
                    builder = (HttpClientConfig.Builder)method.invoke(builder,entry.getValue());
                }
            }catch (Exception e){
                logger.error("",e);
            }
        }
        return builder;

    }

    public void insert(Object ... datas){
        try {
            if (datas == null || datas.length == 0) {
                return;
            }
            List<BulkableAction> bulkList = new ArrayList<>();
            for (Object item : datas) {
                JSONObject data = (JSONObject)item;
                String index = getIndexName(data);
                String id = data.getString("id");
                String type = data.getString("type");
                Object time = data.get("time");
                if(time instanceof  Long){
                    data.put("time",new Date((Long)time));
                }
                BulkableAction action = new Index.Builder(data).index(index).type(type).id(id).build();
                bulkList.add(action);
            }
            Bulk bulk = new Bulk.Builder()
                    .addAction(bulkList)
                    .build();
            JestResult jr = jestClient.execute(bulk);
            if(!jr.isSucceeded()){
                logger.error("Bulk Error : "+jr.getErrorMessage());
            }
        }catch (Exception e){
            logger.error("",e);
        }
    }

    private String getIndexName(JSONObject data){
        String type = data.getString("type");
        String date = DateFormatUtils.format(new Date(),"yyyy.MM.dd");
        return INDEX_MAP.getProperty(type,DEFAULT_INDEX_NAME) + "-" + date;
    }


}
