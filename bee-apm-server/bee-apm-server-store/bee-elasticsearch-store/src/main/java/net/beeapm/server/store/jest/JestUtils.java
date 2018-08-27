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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class JestUtils {
    private static Logger logger = LoggerFactory.getLogger(StoreFactory.class);
    static JestClient jestClient ;
    static JestUtils inst;
    private String indexKeyPrefix = "bee.store.elasticsearch.indices.";
    private String DEFAULT_INDEX_NAME = ConfigHolder.getProperty(indexKeyPrefix+"def");

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
        String prefix = "bee.store.elasticsearch.";
        String[] urls = ConfigHolder.getProperty(prefix,"url").split(",");
        HttpClientConfig.Builder builder = new  HttpClientConfig.Builder(Arrays.asList(urls));
        String userName = ConfigHolder.getProperty(prefix,"username");
        String password = ConfigHolder.getProperty(prefix,"password");
        if(StringUtils.isNotBlank(userName)){
            builder = builder.defaultCredentials(userName,password);
        }
        builder = builder.connTimeout(ConfigHolder.getPropInt(prefix,"connTimeout",60000))
                .readTimeout(ConfigHolder.getPropInt(prefix,"readTimeout",60000))
                .multiThreaded(true)
                .defaultMaxTotalConnectionPerRoute(ConfigHolder.getPropInt(prefix,"defaultMaxTotalConnectionPerRoute",2));
        //Per default this implementation will create no more than 2 concurrent connections per given route
        //.defaultMaxTotalConnectionPerRoute(<YOUR_DESIRED_LEVEL_OF_CONCURRENCY_PER_ROUTE>)
        // and no more 20 connections in total
        //.maxTotalConnection(<YOUR_DESIRED_LEVEL_OF_CONCURRENCY_TOTAL>)
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(builder.build());
        jestClient = factory.getObject();

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
                BulkableAction action = new Index.Builder(data).index(index).id(id).build();
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
        return ConfigHolder.getProperty(indexKeyPrefix+type,DEFAULT_INDEX_NAME);
    }


}
